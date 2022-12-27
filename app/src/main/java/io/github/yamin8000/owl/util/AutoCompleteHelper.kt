/*
 *     Owl: an android app for Owlbot Dictionary API
 *     AutoCompleteHelper.kt Created by Yamin Siahmargooei at 2022/10/22
 *     This file is part of Owl.
 *     Copyright (C) 2022  Yamin Siahmargooei
 *
 *     Owl is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Owl is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Owl.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.util

import android.content.Context
import android.content.res.Resources.NotFoundException
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.util.Constants.DEFAULT_N_GRAM_SIZE
import io.github.yamin8000.owl.util.Constants.NOT_WORD_CHARS_REGEX
import io.github.yamin8000.owl.util.Constants.db
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.roundToInt

class AutoCompleteHelper(
    private val context: Context,
    coroutineScope: CoroutineScope,
    userData: List<String> = listOf()
) {
    private var data = setOf<String>()

    init {
        data = getBasic2000Data().plus(userData).toSet()
        coroutineScope.launch { data = data.plus(getOldSearchData()) }
    }

    private fun getBasic2000Data() = try {
        context.resources.openRawResource(R.raw.basic2000)
            .bufferedReader()
            .use { it.readText() }
            .split(',')
            .map { it.replace(NOT_WORD_CHARS_REGEX, "") }
    } catch (e: NotFoundException) {
        listOf()
    }

    suspend fun suggestTermsForSearch(
        searchTerm: String
    ): List<String> {
        data = data.plus(getOldSearchData())

        val term = searchTerm.lowercase().replace(NOT_WORD_CHARS_REGEX, "")
        val nGramSize = nGramSizeProvider(term)
        if (data.contains(term)) return listOf(term)
        val searchTermGrams = term.windowed(nGramSize)
        val suggestions = buildSet {
            searchTermGrams.forEach { gram ->
                addAll(data.filter { word -> word.contains(gram) })
            }
        }
        return sortSuggestions(suggestions, term)
    }

    private fun sortSuggestions(
        suggestions: Set<String>,
        searchTerm: String
    ): List<String> {
        val nGramSize = nGramSizeProvider(searchTerm)
        if (suggestions.isNotEmpty() && suggestions.size > 1) {
            val searchTermGrams = searchTerm.windowed(nGramSize)
            val rankedSuggestions = buildList {
                suggestions.forEach { suggestion ->
                    val rank = suggestion.windowed(nGramSize)
                        .intersect(searchTermGrams.toSet())
                        .size
                    add(rank to suggestion)
                }
            }
            return rankedSuggestions.asSequence()
                .sortedBy { abs(it.second.length - searchTerm.length) }
                .sortedByDescending {
                    it.second.startsWith(searchTerm.take(nGramSize)) ||
                            it.second.endsWith(searchTerm.takeLast(nGramSize))
                }
                .sortedByDescending { it.first }
                .map { it.second }
                .toList()
        } else return suggestions.toList()
    }

    private fun nGramSizeProvider(
        searchTerm: String
    ): Int {
        return if (searchTerm.length > DEFAULT_N_GRAM_SIZE) {
            val size = ceil(searchTerm.length.toFloat() / DEFAULT_N_GRAM_SIZE).roundToInt()
            if (size < DEFAULT_N_GRAM_SIZE) DEFAULT_N_GRAM_SIZE
            else size
        } else DEFAULT_N_GRAM_SIZE
    }

    private suspend fun getOldSearchData() = db.wordDao().getAll().map { it.word }
}
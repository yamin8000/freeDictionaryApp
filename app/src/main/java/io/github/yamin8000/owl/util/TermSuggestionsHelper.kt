/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     TermSuggestionsHelper.kt Copyrighted by Yamin Siahmargooei at 2024/5/9
 *     TermSuggestionsHelper.kt Last modified at 2024/3/23
 *     This file is part of freeDictionaryApp/freeDictionaryApp.app.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.app.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.app.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.util

import android.content.Context
import android.content.res.Resources.NotFoundException
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.util.Constants.DEFAULT_N_GRAM_SIZE
import io.github.yamin8000.owl.util.Constants.NOT_WORD_CHARS_REGEX
import io.github.yamin8000.owl.util.Constants.db
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.roundToInt

/**
 * A helper class to provide term suggestions
 * using simple n-gram algorithms
 */
class TermSuggestionsHelper(
    private val context: Context,
    userData: List<String> = listOf()
) {
    private val scope = CoroutineScope(Dispatchers.IO)
    private var items = setOf<String>()

    init {
        items = getBasic2000Data().plus(userData).toSet()
        scope.launch { items = items.plus(getOldSearchData()) }
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

    /**
     * Suggest terms for [searchTerm] based on saved data (previous search data)
     */
    suspend fun suggestTermsForSearch(
        searchTerm: String
    ): List<String> {
        items = items.plus(getOldSearchData())

        val term = searchTerm.lowercase().replace(NOT_WORD_CHARS_REGEX, "")
        val nGramSize = nGramSizeProvider(term)
        if (items.contains(term)) return listOf(term)
        val searchTermGrams = term.windowed(nGramSize)
        val suggestions = buildSet {
            searchTermGrams.forEach { gram ->
                addAll(items.filter { word -> word.contains(gram) })
            }
        }
        return sortSuggestions(suggestions, term).filter { it.isNotBlank() }
    }

    private fun sortSuggestions(
        suggestions: Set<String>,
        searchTerm: String
    ): List<String> {
        val nGramSize = nGramSizeProvider(searchTerm)
        return if (suggestions.isNotEmpty() && suggestions.size > 1) {
            val searchTermGrams = searchTerm.windowed(nGramSize)
            val rankedSuggestions = getRankedSuggestions(suggestions, nGramSize, searchTermGrams)
            rankedSuggestions.asSequence()
                .sortedBy { abs(it.second.length - searchTerm.length) }
                .sortedByDescending {
                    it.second.startsWith(searchTerm.take(nGramSize)) ||
                            it.second.endsWith(searchTerm.takeLast(nGramSize))
                }
                .sortedByDescending { it.first }
                .map { it.second }
                .toList()
        } else suggestions.toList()
    }

    private fun getRankedSuggestions(
        suggestions: Set<String>,
        nGramSize: Int,
        searchTermGrams: List<String>
    ) = buildList {
        suggestions.forEach { suggestion ->
            val rank = suggestion.windowed(nGramSize)
                .intersect(searchTermGrams.toSet())
                .size
            add(rank to suggestion)
        }
    }

    private fun nGramSizeProvider(
        searchTerm: String
    ) = if (searchTerm.length > DEFAULT_N_GRAM_SIZE) {
        val size = ceil(searchTerm.length.toFloat() / DEFAULT_N_GRAM_SIZE).roundToInt()
        if (size < DEFAULT_N_GRAM_SIZE) DEFAULT_N_GRAM_SIZE
        else size
    } else DEFAULT_N_GRAM_SIZE

    private suspend fun getOldSearchData() = db.termDao().all().map { it.word }
}
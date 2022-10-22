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
import java.io.File
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.roundToInt

class AutoCompleteHelper(
    private val context: Context,
    userData: List<String> = listOf()
) {
    private var data = setOf<String>()

    init {
        val searchData = getOldSearchData()
        data = getBasic2000Data().plus(userData).plus(searchData).toSet()
    }

    private fun getOldSearchData(): List<String> {
        val searchDataFile = File(context.cacheDir, "words.txt")
        var searchData = listOf<String>()
        if (searchDataFile.exists()) {
            searchData = searchDataFile
                .readText()
                .split(',')
                .filter { it.isNotBlank() }
        }
        return searchData
    }

    private fun getBasic2000Data() = try {
        context.resources.openRawResource(R.raw.basic2000)
            .bufferedReader()
            .use { it.readText() }
            .split(',')
    } catch (e: NotFoundException) {
        listOf()
    }

    fun suggestTermsForSearch(
        searchTerm: String
    ): List<String> {
        data = data.plus(getOldSearchData())
        val nGramSize = nGramSizeProvider(searchTerm)
        if (data.contains(searchTerm)) return listOf(searchTerm)
        val userSearchTermGrams = searchTerm.windowed(nGramSize)
        var suggestions = mutableSetOf<String>()
        userSearchTermGrams.forEach {
            suggestions.addAll(data.filter { word -> word.contains(it) })
        }
        suggestions = suggestions.filter {
            it.windowed(nGramSize).union(userSearchTermGrams).size > nGramSize / 2
        }.toMutableSet()
        return sortSuggestions(suggestions, searchTerm)
    }

    private fun sortSuggestions(
        suggestions: Set<String>,
        searchTerm: String
    ): List<String> {
        val nGramSize = nGramSizeProvider(searchTerm)
        return if (suggestions.isNotEmpty() && suggestions.size > 1) {
            val rankedSuggestions = mutableListOf<Pair<Int, String>>()
            suggestions.forEach { suggestion ->
                val rank = suggestion.windowed(nGramSize)
                    .intersect(suggestions.toSet())
                    .size
                rankedSuggestions.add(rank to suggestion)
            }
            rankedSuggestions.asSequence()
                .sortedByDescending { it.first }
                .map { it.second }
                .sortedBy { abs(it.length - searchTerm.length) }
                .sortedByDescending {
                    it.startsWith(searchTerm.take(nGramSize)) ||
                            it.endsWith(searchTerm.takeLast(nGramSize))
                }
                .toList()
        } else suggestions.toList()
    }

    private fun nGramSizeProvider(
        searchTerm: String
    ): Int {
        return if (searchTerm.length > DEFAULT_N_GRAM_SIZE) {
            val size = ceil(searchTerm.length.toFloat() / DEFAULT_N_GRAM_SIZE / 2).roundToInt()
            if (size < DEFAULT_N_GRAM_SIZE) DEFAULT_N_GRAM_SIZE
            else size
        } else DEFAULT_N_GRAM_SIZE
    }
}
/*
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main
 *     TermSuggesterRepositoryImpl.kt Copyrighted by Yamin Siahmargooei at 2024/8/19
 *     TermSuggesterRepositoryImpl.kt Last modified at 2024/8/19
 *     This file is part of freeDictionaryApp/freeDictionaryApp.feature_home.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.feature_home.data.repository

import android.app.Application
import android.content.res.Resources.NotFoundException
import io.github.yamin8000.owl.feature_home.R
import io.github.yamin8000.owl.search.data.datasource.local.dao.DAOs
import io.github.yamin8000.owl.feature_home.domain.repository.TermSuggesterRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.roundToInt

class TermSuggesterRepositoryImpl(
    private val dao: DAOs.TermDao,
    private val app: Application
) : TermSuggesterRepository {
    private val defaultNGramSize = 3
    private val notWordsRegex = Regex("\\W+")

    override suspend fun suggestTerms(searchTerm: String): List<String> {
        items = items.plus(getOldSearchData())

        val term = searchTerm.lowercase().replace(notWordsRegex, "")
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

    private val scope = CoroutineScope(Dispatchers.IO)
    private var items = setOf<String>()

    init {
        items = getBasic2000Data().toSet()
        scope.launch { items = items.plus(getOldSearchData()) }
    }

    private fun getBasic2000Data() = try {
        app.resources.openRawResource(R.raw.basic2000)
            .bufferedReader()
            .use { it.readText() }
            .split(',')
            .map { it.replace(notWordsRegex, "") }
    } catch (e: NotFoundException) {
        listOf()
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
    ) = if (searchTerm.length > defaultNGramSize) {
        val size = ceil(searchTerm.length.toFloat() / defaultNGramSize).roundToInt()
        if (size < defaultNGramSize) defaultNGramSize
        else size
    } else defaultNGramSize

    private suspend fun getOldSearchData() = dao.all().map { it.word }
}
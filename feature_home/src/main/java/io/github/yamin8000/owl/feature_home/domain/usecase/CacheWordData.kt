/*
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main
 *     CacheWordData.kt Copyrighted by Yamin Siahmargooei at 2024/8/30
 *     CacheWordData.kt Last modified at 2024/8/30
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

package io.github.yamin8000.owl.feature_home.domain.usecase

import io.github.yamin8000.owl.common.ui.util.StringUtils.sanitizeWords
import io.github.yamin8000.owl.feature_home.domain.model.Entry
import io.github.yamin8000.owl.feature_home.domain.model.Meaning
import io.github.yamin8000.owl.feature_home.domain.repository.local.TermRepository

class CacheWordData(
    private val termRepository: TermRepository
) {
    suspend operator fun invoke(wordEntry: Entry) {
        val oldData = termRepository.all().toSet()
        var newData = extractDataFromEntry(wordEntry.meanings)

        if (!oldData.contains(wordEntry.word)) {
            newData.add(wordEntry.word)
        }

        newData = sanitizeWords(newData).filter { it !in oldData }.toMutableSet()

        addWordDataToCache(newData)
    }

    private suspend fun addWordDataToCache(newData: Set<String>) {
        newData.forEach { item -> termRepository.add(item) }
    }

    private fun extractDataFromEntry(
        meanings: List<Meaning>
    ) = meanings.asSequence()
        .flatMap { (partOfSpeech, definitions, _, _) ->
            listOf(partOfSpeech)
                .plus(definitions.map { it.definition })
                .plus(definitions.map { it.example })
                .plus(definitions.flatMap { it.synonyms })
                .plus(definitions.flatMap { it.antonyms })
        }.filterNotNull()
        .map { it.split(Regex("\\s+")) }
        .flatten()
        .map { it.trim() }
        .toMutableSet()
}
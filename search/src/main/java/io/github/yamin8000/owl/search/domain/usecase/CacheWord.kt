/*
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main
 *     CacheWord.kt Copyrighted by Yamin Siahmargooei at 2024/8/30
 *     CacheWord.kt Last modified at 2024/8/30
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

package io.github.yamin8000.owl.search.domain.usecase

import io.github.yamin8000.owl.search.domain.model.Entry
import io.github.yamin8000.owl.search.domain.model.Meaning
import io.github.yamin8000.owl.search.domain.repository.local.DefinitionRepository
import io.github.yamin8000.owl.search.domain.repository.local.EntryRepository
import io.github.yamin8000.owl.search.domain.repository.local.MeaningRepository
import io.github.yamin8000.owl.search.domain.repository.local.PhoneticRepository

class CacheWord(
    private val entryRepository: EntryRepository,
    private val meaningRepository: MeaningRepository,
    private val definitionRepository: DefinitionRepository,
    private val phoneticRepository: PhoneticRepository
) {
    suspend operator fun invoke(wordEntry: Entry) {
        val isNotCached = entryRepository.findByTerm(wordEntry.word.lowercase().trim()) == null
        if (isNotCached) {
            addWordEntryToDatabase(wordEntry)
        }
    }

    private suspend fun addWordEntryToDatabase(wordEntry: Entry) {
        val entryId = entryRepository.add(wordEntry)

        wordEntry.phonetics.forEach { phonetic ->
            phoneticRepository.add(phonetic.copy(entryId = entryId))
        }

        wordEntry.meanings.forEach { (partOfSpeech, definitions, _, _) ->
            val meaningId = meaningRepository.add(
                Meaning(
                    entryId = entryId,
                    partOfSpeech = partOfSpeech,
                    definitions = emptyList(),
                    antonyms = emptyList(),
                    synonyms = emptyList()
                )
            )
            definitions.forEach { definition ->
                definitionRepository.add(definition.copy(meaningId = meaningId))
            }
        }
    }
}
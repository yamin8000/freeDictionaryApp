/*
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main
 *     GetCachedWord.kt Copyrighted by Yamin Siahmargooei at 2024/8/25
 *     GetCachedWord.kt Last modified at 2024/8/25
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

import io.github.yamin8000.owl.feature_home.data.datasource.local.entity.EntryEntity
import io.github.yamin8000.owl.feature_home.domain.model.Definition
import io.github.yamin8000.owl.feature_home.domain.model.Entry
import io.github.yamin8000.owl.feature_home.domain.model.License
import io.github.yamin8000.owl.feature_home.domain.model.Meaning
import io.github.yamin8000.owl.feature_home.domain.model.Phonetic
import io.github.yamin8000.owl.feature_home.domain.repository.local.DefinitionRepository
import io.github.yamin8000.owl.feature_home.domain.repository.local.MeaningRepository
import io.github.yamin8000.owl.feature_home.domain.repository.local.PhoneticRepository
import io.github.yamin8000.owl.feature_home.domain.repository.local.EntryRepository

class GetCachedWord(
    private val entryRepository: EntryRepository,
    private val phoneticRepository: PhoneticRepository,
    private val meaningRepository: MeaningRepository,
    private val definitionRepository: DefinitionRepository
) {
    suspend operator fun invoke(term: String): Entry? {
        val entry = entryRepository.findByTerm(term)
        return if (entry != null) createEntry(entry) else null
    }

    private suspend fun createEntry(entry: EntryEntity): Entry {
        val phonetics = phoneticRepository.findAllByEntryId(entry.id)
        val meanings = meaningRepository.findAllByEntryId(entry.id)

        return Entry(
            word = entry.word,
            phonetics = phonetics.map { Phonetic(text = it.value) },
            license = License("", ""),
            sourceUrls = listOf(),
            meanings = meanings.map { meaning ->
                Meaning(
                    partOfSpeech = meaning.partOfSpeech,
                    antonyms = listOf(),
                    synonyms = listOf(),
                    definitions = definitionRepository.findAllByMeaningId(meaning.id).map {
                        Definition(
                            definition = it.definition,
                            example = it.example,
                            antonyms = listOf(),
                            synonyms = listOf()
                        )
                    }
                )
            }
        )
    }
}
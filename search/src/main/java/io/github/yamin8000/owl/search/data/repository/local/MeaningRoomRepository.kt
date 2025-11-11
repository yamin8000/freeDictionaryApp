/*
 *     freeDictionaryApp/freeDictionaryApp.search.main
 *     MeaningRoomRepository.kt Copyrighted by Yamin Siahmargooei at 2024/9/5
 *     MeaningRoomRepository.kt Last modified at 2024/9/5
 *     This file is part of freeDictionaryApp/freeDictionaryApp.search.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.search.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.search.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.search.data.repository.local

import io.github.yamin8000.owl.common.util.DateTimeUtils.epoch
import io.github.yamin8000.owl.search.data.datasource.local.dao.DAOs
import io.github.yamin8000.owl.search.data.datasource.local.entity.MeaningEntity
import io.github.yamin8000.owl.search.domain.model.Meaning
import io.github.yamin8000.owl.search.domain.repository.local.DefinitionRepository
import io.github.yamin8000.owl.search.domain.repository.local.MeaningRepository
import io.github.yamin8000.owl.search.domain.repository.local.util.HasEntry
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

class MeaningRoomRepository(
    private val meaningDao: DAOs.MeaningDao,
    private val definitionRepository: DefinitionRepository
) : MeaningRepository, BaseRoomRepository<Meaning, MeaningEntity>(meaningDao), HasEntry<Meaning> {

    override suspend fun findAllByEntryId(entryId: Long): List<Meaning> {
        return meaningDao.where("entryId", entryId).mapNotNull { mapToDomain(it) }
    }

    override suspend fun mapToDomain(item: MeaningEntity?): Meaning? {
        return if (item != null) {
            Meaning(
                id = item.id,
                entryId = item.entryId,
                partOfSpeech = item.partOfSpeech,
                definitions = definitionRepository.findAllByMeaningId(item.id).toImmutableList(),
                synonyms = persistentListOf(),
                antonyms = persistentListOf()
            )
        } else null
    }

    override suspend fun findEntity(item: Meaning): MeaningEntity? {
        return if (item.id != null) meaningDao.find(item.id)
        else null
    }

    override suspend fun add(item: Meaning): Long {
        return if (item.entryId != null) {
            meaningDao.insert(
                MeaningEntity(
                    entryId = item.entryId,
                    partOfSpeech = item.partOfSpeech,
                    createdAt = epoch(),
                )
            )
        } else -1
    }
}

/*
 *     freeDictionaryApp/freeDictionaryApp.search.main
 *     DefinitionRoomRepository.kt Copyrighted by Yamin Siahmargooei at 2024/9/5
 *     DefinitionRoomRepository.kt Last modified at 2024/9/5
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
import io.github.yamin8000.owl.search.data.datasource.local.entity.DefinitionEntity
import io.github.yamin8000.owl.search.domain.model.Definition
import io.github.yamin8000.owl.search.domain.repository.local.DefinitionRepository

class DefinitionRoomRepository(
    private val definitionDao: DAOs.DefinitionDao,
    private val antonymDao: DAOs.AntonymDao,
    private val synonymDao: DAOs.SynonymDao
) : DefinitionRepository, BaseRoomRepository<Definition, DefinitionEntity>(definitionDao) {

    override suspend fun findAllByMeaningId(meaningId: Long): List<Definition> {
        return definitionDao.where("meaningId", meaningId).mapNotNull { mapToDomain(it) }
    }

    override suspend fun mapToDomain(item: DefinitionEntity?): Definition? {
        return if (item != null) {
            Definition(
                definition = item.definition,
                example = item.example,
                antonyms = antonymDao.where("definitionId", item.id).map { it.value },
                synonyms = synonymDao.where("definitionId", item.id).map { it.value },
                meaningId = item.meaningId,
                id = item.id
            )
        } else null
    }

    override suspend fun findEntity(item: Definition): DefinitionEntity? {
        return if (item.id != null) definitionDao.find(item.id)
        else return null
    }

    override suspend fun add(item: Definition): Long {
        return if (item.meaningId != null) {
            definitionDao.insert(
                DefinitionEntity(
                    definition = item.definition,
                    example = item.example,
                    createdAt = epoch(),
                    meaningId = item.meaningId
                )
            )
        } else -1
    }
}
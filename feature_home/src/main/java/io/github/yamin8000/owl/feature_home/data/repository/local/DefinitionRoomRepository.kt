/*
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main
 *     DefinitionRoomRepository.kt Copyrighted by Yamin Siahmargooei at 2024/8/25
 *     DefinitionRoomRepository.kt Last modified at 2024/8/25
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

package io.github.yamin8000.owl.feature_home.data.repository.local

import io.github.yamin8000.owl.feature_home.data.datasource.local.dao.DAOs
import io.github.yamin8000.owl.feature_home.data.datasource.local.entity.DefinitionEntity
import io.github.yamin8000.owl.feature_home.domain.model.Definition
import io.github.yamin8000.owl.feature_home.domain.repository.local.DefinitionRepository

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
                synonyms = synonymDao.where("definitionId", item.id).map { it.value }
            )
        } else null
    }

    override suspend fun mapToEntity(item: Definition): DefinitionEntity? {
        return if (item.id != null) definitionDao.find(item.id)
        else return null
    }
}
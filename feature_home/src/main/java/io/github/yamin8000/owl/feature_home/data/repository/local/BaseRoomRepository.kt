/*
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main
 *     BaseRoomRepository.kt Copyrighted by Yamin Siahmargooei at 2024/8/30
 *     BaseRoomRepository.kt Last modified at 2024/8/30
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

import io.github.yamin8000.owl.feature_home.data.datasource.local.dao.AdvancedDao
import io.github.yamin8000.owl.feature_home.domain.repository.local.util.BaseRepository

abstract class BaseRoomRepository<T, E>(
    private val dao: AdvancedDao<E>
) : BaseRepository<T> {

    override suspend fun remove(item: T): Int {
        val entity = findEntity(item)
        return if (entity != null) dao.delete(entity) else -1
    }

    override suspend fun find(id: Long): T? {
        return mapToDomain(dao.find(id))
    }

    override suspend fun all(): List<T> {
        return dao.all().mapNotNull { mapToDomain(it) }
    }

    abstract suspend fun mapToDomain(item: E?): T?

    abstract suspend fun findEntity(item: T): E?
}
/*
 *     freeDictionaryApp/freeDictionaryApp.search.main
 *     TermRoomRepository.kt Copyrighted by Yamin Siahmargooei at 2024/9/5
 *     TermRoomRepository.kt Last modified at 2024/8/30
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

import io.github.yamin8000.owl.common.ui.util.StringUtils.sanitizeWord
import io.github.yamin8000.owl.common.util.DateTimeUtils.epoch
import io.github.yamin8000.owl.search.data.datasource.local.dao.DAOs
import io.github.yamin8000.owl.search.data.datasource.local.entity.TermEntity
import io.github.yamin8000.owl.search.domain.repository.local.TermRepository

class TermRoomRepository(
    private val dao: DAOs.TermDao
) : TermRepository, BaseRoomRepository<String, TermEntity>(dao) {

    override suspend fun mapToDomain(item: TermEntity?): String? {
        return item?.word
    }

    override suspend fun findEntity(item: String): TermEntity? {
        return dao.where("word", sanitizeWord(item)).firstOrNull()
    }

    override suspend fun add(item: String): Long {
        return if (dao.where("word", sanitizeWord(item)).firstOrNull() == null) {
            dao.insert(
                TermEntity(
                    word = item,
                    createdAt = epoch()
                )
            )
        } else -1
    }
}
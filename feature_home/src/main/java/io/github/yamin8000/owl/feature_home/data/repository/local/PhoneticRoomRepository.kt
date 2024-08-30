/*
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main
 *     PhoneticRoomRepository.kt Copyrighted by Yamin Siahmargooei at 2024/8/25
 *     PhoneticRoomRepository.kt Last modified at 2024/8/25
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

import io.github.yamin8000.owl.common.util.DateTimeUtils.epoch
import io.github.yamin8000.owl.feature_home.data.datasource.local.dao.DAOs
import io.github.yamin8000.owl.feature_home.data.datasource.local.entity.PhoneticEntity
import io.github.yamin8000.owl.feature_home.domain.model.Phonetic
import io.github.yamin8000.owl.feature_home.domain.repository.local.PhoneticRepository

class PhoneticRoomRepository(
    private val phoneticDao: DAOs.PhoneticDao
) : PhoneticRepository, BaseRoomRepository<Phonetic, PhoneticEntity>(phoneticDao) {

    override suspend fun findAllByEntryId(entryId: Long): List<Phonetic> {
        return phoneticDao.where("entryId", entryId).mapNotNull { mapToDomain(it) }
    }

    override suspend fun mapToDomain(item: PhoneticEntity?): Phonetic? {
        return if (item != null) {
            Phonetic(
                text = item.value,
                entryId = item.entryId,
                id = item.id
            )
        } else null
    }

    override suspend fun findEntity(item: Phonetic): PhoneticEntity? {
        return if (item.id != null) phoneticDao.find(item.id) else null
    }

    override suspend fun add(item: Phonetic): Long {
        return if (item.entryId != null) {
            phoneticDao.insert(
                PhoneticEntity(
                    value = item.text,
                    entryId = item.entryId,
                    createdAt = epoch()
                )
            )
        } else -1
    }
}

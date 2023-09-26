/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     MeaningEntity.kt Copyrighted by Yamin Siahmargooei at 2023/9/26
 *     MeaningEntity.kt Last modified at 2023/9/26
 *     This file is part of freeDictionaryApp/freeDictionaryApp.app.main.
 *     Copyright (C) 2023  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.app.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.app.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey

import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = EntryEntity::class,
            parentColumns = ["id"],
            childColumns = ["entryId"]
        )
    ]
)
data class MeaningEntity(
    val entryId: Long,
    val partOfSpeech: String,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)

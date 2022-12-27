/*
 *     Owl/Owl.app.main
 *     DefinitionEntity.kt Copyrighted by Yamin Siahmargooei at 2022/12/27
 *     DefinitionEntity.kt Last modified at 2022/12/27
 *     This file is part of Owl/Owl.app.main.
 *     Copyright (C) 2022  Yamin Siahmargooei
 *
 *     Owl/Owl.app.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Owl/Owl.app.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Owl.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = WordEntity::class,
            parentColumns = ["id"],
            childColumns = ["wordId"],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class DefinitionEntity(
    @ColumnInfo(name = "wordId", index = true)
    val wordId: Long,
    val type: String?,
    val definition: String,
    val example: String?,
    val imageUrl: String?,
    val emoji: String?,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)

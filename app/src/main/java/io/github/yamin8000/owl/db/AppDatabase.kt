/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     AppDatabase.kt Copyrighted by Yamin Siahmargooei at 2023/8/26
 *     AppDatabase.kt Last modified at 2023/8/26
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

package io.github.yamin8000.owl.db

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.yamin8000.owl.db.dao.DAOs
import io.github.yamin8000.owl.db.entity.AntonymEntity
import io.github.yamin8000.owl.db.entity.DefinitionEntity
import io.github.yamin8000.owl.db.entity.EntryEntity
import io.github.yamin8000.owl.db.entity.MeaningEntity
import io.github.yamin8000.owl.db.entity.PhoneticEntity
import io.github.yamin8000.owl.db.entity.SynonymEntity

@Database(
    entities = [AntonymEntity::class, DefinitionEntity::class, EntryEntity::class, MeaningEntity::class, PhoneticEntity::class, SynonymEntity::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun antonymDao(): DAOs.AntonymDao
    abstract fun definitionDao(): DAOs.DefinitionDao
    abstract fun entryDao(): DAOs.EntryDao
    abstract fun meaningDao(): DAOs.MeaningDao
    abstract fun phoneticDao(): DAOs.PhoneticDao
    abstract fun synonymDao(): DAOs.SynonymDao
}
/*
 *     freeDictionaryApp/freeDictionaryApp.search.main
 *     AppDatabase.kt Copyrighted by Yamin Siahmargooei at 2024/9/5
 *     AppDatabase.kt Last modified at 2024/9/5
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

package io.github.yamin8000.owl.search.data.datasource.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import io.github.yamin8000.owl.search.data.datasource.local.dao.DAOs
import io.github.yamin8000.owl.search.data.datasource.local.entity.AntonymEntity
import io.github.yamin8000.owl.search.data.datasource.local.entity.DefinitionEntity
import io.github.yamin8000.owl.search.data.datasource.local.entity.EntryEntity
import io.github.yamin8000.owl.search.data.datasource.local.entity.MeaningEntity
import io.github.yamin8000.owl.search.data.datasource.local.entity.PhoneticEntity
import io.github.yamin8000.owl.search.data.datasource.local.entity.SynonymEntity
import io.github.yamin8000.owl.search.data.datasource.local.entity.TermEntity

@Database(
    version = 8,
    entities = [
        AntonymEntity::class, DefinitionEntity::class, EntryEntity::class,
        MeaningEntity::class, PhoneticEntity::class, SynonymEntity::class,
        TermEntity::class
    ],
    autoMigrations = [
        AutoMigration(from = 6, to = 7)
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun antonymDao(): DAOs.AntonymDao
    abstract fun definitionDao(): DAOs.DefinitionDao
    abstract fun entryDao(): DAOs.EntryDao
    abstract fun meaningDao(): DAOs.MeaningDao
    abstract fun phoneticDao(): DAOs.PhoneticDao
    abstract fun synonymDao(): DAOs.SynonymDao
    abstract fun termDao(): DAOs.TermDao
}

val MIGRATION_7_8 = object : Migration(7, 8) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(addDateColumn("AntonymEntity"))
        db.execSQL(addDateColumn("DefinitionEntity"))
        db.execSQL(addDateColumn("EntryEntity"))
        db.execSQL(addDateColumn("MeaningEntity"))
        db.execSQL(addDateColumn("PhoneticEntity"))
        db.execSQL(addDateColumn("SynonymEntity"))
        db.execSQL(addDateColumn("TermEntity"))
    }
}

private fun addDateColumn(
    tableName: String
): String = "ALTER TABLE `$tableName` ADD COLUMN createdAt INTEGER NOT NULL DEFAULT 0"
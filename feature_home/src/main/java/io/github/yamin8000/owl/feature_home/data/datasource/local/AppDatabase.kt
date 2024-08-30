/*
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main
 *     AppDatabase.kt Copyrighted by Yamin Siahmargooei at 2024/8/18
 *     AppDatabase.kt Last modified at 2024/8/18
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

package io.github.yamin8000.owl.feature_home.data.datasource.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import io.github.yamin8000.owl.common.util.DateTimeUtils
import io.github.yamin8000.owl.feature_home.data.datasource.local.dao.DAOs
import io.github.yamin8000.owl.feature_home.data.datasource.local.entity.AntonymEntity
import io.github.yamin8000.owl.feature_home.data.datasource.local.entity.DefinitionEntity
import io.github.yamin8000.owl.feature_home.data.datasource.local.entity.EntryEntity
import io.github.yamin8000.owl.feature_home.data.datasource.local.entity.MeaningEntity
import io.github.yamin8000.owl.feature_home.data.datasource.local.entity.PhoneticEntity
import io.github.yamin8000.owl.feature_home.data.datasource.local.entity.SynonymEntity
import io.github.yamin8000.owl.feature_home.data.datasource.local.entity.TermEntity

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
        val now = DateTimeUtils.epoch()
        db.execSQL(addDateColumn("AntonymEntity", now))
        db.execSQL(addDateColumn("DefinitionEntity", now))
        db.execSQL(addDateColumn("EntryEntity", now))
        db.execSQL(addDateColumn("MeaningEntity", now))
        db.execSQL(addDateColumn("PhoneticEntity", now))
        db.execSQL(addDateColumn("SynonymEntity", now))
        db.execSQL(addDateColumn("TermEntity", now))
    }
}

private fun addDateColumn(
    tableName: String,
    epoch: Long
): String = "ALTER TABLE `$tableName` ADD COLUMN createdAt INTEGER NOT NULL DEFAULT 0"
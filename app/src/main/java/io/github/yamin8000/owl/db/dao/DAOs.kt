/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     DAOs.kt Copyrighted by Yamin Siahmargooei at 2023/8/26
 *     DAOs.kt Last modified at 2023/8/26
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

package io.github.yamin8000.owl.db.dao

import androidx.room.Dao
import io.github.yamin8000.owl.db.entity.AntonymEntity
import io.github.yamin8000.owl.db.entity.DefinitionEntity
import io.github.yamin8000.owl.db.entity.EntryEntity
import io.github.yamin8000.owl.db.entity.MeaningEntity
import io.github.yamin8000.owl.db.entity.PhoneticEntity
import io.github.yamin8000.owl.db.entity.SynonymEntity

object DAOs {
    @Dao
    abstract class AntonymDao : BaseDao<AntonymEntity>("AntonymEntity")

    @Dao
    abstract class DefinitionDao : BaseDao<DefinitionEntity>("DefinitionEntity")

    @Dao
    abstract class EntryDao : BaseDao<EntryEntity>("EntryEntity")

    @Dao
    abstract class MeaningDao : BaseDao<MeaningEntity>("MeaningEntity")

    @Dao
    abstract class PhoneticDao : BaseDao<PhoneticEntity>("PhoneticEntity")

    @Dao
    abstract class SynonymDao : BaseDao<SynonymEntity>("SynonymEntity")
}
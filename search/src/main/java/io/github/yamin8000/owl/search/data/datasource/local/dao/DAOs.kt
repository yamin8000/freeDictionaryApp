/*
 *     freeDictionaryApp/freeDictionaryApp.search.main
 *     DAOs.kt Copyrighted by Yamin Siahmargooei at 2024/9/5
 *     DAOs.kt Last modified at 2024/8/18
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

package io.github.yamin8000.owl.search.data.datasource.local.dao

import androidx.room.Dao
import io.github.yamin8000.owl.search.data.datasource.local.entity.AntonymEntity
import io.github.yamin8000.owl.search.data.datasource.local.entity.DefinitionEntity
import io.github.yamin8000.owl.search.data.datasource.local.entity.EntryEntity
import io.github.yamin8000.owl.search.data.datasource.local.entity.MeaningEntity
import io.github.yamin8000.owl.search.data.datasource.local.entity.PhoneticEntity
import io.github.yamin8000.owl.search.data.datasource.local.entity.SynonymEntity
import io.github.yamin8000.owl.search.data.datasource.local.entity.TermEntity

object DAOs {
    @Dao
    abstract class AntonymDao : AdvancedDao<AntonymEntity>("AntonymEntity")

    @Dao
    abstract class DefinitionDao : AdvancedDao<DefinitionEntity>("DefinitionEntity")

    @Dao
    abstract class EntryDao : AdvancedDao<EntryEntity>("EntryEntity")

    @Dao
    abstract class MeaningDao : AdvancedDao<MeaningEntity>("MeaningEntity")

    @Dao
    abstract class PhoneticDao : AdvancedDao<PhoneticEntity>("PhoneticEntity")

    @Dao
    abstract class SynonymDao : AdvancedDao<SynonymEntity>("SynonymEntity")

    @Dao
    abstract class TermDao : AdvancedDao<TermEntity>("TermEntity")
}
/*
 *     Owl/Owl.app.main
 *     DAOs.kt Copyrighted by Yamin Siahmargooei at 2022/12/27
 *     DAOs.kt Last modified at 2022/12/27
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

package io.github.yamin8000.owl.db.dao

import androidx.room.Dao
import io.github.yamin8000.owl.db.entity.DefinitionEntity
import io.github.yamin8000.owl.db.entity.WordEntity

object DAOs {
    @Dao
    abstract class DefinitionDao : BaseDao<DefinitionEntity>("DefinitionEntity")

    @Dao
    abstract class WordDao : BaseDao<WordEntity>("WordEntity")
}
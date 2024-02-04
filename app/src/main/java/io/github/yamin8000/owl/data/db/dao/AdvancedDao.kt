/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     BaseDao.kt Copyrighted by Yamin Siahmargooei at 2023/8/26
 *     BaseDao.kt Last modified at 2023/8/26
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

package io.github.yamin8000.owl.data.db.dao

import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery

/**
 * Advanced Dao
 */
abstract class AdvancedDao<T>(tableName: String) : BaseDao<T>(tableName) {

    @RawQuery
    protected abstract suspend fun find(query: SupportSQLiteQuery): T?

    suspend fun find(id: Long) = find(SimpleSQLiteQuery("$baseQuery where id = $id"))

    @RawQuery
    protected abstract suspend fun all(query: SupportSQLiteQuery): List<T>

    suspend fun all() = all(SimpleSQLiteQuery(baseQuery))

    @RawQuery
    protected abstract suspend fun findAll(query: SupportSQLiteQuery): List<T>

    suspend fun findAll(
        ids: List<Long>
    ): List<T> {
        val params = ids.joinToString(",")
        return findAll(SimpleSQLiteQuery("$baseWhereQuery id in ($params)"))
    }

    @RawQuery
    protected abstract suspend fun where(query: SupportSQLiteQuery): List<T>

    suspend fun <V> where(
        column: String,
        value: V,
        operator: String = "="
    ) = where(SimpleSQLiteQuery("$baseWhereQuery $column $operator '$value'"))

    @RawQuery
    protected abstract suspend fun whereIn(query: SupportSQLiteQuery): List<T>

    suspend fun whereIn(
        column: String,
        values: List<*>
    ): List<T> {
        val temp = values.joinToString(separator = ",", transform = { "'$it'" })
        val query = SimpleSQLiteQuery("$baseWhereQuery $column in ($temp)")
        return whereIn(query)
    }

    suspend fun getByParams(
        paramPair: Pair<String, *>,
        vararg paramPairs: Pair<String, *>
    ): List<T> {
        val params = listOf(paramPair, *paramPairs)
        val condition = buildString {
            params.forEachIndexed { index, pair ->
                append("${pair.first}='${pair.second}'")
                if (index != params.lastIndex) append(" and ")
            }
        }
        return where(SimpleSQLiteQuery("$baseWhereQuery $condition"))
    }
}
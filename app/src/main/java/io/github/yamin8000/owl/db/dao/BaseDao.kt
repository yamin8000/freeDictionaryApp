/*
 *     Owl/Owl.app.main
 *     BaseDao.kt Copyrighted by Yamin Siahmargooei at 2022/12/27
 *     BaseDao.kt Last modified at 2022/12/27
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

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery

abstract class BaseDao<T>(tableName: String) {

    private val baseQuery = "select * from `$tableName`"

    private val baseWhereQuery = "$baseQuery where"

    @Insert
    abstract suspend fun insert(entity: T): Long

    @Insert
    abstract suspend fun insertAll(entities: List<T>): List<Long>

    @Update
    abstract suspend fun update(entity: T): Int

    @Delete
    abstract suspend fun delete(entity: T): Int

    @Delete
    abstract suspend fun deleteAll(entities: List<T>): Int

    @RawQuery
    protected abstract suspend fun getById(query: SupportSQLiteQuery): T?

    suspend fun getById(id: Long) = getById(SimpleSQLiteQuery("$baseQuery where id = $id"))

    @RawQuery
    protected abstract suspend fun getAll(query: SupportSQLiteQuery): List<T>

    suspend fun getAll() = getAll(SimpleSQLiteQuery(baseQuery))

    @RawQuery
    protected abstract suspend fun getAllByIds(query: SupportSQLiteQuery): List<T>

    suspend fun getAllByIds(
        ids: List<Long>
    ): List<T> {
        val params = ids.joinToString("")
        return getAllByIds(SimpleSQLiteQuery("$baseWhereQuery id in ($params)"))
    }

    @RawQuery
    protected abstract suspend fun getByParam(query: SupportSQLiteQuery): List<T>

    suspend fun <P> getByParam(
        param: String, value: P
    ) = getByParam(SimpleSQLiteQuery("$baseWhereQuery $param='$value'"))

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
        return getByParam(SimpleSQLiteQuery("$baseWhereQuery $condition"))
    }
}
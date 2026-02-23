/*
 *     freeDictionaryApp/freeDictionaryApp.common.main
 *     StringUtils.kt Copyrighted by Yamin Siahmargooei at 2024/8/18
 *     StringUtils.kt Last modified at 2024/8/18
 *     This file is part of freeDictionaryApp/freeDictionaryApp.common.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.common.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.common.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.common.util

object StringUtils {
    /**
     * This method sanitize words from the given [data] set by removing
     * unnecessary characters like white spaces and numbers, etc.
     * and making them lowercase and filtering out blank entries
     */
    fun sanitizeWords(
        data: Set<String>
    ): Set<String> = data.asSequence()
        .map { sanitizeWord(it) }
        .filter { it.isNotBlank() }
        .toSet()

    fun sanitizeWord(
        data: String
    ): String = data.replace(Regex("\\W+"), "")
        .lowercase()
        .trim()
}
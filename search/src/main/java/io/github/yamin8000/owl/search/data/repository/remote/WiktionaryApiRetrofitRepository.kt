/*
 *     freeDictionaryApp/freeDictionaryApp.search.main
 *     WiktionaryApiRetrofitRepository.kt Copyrighted by Yamin Siahmargooei at 2026/6/25
 *     WiktionaryApiRetrofitRepository.kt Last modified at 2026/6/25
 *     This file is part of freeDictionaryApp/freeDictionaryApp.search.main.
 *     Copyright (C) 2026  Yamin Siahmargooei
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

package io.github.yamin8000.owl.search.data.repository.remote

import io.github.yamin8000.owl.search.data.datasource.remote.wiktionary.WiktionaryAPI
import io.github.yamin8000.owl.search.domain.model.Entry
import io.github.yamin8000.owl.search.domain.repository.remote.WiktionaryApiRepository
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

class WiktionaryApiRetrofitRepository @Inject constructor(
    private val api: WiktionaryAPI
) : WiktionaryApiRepository {
    override suspend fun searchWord(word: String): List<Entry> {
        return api.search(word).values.map { meanings ->
            Entry(
                word = word,
                meanings = meanings.map { it.domain() }.toImmutableList()
            )
        }
    }
}
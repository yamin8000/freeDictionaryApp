/*
 *     freeDictionaryApp/freeDictionaryApp.search.main
 *     FreeDictionaryRetrofitApiRepository.kt Copyrighted by Yamin Siahmargooei at 2024/9/5
 *     FreeDictionaryRetrofitApiRepository.kt Last modified at 2024/9/5
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

package io.github.yamin8000.owl.search.data.repository.remote

import io.github.yamin8000.owl.search.data.datasource.remote.FreeDictionaryAPI
import io.github.yamin8000.owl.search.domain.model.Entry
import io.github.yamin8000.owl.search.domain.repository.remote.FreeDictionaryApiRepository
import javax.inject.Inject

class FreeDictionaryRetrofitApiRepository @Inject constructor(
    private val api: FreeDictionaryAPI
) : FreeDictionaryApiRepository {
    override suspend fun searchWord(word: String): List<Entry> {
        return api.search(word).map { it.domain() }
    }
}
/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     HomeViewModel.kt Copyrighted by Yamin Siahmargooei at 2024/1/25
 *     HomeViewModel.kt Last modified at 2024/1/25
 *     This file is part of freeDictionaryApp/freeDictionaryApp.app.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
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

package io.github.yamin8000.owl.ui.content.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.yamin8000.owl.data.DataStoreRepository
import io.github.yamin8000.owl.data.model.Entry
import io.github.yamin8000.owl.util.Constants
import io.github.yamin8000.owl.util.Constants.DEFAULT_LOCALE
import io.github.yamin8000.owl.util.Constants.FREE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val settings: DataStoreRepository
) : ViewModel() {
    private var _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private var _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private var _searchResult = MutableStateFlow(listOf<Entry>())
    val searchResult = _searchResult.asStateFlow()

    private var _entry = MutableStateFlow<Entry?>(null)
    val entry = _entry.asStateFlow()

    private var _searchSuggestions = MutableStateFlow(listOf<String>())
    val searchSuggestions = _searchSuggestions.asStateFlow()

    private var _isOnline = MutableStateFlow(false)
    val isOnline = _isOnline.asStateFlow()

    private var _isVibrating = MutableStateFlow(true)
    val isVibrating = _isVibrating.asStateFlow()

    private var _ttsLang = MutableStateFlow(DEFAULT_LOCALE)
    val ttsLang = _ttsLang.asStateFlow()

    init {
        viewModelScope.launch {
            _ttsLang.value = settings.getString(Constants.TTS_LANG) ?: DEFAULT_LOCALE
            _isVibrating.value = settings.getBool(Constants.IS_VIBRATING) ?: true
            val isBlank = settings.getBool(Constants.IS_STARTING_BLANK) ?: true
            if (!isBlank) {
                _searchText.value = FREE
                //searchForDefinition()
            }
        }
    }
}
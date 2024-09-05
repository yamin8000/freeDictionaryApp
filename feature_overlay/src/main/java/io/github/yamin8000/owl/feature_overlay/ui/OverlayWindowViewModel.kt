/*
 *     freeDictionaryApp/freeDictionaryApp.feature_overlay.main
 *     OverlayWindowViewModel.kt Copyrighted by Yamin Siahmargooei at 2024/9/5
 *     OverlayWindowViewModel.kt Last modified at 2024/9/5
 *     This file is part of freeDictionaryApp/freeDictionaryApp.feature_overlay.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_overlay.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_overlay.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.feature_overlay.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.yamin8000.owl.feature_overlay.di.OverlayViewModelFactory
import io.github.yamin8000.owl.search.domain.model.Phonetic
import io.github.yamin8000.owl.search.domain.usecase.SearchFreeDictionary
import io.github.yamin8000.owl.search.domain.usecase.WordCacheUseCases
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = OverlayViewModelFactory::class)
class OverlayWindowViewModel @AssistedInject constructor(
    @Assisted("intent") private val intentSearch: String?,
    private val searchFreeDictionaryUseCase: SearchFreeDictionary,
    private val cacheUseCases: WordCacheUseCases,
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        println(throwable.stackTraceToString())
    }

    private val scope = CoroutineScope(viewModelScope.coroutineContext + exceptionHandler)

    private var _state = MutableStateFlow(OverlayWindowState(searchTerm = intentSearch ?: ""))
    val state = _state.asStateFlow()

    init {
        val term = state.value.searchTerm
        if (term.isNotBlank()) {
            scope.launch {
                _state.update { it.copy(isSearching = true) }
                val cached = cacheUseCases.getCachedWord(term)
                if (cached != null) {
                    _state.update {
                        it.copy(
                            meanings = cached.meanings,
                            word = cached.word,
                            phonetic = getFirstPhonetic(cached.phonetics)
                        )
                    }
                } else {
                    val result = searchFreeDictionaryUseCase(term).firstOrNull()
                    if (result != null) {
                        _state.update {
                            it.copy(
                                meanings = result.meanings,
                                word = result.word,
                                phonetic = getFirstPhonetic(result.phonetics)
                            )
                        }
                    }
                }
                _state.update { it.copy(isSearching = false) }
            }
        }
    }

    private fun getFirstPhonetic(phonetics: List<Phonetic>): String {
        return phonetics.firstOrNull { it.text != null }?.text ?: ""
    }
}
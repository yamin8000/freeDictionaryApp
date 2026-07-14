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
import io.github.yamin8000.owl.common.util.TTS
import io.github.yamin8000.owl.common.util.log
import io.github.yamin8000.owl.feature_overlay.di.OverlayViewModelFactory
import io.github.yamin8000.owl.search.domain.usecase.cache.WordCacheUseCases
import io.github.yamin8000.owl.search.domain.usecase.search.SearchFreeDictionary
import io.github.yamin8000.owl.search.utils.MediaPlayerHelper
import kotlinx.collections.immutable.toImmutableList
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
    private val mediaPlayerHelper: MediaPlayerHelper,
    private val tts: TTS,
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        log(throwable.stackTraceToString())
        _state.update { it.copy(isSearching = false) }
    }

    private val scope = CoroutineScope(viewModelScope.coroutineContext + exceptionHandler)

    private var _state = MutableStateFlow(OverlayWindowState(searchTerm = intentSearch ?: ""))
    val state = _state.asStateFlow()

    init {
        val term = state.value.searchTerm
        if (term.isNotBlank()) {
            scope.launch {
                _state.update { it.copy(isSearching = true) }
                val cachedEntries = cacheUseCases.getCachedEntries(term)
                if (cachedEntries.isNotEmpty()) {
                    _state.update {
                        it.copy(
                            entries = cachedEntries.toImmutableList(),
                            word = cachedEntries.firstOrNull()?.word ?: "",
                        )
                    }
                } else {
                    val entries = searchFreeDictionaryUseCase(term)
                    if (entries.isNotEmpty()) {
                        _state.update {
                            it.copy(
                                entries = entries.toImmutableList(),
                                word = entries.firstOrNull()?.word ?: "",
                            )
                        }
                    }
                }
                _state.update { it.copy(isSearching = false) }
            }
        }
    }

    fun onAction(action: OverlayWindowAction) {
        when (action) {
            is OverlayWindowAction.OnPlayAudio -> mediaPlayerHelper.playFromUrl(action.audioUrl)
            is OverlayWindowAction.OnTextToSpeech -> tts.speak(action.phonetic)
        }
    }
}
/*
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main
 *     HomeViewModel.kt Copyrighted by Yamin Siahmargooei at 2024/8/17
 *     HomeViewModel.kt Last modified at 2024/6/2
 *     This file is part of freeDictionaryApp/freeDictionaryApp.feature_home.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.feature_home.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.yamin8000.owl.common.util.TTS
import io.github.yamin8000.owl.datastore.domain.usecase.favourites.FavouriteUseCases
import io.github.yamin8000.owl.datastore.domain.usecase.history.HistoryUseCases
import io.github.yamin8000.owl.datastore.domain.usecase.settings.SettingUseCases
import io.github.yamin8000.owl.feature_home.di.HomeViewModelFactory
import io.github.yamin8000.owl.search.domain.model.Entry
import io.github.yamin8000.owl.feature_home.domain.repository.TermSuggesterRepository
import io.github.yamin8000.owl.feature_home.domain.usecase.GetRandomWord
import io.github.yamin8000.owl.feature_home.ui.util.HomeSnackbarType
import io.github.yamin8000.owl.search.domain.usecase.SearchFreeDictionary
import io.github.yamin8000.owl.search.domain.usecase.WordCacheUseCases
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.UnknownHostException
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel(assistedFactory = HomeViewModelFactory::class)
class HomeViewModel @AssistedInject constructor(
    private val savedState: SavedStateHandle,
    private val searchFreeDictionaryUseCase: SearchFreeDictionary,
    private val termSuggesterRepository: TermSuggesterRepository,
    private val settingsUseCases: SettingUseCases,
    private val historyUseCases: HistoryUseCases,
    private val favouriteUseCases: FavouriteUseCases,
    private val cacheUseCases: WordCacheUseCases,
    private val randomWordUseCase: GetRandomWord,
    val tts: TTS,
    @Assisted("intent") private val intentSearch: String?,
    @Assisted("navigation") private val navigationSearch: String?
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _state.update { it.copy(isSearching = false) }
        println(throwable.stackTraceToString())
        viewModelScope.launch {
            when (throwable) {
                is HttpException -> when (throwable.code()) {
                    401 -> errorChannel.send(HomeSnackbarType.ApiAuthorizationError)
                    404 -> errorChannel.send(HomeSnackbarType.NotFound)
                    429 -> errorChannel.send(HomeSnackbarType.ApiThrottled)
                    else -> errorChannel.send(HomeSnackbarType.Unknown)
                }

                is CancellationException -> errorChannel.send(HomeSnackbarType.Cancelled)
                is UnknownHostException -> errorChannel.send(HomeSnackbarType.NoInternet)
                else -> errorChannel.send(HomeSnackbarType.Unknown)
            }
        }
    }

    private val scope = CoroutineScope(viewModelScope.coroutineContext + exceptionHandler)

    val searchTerm = savedState.getStateFlow("Search", intentSearch ?: navigationSearch ?: "")

    private var errorChannel = Channel<HomeSnackbarType>()
    val errorChannelFlow = errorChannel.receiveAsFlow()

    private var shareChannel = Channel<Entry?>()
    val shareChannelFlow = shareChannel.receiveAsFlow()

    private var _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private var searchJob: Job? = null

    val isWordSelectedFromKeyboardSuggestions: State<Boolean>
        get() = derivedStateOf { searchTerm.value.length > 1 && searchTerm.value.last() == ' ' && !searchTerm.value.all { it == ' ' } }

    private val internetCheckDelay = 5000L
    private val dnsServers = listOf(
        "8.8.8.8",
        "8.8.4.4",
        "1.1.1.1",
        "1.0.0.1",
        "185.51.200.2",
        "178.22.122.100",
        "10.202.10.202",
        "10.202.10.102"
    )

    init {
        scope.launch {
            _state.update { it.copy(isVibrating = settingsUseCases.getVibration()) }
            if (!settingsUseCases.getStartingBlank() && searchTerm.value.isBlank()) {
                savedState["Search"] = "free"
            }
            if (searchTerm.value.isNotBlank()) {
                searchForDefinition(searchTerm.value)
            }
            while (true) {
                onEvent(HomeEvent.OnCheckInternet)
                delay(internetCheckDelay)
            }
        }
    }

    fun onEvent(
        event: HomeEvent
    ) {
        when (event) {
            HomeEvent.RandomWord -> searchForRandomWord()
            HomeEvent.OnShareData -> scope.launch { shareChannel.send(state.value.searchResult) }
            HomeEvent.CancelSearch -> cancel()
            HomeEvent.UpdateTTS -> scope.launch { tts.createEngine(settingsUseCases.getTTS()) }

            is HomeEvent.NewSearch -> {
                val term = event.searchTerm ?: searchTerm.value
                savedState["Search"] = term
                searchJob = searchForDefinition(term)
            }

            is HomeEvent.OnTermChanged -> {
                savedState["Search"] = event.term
                scope.launch {
                    _state.update {
                        it.copy(searchSuggestions = termSuggesterRepository.suggestTerms(event.term))
                    }
                }
            }

            HomeEvent.OnCheckInternet -> {
                scope.launch {
                    _state.update { stateUpdate ->
                        stateUpdate.copy(isOnline = dnsServers.any { dnsAccessible(it) })
                    }
                }
            }

            is HomeEvent.OnAddToFavourite -> {
                scope.launch {
                    favouriteUseCases.addFavourite(event.word)
                    errorChannel.send(HomeSnackbarType.AddedToFavourite)
                }
            }

        }
    }

    private suspend fun dnsAccessible(
        dnsServer: String
    ) = withContext(Dispatchers.IO + exceptionHandler) {
        Runtime.getRuntime().exec("/system/bin/ping -c 1 $dnsServer").waitFor()
    } == 0

    private fun searchForRandomWord() = scope.launch {
        searchForDefinition(randomWordUseCase())
    }

    private fun searchForDefinition(
        searchTerm: String
    ) = scope.launch {
        if (searchTerm.isNotBlank()) {
            historyUseCases.addHistory(searchTerm)

            _state.update { it.copy(isSearching = true) }

            val cachedWord = cacheUseCases.getCachedWord(searchTerm)
            if (cachedWord == null) {
                val newWord = searchForDefinitionUsingApi(searchTerm)
                if (newWord != null) {
                    cacheUseCases.cacheWord(newWord)
                    cacheUseCases.cacheWordData(newWord)
                }
            } else loadCachedWord(cachedWord)

            _state.update { it.copy(isSearching = false) }
        } else errorChannel.send(HomeSnackbarType.TermIsEmpty)
    }

    private fun loadCachedWord(cachedWord: Entry) {
        val phonetic = cachedWord.phonetics.firstOrNull { it.text != null }?.text ?: ""
        _state.update {
            it.copy(
                searchResult = cachedWord,
                word = cachedWord.word,
                phonetic = phonetic,
                searchSuggestions = emptyList()
            )
        }
    }

    private suspend fun searchForDefinitionUsingApi(
        searchTerm: String
    ): Entry? {
        val entry = searchFreeDictionaryUseCase(searchTerm).firstOrNull()
        val phonetic = entry?.phonetics?.firstOrNull { it.text != null }?.text ?: ""
        _state.update {
            it.copy(
                searchResult = entry,
                word = entry?.word ?: "",
                phonetic = phonetic,
                searchSuggestions = emptyList()
            )
        }
        return entry
    }

    private fun cancel() {
        _state.update {
            it.copy(isSearching = false)
        }
        searchJob?.cancel()
    }
}
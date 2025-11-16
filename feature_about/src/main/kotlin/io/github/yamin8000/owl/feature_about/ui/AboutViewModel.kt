/*
 *     freeDictionaryApp/freeDictionaryApp.feature_about.main
 *     AboutViewModel.kt Copyrighted by Yamin Siahmargooei at 2025/11/16
 *     AboutViewModel.kt Last modified at 2025/11/16
 *     This file is part of freeDictionaryApp/freeDictionaryApp.feature_about.main.
 *     Copyright (C) 2025  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_about.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_about.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.feature_about.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.yamin8000.owl.common.util.log
import io.github.yamin8000.owl.common.util.randomColor
import io.github.yamin8000.owl.feature_about.domain.repository.GithubRepoRepository
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(
    private val repository: GithubRepoRepository
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _state.update { it.copy(isLoading = false) }
        log(throwable.stackTraceToString())
    }

    private val scope = CoroutineScope(
        SupervisorJob() + viewModelScope.coroutineContext + exceptionHandler
    )
    private val ioScope = CoroutineScope(SupervisorJob() + Dispatchers.IO + exceptionHandler)

    private var _state = MutableStateFlow(AboutState())
    val state = _state.asStateFlow()

    init {
        load()
    }

    private fun load() {
        scope.launch {
            _state.update { it.copy(isLoading = true) }
            val repo = withContext(ioScope.coroutineContext) {
                repository.getRepository("yamin8000", "freeDictionaryApp")
            }
            val releases = withContext(ioScope.coroutineContext) {
                repository.getReleases("yamin8000", "freeDictionaryApp")
            }
            val contributors = withContext(ioScope.coroutineContext) {
                repository.getRepositoryContributors("yamin8000", "freeDictionaryApp")
            }
            _state.update {
                it.copy(
                    isLoading = false,
                    repository = repo,
                    latestVersionName = releases.firstOrNull()?.name ?: "-",
                    contributors = contributors.toImmutableList().map { contributor ->
                        UiContributor(
                            contributor = contributor,
                            borderColor = randomColor()
                        )
                    }.toImmutableList()
                )
            }
        }
    }

    fun onAction(action: AboutAction) {
        when (action) {
            AboutAction.OnRefresh -> load()
            is AboutAction.OnTabChanged -> _state.update { it.copy(tab = action.tab) }
        }
    }
}
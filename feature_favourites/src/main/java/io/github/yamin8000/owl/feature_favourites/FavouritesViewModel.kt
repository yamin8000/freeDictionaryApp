/*
 *     freeDictionaryApp/freeDictionaryApp.feature_favourites.main
 *     FavouritesViewModel.kt Copyrighted by Yamin Siahmargooei at 2024/8/25
 *     FavouritesViewModel.kt Last modified at 2024/8/25
 *     This file is part of freeDictionaryApp/freeDictionaryApp.feature_favourites.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_favourites.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_favourites.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.feature_favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.yamin8000.owl.datastore.domain.usecase.favourites.FavouriteUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val useCases: FavouriteUseCases
) : ViewModel() {
    private val scope = viewModelScope

    private var _state = MutableStateFlow(FavouriteState())
    val state = _state.asStateFlow()

    init {
        scope.launch {
            useCases.getAllFavourite().collect { favourites ->
                _state.update { it.copy(favourites = favourites) }
            }
        }
    }

    fun onEvent(event: FavouriteEvent) {
        when (event) {
            is FavouriteEvent.Remove -> scope.launch { useCases.removeFavourite(event.favourite) }
            FavouriteEvent.RemoveAll -> scope.launch { useCases.removeAllFavourite() }
        }
    }
}
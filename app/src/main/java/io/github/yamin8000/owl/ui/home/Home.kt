/*
 *     Owl: an android app for Owlbot Dictionary API
 *     Home.kt Created by Yamin Siahmargooei at 2022/8/22
 *     This file is part of Owl.
 *     Copyright (C) 2022  Yamin Siahmargooei
 *
 *     Owl is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Owl is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Owl.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.ui.home

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.ui.MainBottomBar
import io.github.yamin8000.owl.ui.MainTopBar
import io.github.yamin8000.owl.ui.composable.DefinitionCard
import io.github.yamin8000.owl.ui.composable.PersianText
import io.github.yamin8000.owl.ui.composable.WordCard
import io.github.yamin8000.owl.ui.util.navigation.Nav
import io.github.yamin8000.owl.ui.util.theme.OwlTheme
import io.github.yamin8000.owl.util.LockScreenOrientation
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun HomeContent(
    navController: NavHostController? = null,
    searchTerm: String? = null,
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    OwlTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            val homeState = rememberHomeState()

            if (searchTerm != null)
                homeState.searchText = searchTerm
            LaunchedEffect(Unit) {
                if (homeState.isFirstTimeOpening)
                    homeState.searchForRandomWord()
                if (homeState.searchText.isNotBlank())
                    homeState.searchForDefinition()
            }

            Scaffold(
                topBar = {
                    MainTopBar(
                        onHistoryClick = {

                        },
                        onFavouritesClick = { navController?.navigate(Nav.Routes.favourites) },
                        onRandomWordClick = {
                            homeState.lifecycleOwner.lifecycleScope.launch {
                                homeState.searchForRandomWord()
                            }
                        },
                        onSettingsClick = {

                        },
                        onInfoClick = { navController?.navigate(Nav.Routes.about) }
                    )
                },
                floatingActionButton = {
                    AnimatedVisibility(
                        visible = homeState.floatingActionButtonVisibility,
                        enter = slideInHorizontally { it * 2 },
                        exit = slideOutHorizontally { it * 2 }
                    ) {
                        FloatingActionButton(onClick = {
                            homeState.lifecycleOwner.lifecycleScope.launch {
                                homeState.searchForDefinitionHandler()
                            }
                        }) { Icon(Icons.Filled.Search, stringResource(id = R.string.search)) }
                    }
                },
                bottomBar = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (homeState.isShowingError)
                            PersianText(
                                homeState.errorMessage.value,
                                color = MaterialTheme.colorScheme.error
                            )
                        if (homeState.isSearching.value)
                            CircularProgressIndicator()
                        MainBottomBar(
                            onSearchTermChanged = { homeState.searchText = it },
                            onSearch = {
                                homeState.searchText = it
                                homeState.lifecycleOwner.lifecycleScope.launch {
                                    homeState.searchForDefinitionHandler()
                                }
                            }
                        )
                    }
                }) { contentPadding ->
                Column(
                    modifier = Modifier.padding(contentPadding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    WordCard(homeState)
                    WordDefinitionsList(homeState)
                }
            }
        }
    }
}

@Composable
private fun WordDefinitionsList(
    homeState: HomeState
) {
    LazyColumn(
        modifier = Modifier.padding(16.dp),
        state = homeState.listState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
            items(homeState.searchResult.value) { definition ->
                DefinitionCard(definition)
            }
        })
}

@Composable
private fun WordCard(
    homeState: HomeState
) {
    val addedToFavourites = stringResource(id = R.string.added_to_favourites)
    homeState.rawWordSearchBody.value?.let { word ->
        WordCard(
            word,
            onAddToFavouriteClick = {
                homeState.lifecycleOwner.lifecycleScope.launch { homeState.addToFavourite(word.word) }
                Toast.makeText(homeState.context, addedToFavourites, Toast.LENGTH_SHORT).show()
            }
        )
    }
}
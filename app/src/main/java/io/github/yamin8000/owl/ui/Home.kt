/*
 *     Owl: an android app for Owlbot Dictionary API
 *     Home.kt Created by Yamin Siahmargooei at 2022/8/21
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

package io.github.yamin8000.owl.ui

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.model.Definition
import io.github.yamin8000.owl.model.Word
import io.github.yamin8000.owl.network.createRandomWordRequest
import io.github.yamin8000.owl.network.createSearchWordRequest
import io.github.yamin8000.owl.ui.composable.DefinitionCard
import io.github.yamin8000.owl.ui.composable.WordCard
import io.github.yamin8000.owl.ui.util.navigation.NavigationConstants
import io.github.yamin8000.owl.ui.util.theme.OwlTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun HomeContent(
    navController: NavHostController? = null,
) {
    OwlTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            val lifecycleOwner = LocalLifecycleOwner.current
            val focusManager = LocalFocusManager.current

            var searchText by rememberSaveable { mutableStateOf("") }
            var searchResult by rememberSaveable { mutableStateOf<List<Definition>>(emptyList()) }
            var rawSearchWordBody by remember { mutableStateOf<Word?>(null) }
            var isSearching by rememberSaveable { mutableStateOf(false) }
            val listState = rememberLazyListState()

            Scaffold(
                topBar = {
                    MainTopBar(
                        MainTopAppBarCallbacks(
                            onHistoryClick = {

                            },
                            onFavouritesClick = { navController?.navigate(NavigationConstants.NavRoutes.favourites) },
                            onRandomWordClick = {
                                isSearching = true
                                createRandomWordRequest(lifecycleOwner) {
                                    searchText = it
                                    createSearchWordRequest(
                                        lifecycleOwner,
                                        searchText,
                                        onSuccess = { word ->
                                            isSearching = false
                                            searchResult = word.definitions
                                            rawSearchWordBody = word
                                        }) { isSearching = false }
                                }
                            },
                            onSettingsClick = {

                            },
                            onInfoClick = { navController?.navigate(NavigationConstants.NavRoutes.about) }
                        )
                    )
                },
                floatingActionButton = {
                    AnimatedVisibility(
                        visible = !listState.isScrollInProgress,
                        enter = scaleIn(),
                        exit = scaleOut()
                    ) {
                        FloatingActionButton(onClick = {
                            isSearching = true
                            createSearchWordRequest(
                                lifecycleOwner,
                                searchText,
                                onSuccess = { word ->
                                    isSearching = false
                                    searchResult = word.definitions
                                    rawSearchWordBody = word
                                }) { isSearching = false }
                            focusManager.clearFocus()
                        }) { Icon(Icons.Filled.Search, stringResource(id = R.string.search)) }
                    }
                },
                bottomBar = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (isSearching)
                            CircularProgressIndicator()
                        MainBottomBar(
                            MainBottomBarCallbacks(
                                onSearch = {
                                    searchText = it
                                    isSearching = true
                                    createSearchWordRequest(
                                        lifecycleOwner,
                                        searchText,
                                        onSuccess = { word ->
                                            isSearching = false
                                            searchResult = word.definitions
                                            rawSearchWordBody = word
                                        }) { isSearching = false }
                                    focusManager.clearFocus()
                                },
                                onTextChanged = {
                                    searchText = it
                                }
                            )
                        )
                    }
                }) { contentPadding ->

                Column(
                    modifier = Modifier
                        .padding(contentPadding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    rawSearchWordBody?.let { WordCard(it) }

                    searchResult = searchResult.sortedByDescending { it.imageUrl }

                    LazyColumn(
                        state = listState,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        content = {
                            items(searchResult) { definition ->
                                DefinitionCard(definition)
                            }
                        })
                }
            }
        }
    }
}
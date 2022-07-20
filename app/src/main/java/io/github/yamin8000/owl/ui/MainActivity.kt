/*
 *     Owl: an android app for Owlbot Dictionary API
 *     MainActivity.kt Created by Yamin Siahmargooei at 2022/6/16
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

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.orhanobut.logger.Logger
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.model.Definition
import io.github.yamin8000.owl.model.Word
import io.github.yamin8000.owl.network.APIs
import io.github.yamin8000.owl.network.Web
import io.github.yamin8000.owl.network.Web.asyncResponse
import io.github.yamin8000.owl.ui.composable.AddDefinitionCard
import io.github.yamin8000.owl.ui.composable.ButtonWithIcon
import io.github.yamin8000.owl.ui.theme.OwlTheme
import retrofit2.Response

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MainContent() }
    }

    @Preview(showBackground = true)
    @Composable
    private fun MainContent() {
        OwlTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background,
            ) {

                val focusManager = LocalFocusManager.current
                var searchText by remember { mutableStateOf("") }
                var searchResult by remember { mutableStateOf<List<Definition>>(emptyList()) }
                var isSearching by remember { mutableStateOf(false) }

                Scaffold(
                    topBar = { MainTopBar() },
                    floatingActionButton = {
                        FloatingActionButton(onClick = {
                            isSearching = true
                            createSearchWordRequest(searchText) { response ->
                                isSearching = false
                                val body = response.body()
                                if (body != null) searchResult = body.definitions
                                else handleNullResponseBody(response.code())
                            }
                            focusManager.clearFocus()
                        }) { Icon(Icons.Filled.Search, stringResource(id = R.string.search)) }
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
                                        createSearchWordRequest(searchText) { response ->
                                            isSearching = false
                                            val body = response.body()
                                            if (body != null) searchResult = body.definitions
                                            else handleNullResponseBody(response.code())
                                        }
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
                        ButtonWithIcon(
                            onClick = {

                            },
                            iconPainter = painterResource(id = R.drawable.ic_history),
                            contentDescription = stringResource(id = R.string.search_history),
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally)
                        )

                        ButtonWithIcon(
                            onClick = {

                            },
                            iconPainter = painterResource(id = R.drawable.ic_favorites),
                            contentDescription = stringResource(id = R.string.favourites),
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally)
                        )

                        ButtonWithIcon(
                            onClick = {

                            },
                            iconPainter = painterResource(id = R.drawable.ic_casino),
                            contentDescription = stringResource(id = R.string.random_word),
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally)
                        )

                        LazyColumn(
                            content = {
                                items(searchResult) { item ->
                                    AddDefinitionCard(item)
                                }
                            },
                            verticalArrangement = Arrangement.spacedBy(2.dp),
                        )
                    }
                }
            }
        }
    }

    private fun createSearchWordRequest(input: String, callback: (Response<Word>) -> Unit) {
        Web.getAPI<APIs.WordAPI>().searchWord(input.trim()).asyncResponse(this, {
            callback(it)
        }, { throwable -> handleException(throwable) })
    }

    private fun handleException(throwable: Throwable) {
        Logger.d(throwable.stackTraceToString())
        Toast.makeText(this, getString(R.string.general_net_error), Toast.LENGTH_LONG).show()
    }

    private fun handleNullResponseBody(code: Int) {
        val message = when (code) {
            401 -> getString(R.string.api_authorization_error)
            404 -> getString(R.string.definition_not_found)
            429 -> getString(R.string.api_throttled)
            else -> getString(R.string.general_net_error)
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
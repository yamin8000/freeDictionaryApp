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
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import io.github.yamin8000.owl.model.Word
import io.github.yamin8000.owl.network.APIs
import io.github.yamin8000.owl.network.Web
import io.github.yamin8000.owl.network.Web.asyncResponse
import io.github.yamin8000.owl.ui.composable.ButtonWithIcon
import io.github.yamin8000.owl.ui.composable.DefinitionCard
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

                Scaffold(
                    topBar = { MainTopBar() },
                    floatingActionButton = {
                        FloatingActionButton(onClick = {
                            createSearchWordRequest(searchText)
                            focusManager.clearFocus()
                        }) { Icon(Icons.Filled.Search, stringResource(id = R.string.search)) }
                    },
                    bottomBar = {
                        MainBottomBar(
                            MainBottomBarCallbacks(
                                onSearch = {
                                    searchText = it
                                    createSearchWordRequest(searchText)
                                    focusManager.clearFocus()
                                },
                                onTextChanged = {
                                    searchText = it
                                }
                            )
                        )
                    }) { contentPadding ->

                    Column(
                        modifier = Modifier
                            .padding(contentPadding)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
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
                                item {
                                    DefinitionCard(
                                        word = "test",
                                        imagePainter = painterResource(id = R.drawable.ic_casino),
                                        type = "noun",
                                        definition = "hello",
                                        example = "hello there",
                                        emoji = "duh"
                                    )
                                }
                            },
                            verticalArrangement = Arrangement.spacedBy(2.dp),
                        )
                    }
                }
            }
        }
    }

    private fun createSearchWordRequest(input: String) {
        Web.getAPI<APIs.WordAPI>().searchWord(input).asyncResponse(this, {
            handleReceivedResponse(it)
        }, { throwable -> handleException(throwable) })
    }

    private fun handleReceivedResponse(it: Response<Word>) {
        val body = it.body()
        if (body != null) handleOkResponseBody(body)
        else handleNullResponseBody(it.code())
    }

    private fun handleException(throwable: Throwable) {
        Logger.d(throwable.stackTraceToString())
        //toast(getString(R.string.general_net_error), Toast.LENGTH_LONG)
        //binding.searchProgress.gone()
    }

    private fun handleOkResponseBody(body: Word) {
        //hideKeyboard()
        //binding.basicInfoCard.visible()
        //binding.wordText.handleViewDataNullity(body.word)
        //binding.pronunciationText.handleViewDataNullity(body.pronunciation)

        //val adapter = DefinitionListAdapter(this::imageClickListener)
        //binding.recyclerView.adapter = adapter
        body.definitions.forEachIndexed { index, definition ->
            //adapter.addItem(definition)
            //adapter.notifyItemInserted(index)
        }
        //binding.searchProgress.gone()
    }

    private fun handleNullResponseBody(code: Int) {
        val message = when (code) {
            401 -> getString(R.string.api_authorization_error)
            404 -> getString(R.string.definition_not_found)
            429 -> getString(R.string.api_throttled)
            else -> getString(R.string.general_net_error)
        }
        //toast(message, Toast.LENGTH_LONG)
        //binding.searchProgress.gone()
    }
}
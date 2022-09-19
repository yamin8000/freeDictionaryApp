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

package io.github.yamin8000.owl.content.home

import android.content.pm.ActivityInfo
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import coil.compose.AsyncImage
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.content.MainBottomBar
import io.github.yamin8000.owl.content.MainTopBar
import io.github.yamin8000.owl.model.Definition
import io.github.yamin8000.owl.model.Word
import io.github.yamin8000.owl.ui.composable.*
import io.github.yamin8000.owl.util.LockScreenOrientation
import io.github.yamin8000.owl.util.TtsEngine
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    searchTerm: String?,
    onHistoryClick: () -> Unit,
    onFavouritesClick: () -> Unit,
    onInfoClick: () -> Unit
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
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

        if (homeState.searchResult.value.isNotEmpty() && homeState.rawWordSearchBody.value != null && homeState.isSharing.value)
            homeState.handleShareIntent()

        Scaffold(
            topBar = {
                MainTopBar(
                    onHistoryClick = onHistoryClick,
                    onFavouritesClick = onFavouritesClick,
                    onInfoClick = onInfoClick,
                    onRandomWordClick = {
                        homeState.lifecycleOwner.lifecycleScope.launch {
                            homeState.searchForRandomWord()
                        }
                    },
                    onSettingsClick = {
                        Toast.makeText(
                            homeState.context,
                            homeState.context.getString(R.string.very_soon),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
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
                val addedToFavourites = stringResource(id = R.string.added_to_favourites)
                homeState.rawWordSearchBody.value?.let { word ->
                    WordCard(
                        word,
                        onShareWord = { homeState.isSharing.value = true },
                        onAddToFavourite = {
                            homeState.lifecycleCoroutineScope.launch {
                                homeState.addToFavourite(word.word)
                            }
                            Toast.makeText(homeState.context, addedToFavourites, Toast.LENGTH_SHORT)
                                .show()
                        },
                        onClick = {}
                    )
                }

                WordDefinitionsList(homeState.listState, homeState.searchResult.value)
            }
        }
    }
}

@Composable
private fun WordDefinitionsList(
    listState: LazyListState,
    searchResult: List<Definition>
) {
    LazyColumn(
        modifier = Modifier.padding(16.dp),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
            items(searchResult) { definition ->
                DefinitionCard(definition)
            }
        })
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun WordCard(
    word: Word,
    onClick: () -> Unit,
    onAddToFavourite: () -> Unit,
    onShareWord: () -> Unit
) {
    OutlinedCard(
        shape = CutCornerShape(15.dp),
        modifier = Modifier
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
                onClick = onClick
            )
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TtsReadyComposable { ttsEngine ->
                    WordText(word.word, ttsEngine)
                }
                if (word.pronunciation != null)
                    PronunciationText(
                        word.pronunciation,
                        word.word
                    )
            }
            Row {
                ClickableIcon(
                    iconPainter = painterResource(id = R.drawable.ic_favorites),
                    contentDescription = stringResource(id = R.string.favourites)
                ) { onAddToFavourite() }
                ClickableIcon(
                    iconPainter = painterResource(id = R.drawable.ic_share),
                    contentDescription = stringResource(R.string.share)
                ) { onShareWord() }
            }
        }
    }
}

@Composable
fun WordText(
    word: String,
    ttsEngine: TtsEngine
) {
    SpeakableRippleTextWithIcon(
        word,
        painterResource(id = R.drawable.ic_form_text),
        ttsEngine
    )
}

@Composable
fun PronunciationText(
    pronunciation: String,
    word: String
) {
    TtsReadyComposable { ttsEngine ->
        CopyAbleRippleTextWithIcon(
            text = pronunciation,
            iconPainter = painterResource(id = R.drawable.ic_person_voice)
        ) {
            ttsEngine.speak(word)
        }
    }
}

@Composable
fun WordEmojiText(
    emoji: String,
    ttsEngine: TtsEngine
) {
    SpeakableRippleTextWithIcon(
        emoji,
        painterResource(id = R.drawable.ic_emoji_symbols),
        ttsEngine
    )
}

@Composable
fun WordExampleText(
    example: String,
    ttsEngine: TtsEngine
) {
    SpeakableRippleTextWithIcon(
        example,
        painterResource(id = R.drawable.ic_text_snippet),
        ttsEngine
    )
}

@Composable
fun WordDefinitionText(
    definition: String,
    ttsEngine: TtsEngine
) {
    SpeakableRippleTextWithIcon(
        definition,
        painterResource(id = R.drawable.ic_short_text),
        ttsEngine
    )
}

@Composable
fun WordTypeText(
    type: String,
    ttsEngine: TtsEngine
) {
    SpeakableRippleTextWithIcon(
        type,
        painterResource(id = R.drawable.ic_category),
        ttsEngine
    )
}

@Preview(showBackground = true)
@Composable
fun DefinitionCard(
    @PreviewParameter(DefinitionProvider::class)
    definition: Definition
) {
    Card(
        shape = CutCornerShape(15.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TtsReadyComposable { ttsEngine ->
                    if (definition.type != null)
                        WordTypeText(definition.type, ttsEngine)
                    WordDefinitionText(definition.definition, ttsEngine)
                    if (definition.example != null)
                        WordExampleText(definition.example, ttsEngine)
                    if (definition.emoji != null)
                        WordEmojiText(definition.emoji, ttsEngine)
                }
            }
            if (!definition.imageUrl.isNullOrBlank())
                AsyncImage(
                    model = definition.imageUrl,
                    contentDescription = definition.definition,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillWidth
                )
        }
    }
}
/*
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main
 *     Previews.kt Copyrighted by Yamin Siahmargooei at 2025/11/16
 *     Previews.kt Last modified at 2025/11/16
 *     This file is part of freeDictionaryApp/freeDictionaryApp.feature_home.main.
 *     Copyright (C) 2025  Yamin Siahmargooei
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

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme
import io.github.yamin8000.owl.search.domain.model.Definition
import io.github.yamin8000.owl.search.domain.model.Entry
import io.github.yamin8000.owl.search.domain.model.Meaning
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import net.datafaker.Faker
import kotlin.random.Random
import kotlin.random.nextInt

@Preview(showBackground = true)
@Composable
private fun HomeLoadSuccess() {
    val faker = Faker()
    val word = faker.backToTheFuture().character()
    PreviewTheme {
        HomeContent(
            state = HomeState(
                searchResult = Entry(
                    word = word,
                    phonetics = persistentListOf(),
                    meanings = buildList {
                        repeat(Random.nextInt(2..3)) {
                            add(
                                Meaning(
                                    partOfSpeech = "noun",
                                    definitions = buildList {
                                        repeat(Random.nextInt(1..4)) {
                                            add(
                                                Definition(
                                                    definition = faker.backToTheFuture().quote(),
                                                    example = faker.backToTheFuture().quote(),
                                                    synonyms = buildList {
                                                        repeat(Random.nextInt(0..3)) {
                                                            add(faker.backToTheFuture().character())
                                                        }
                                                    },
                                                    antonyms = persistentListOf(),
                                                )
                                            )
                                        }
                                    }.toImmutableList(),
                                    synonyms = persistentListOf(),
                                    antonyms = persistentListOf()
                                )
                            )
                        }
                    }.toImmutableList()
                ),
                isOnline = true,
                isSearching = false,
                searchSuggestions = buildList {
                    add(word)
                    repeat(Random.nextInt(1..3)) {
                        add(faker.backToTheFuture().character())
                    }
                }.toImmutableList(),
                word = word,
                phonetic = "/ɪɡˈzæmpəl/"
            ),
            term = word,
            isWordSelectedFromKeyboardSuggestions = Random.nextBoolean(),
            onAction = {},
            onNavigateToAbout = {},
            onNavigateToSettings = {},
            onNavigateToFavourites = {},
            onNavigateToHistory = {}
        )
    }
}
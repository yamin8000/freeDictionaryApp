/*
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main
 *     SearchList.kt Copyrighted by Yamin Siahmargooei at 2025/1/16
 *     SearchList.kt Last modified at 2025/1/16
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

package io.github.yamin8000.owl.feature_home.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import io.github.yamin8000.owl.common.ui.components.AppText
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.search.domain.model.Meaning
import io.github.yamin8000.owl.search.ui.components.MeaningCard
import io.github.yamin8000.owl.search.ui.components.WordCard
import io.github.yamin8000.owl.strings.R

@Composable
internal fun SearchList(
    modifier: Modifier = Modifier,
    isOnline: Boolean,
    word: String,
    phonetic: String,
    onAddToFavourite: () -> Unit,
    onShareWord: () -> Unit,
    onWordChipClick: (String) -> Unit,
    meanings: List<Meaning>
) {
    val context = LocalContext.current
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Sizes.Large),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(Sizes.Large),
        content = {
            item(key = isOnline) {
                AnimatedVisibility(
                    visible = !isOnline,
                    enter = slideInVertically() + fadeIn(),
                    exit = slideOutVertically() + fadeOut(),
                    content = {
                        val internetError = remember { context.getString(R.string.general_net_error) }
                        AppText(
                            modifier = Modifier.padding(Sizes.Medium),
                            color = MaterialTheme.colorScheme.error,
                            text = internetError
                        )
                    }
                )
            }
            item(key = word + phonetic) {
                WordCard(
                    word = word,
                    pronunciation = phonetic,
                    onShareWord = onShareWord,
                    onAddToFavourite = onAddToFavourite
                )
            }

            itemsIndexed(
                items = meanings,
                key = { index, item -> item.id ?: index },
                itemContent = { _, meaning ->
                    MeaningCard(
                        modifier = Modifier.animateItem(),
                        word = word,
                        meaning = meaning,
                        onWordChipClick = onWordChipClick
                    )
                }
            )
        }
    )
}
/*
 *     freeDictionaryApp/freeDictionaryApp.feature_about.main
 *     AboutContributors.kt Copyrighted by Yamin Siahmargooei at 2025/11/17
 *     AboutContributors.kt Last modified at 2025/11/17
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

package io.github.yamin8000.owl.feature_about.ui.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import io.github.yamin8000.owl.common.ui.components.AppText
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.feature_about.ui.UiContributor
import io.github.yamin8000.owl.feature_about.ui.components.ContributionsBar
import io.github.yamin8000.owl.feature_about.ui.components.ContributorItem
import io.github.yamin8000.owl.strings.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlin.random.Random
import kotlin.random.nextInt

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PreviewTheme {
        AboutContributors(
            contributors = buildList {
                repeat(Random.nextInt(2..5)) {
                    add(UiContributor.mock())
                }
            }.toImmutableList()
        )
    }
}

@Composable
internal fun AboutContributors(
    contributors: ImmutableList<UiContributor>,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        modifier = modifier,
        contentPadding = PaddingValues(Sizes.Small),
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(Sizes.Large, Alignment.CenterVertically),
        horizontalArrangement = Arrangement.spacedBy(Sizes.Medium, Alignment.CenterHorizontally),
        content = {
            item(
                span = { GridItemSpan(maxLineSpan) },
                content = {
                    AppText(
                        text = stringResource(R.string.contributions_graph),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
            item(
                span = { GridItemSpan(maxLineSpan) },
                content = {
                    if (contributors.isNotEmpty()) {
                        ContributionsBar(contributors = contributors)
                    }
                }
            )
            items(
                items = contributors,
                key = { it.contributor.id },
                itemContent = {
                    ContributorItem(
                        avatarUrl = it.contributor.avatarUrl,
                        username = it.contributor.username,
                        borderColor = it.borderColor,
                        profileUrl = it.contributor.profileUrl
                    )
                }
            )
        }
    )
}
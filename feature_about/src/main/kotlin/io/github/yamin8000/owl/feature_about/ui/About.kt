/*
 *     freeDictionaryApp/freeDictionaryApp.feature_about.main
 *     About.kt Copyrighted by Yamin Siahmargooei at 2025/11/16
 *     About.kt Last modified at 2025/11/16
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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.yamin8000.owl.common.ui.components.AppText
import io.github.yamin8000.owl.common.ui.components.ScaffoldWithTitle
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.feature_about.ui.tabs.AboutContributors
import io.github.yamin8000.owl.feature_about.ui.tabs.AboutInfo
import io.github.yamin8000.owl.feature_about.ui.tabs.AboutLicense
import io.github.yamin8000.owl.strings.R
import kotlinx.collections.immutable.toImmutableList
import kotlin.random.Random
import kotlin.random.nextInt

@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun Preview() {
    PreviewTheme {
        var tab by remember { mutableStateOf(AboutTab.entries.random()) }
        AboutContent(
            installedVersionName = "1.0.0",
            state = AboutState(
                tab = tab,
                contributors = buildList {
                    repeat(Random.nextInt(2..5)) {
                        add(UiContributor.mock())
                    }
                }.toImmutableList()
            ),
            onAction = {
                when (it) {
                    AboutAction.OnRefresh -> {}
                    is AboutAction.OnTabChanged -> tab = it.tab
                }
            },
            onBackClick = {}
        )
    }
}

@Composable
fun AboutScreen(
    onBackClick: () -> Unit,
    installedVersionName: String,
    modifier: Modifier = Modifier,
    vm: AboutViewModel = hiltViewModel()
) {
    val state = vm.state.collectAsStateWithLifecycle().value

    PullToRefreshBox(
        isRefreshing = state.isLoading,
        onRefresh = { vm.onAction(AboutAction.OnRefresh) },
        content = {
            AboutContent(
                onBackClick = onBackClick,
                modifier = modifier,
                installedVersionName = installedVersionName,
                state = state,
                onAction = { action -> vm.onAction(action) },
            )
        }
    )
}

@Composable
internal fun AboutContent(
    onBackClick: () -> Unit,
    installedVersionName: String,
    state: AboutState,
    onAction: (AboutAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    ScaffoldWithTitle(
        modifier = modifier,
        title = stringResource(R.string.about),
        onBackClick = onBackClick,
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(Sizes.Small, Alignment.Top),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    PrimaryTabRow(
                        selectedTabIndex = state.tab.index,
                        divider = {},
                        tabs = {
                            AboutTab.entries.forEach { tab ->
                                Tab(
                                    modifier = Modifier.fillMaxWidth(),
                                    selected = tab == state.tab,
                                    onClick = { onAction(AboutAction.OnTabChanged(tab)) },
                                    text = {
                                        AppText(
                                            text = tab.name,
                                            modifier = Modifier.fillMaxWidth(),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                )
                            }
                        }
                    )
                    when (state.tab) {
                        AboutTab.Info -> {
                            AboutInfo(
                                installedVersionName = installedVersionName,
                                latestVersionName = state.latestVersionName,
                                description = state.repository?.description ?: ""
                            )
                        }

                        AboutTab.License -> AboutLicense()
                        AboutTab.Contributors -> AboutContributors(contributors = state.contributors)
                    }
                }
            )
        }
    )
}
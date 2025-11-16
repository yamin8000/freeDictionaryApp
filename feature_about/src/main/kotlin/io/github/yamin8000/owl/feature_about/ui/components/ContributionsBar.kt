/*
 *     freeDictionaryApp/freeDictionaryApp.feature_about.main
 *     ContributionsBar.kt Copyrighted by Yamin Siahmargooei at 2025/11/17
 *     ContributionsBar.kt Last modified at 2025/11/17
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

package io.github.yamin8000.owl.feature_about.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.feature_about.ui.UiContributor
import kotlin.random.Random
import kotlin.random.nextInt

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PreviewTheme {
        Box(
            modifier = Modifier.fillMaxWidth(),
            content = {
                ContributionsBar(
                    modifier = Modifier.padding(Sizes.Medium),
                    contributors = buildList {
                        repeat(Random.nextInt(2..5)) {
                            add(UiContributor.mock())
                        }
                    }
                )
            }
        )
    }
}

@Composable
internal fun ContributionsBar(
    contributors: List<UiContributor>,
    modifier: Modifier = Modifier,
    thickness: Dp = Sizes.Small
) {
    val sorted = remember(contributors) {
        contributors.sortedByDescending { it.contributor.contributions }
    }
    val colors = remember(contributors) {
        sorted.map { it.borderColor }
    }
    BoxWithConstraints(
        modifier = modifier,
        content = {
            Canvas(
                modifier = Modifier,
                onDraw = {
                    val total = sorted.sumOf { it.contributor.contributions }.toFloat()
                    var portions = 0f
                    drawLine(
                        color = colors.first(),
                        strokeWidth = thickness.toPx(),
                        start = Offset(0f, 0f),
                        end = Offset(0f, 0f),
                        cap = StrokeCap.Round
                    )
                    sorted.forEachIndexed { index, value ->
                        val portion = value.contributor.contributions / total

                        drawLine(
                            color = colors[index],
                            strokeWidth = thickness.toPx(),
                            start = Offset(portions * constraints.maxWidth, 0f),
                            end = Offset((portion + portions) * constraints.maxWidth, 0f),
                            cap = StrokeCap.Butt
                        )
                        portions += portion
                    }
                }
            )
        }
    )
}
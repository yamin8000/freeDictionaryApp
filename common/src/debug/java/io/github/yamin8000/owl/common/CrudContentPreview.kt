/*
 *     freeDictionaryApp/freeDictionaryApp.common.main
 *     CrudContentPreview.kt Copyrighted by Yamin Siahmargooei at 2025/10/26
 *     CrudContentPreview.kt Last modified at 2025/10/26
 *     This file is part of freeDictionaryApp/freeDictionaryApp.common.main.
 *     Copyright (C) 2025  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.common.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.common.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import io.github.yamin8000.owl.common.ui.components.crud.CrudContent
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme
import net.datafaker.Faker
import kotlin.random.Random
import kotlin.random.nextInt

@PreviewScreenSizes
@PreviewFontScale
@Preview
@Composable
private fun CrudContentPreview() {
    PreviewTheme {
        val faker = Faker()
        val items = buildList {
            repeat(Random.nextInt(0..100)) {
                val random = Random.nextInt(0..4)
                val word = when (random) {
                    0 -> faker.word().noun()
                    1 -> faker.word().verb()
                    2 -> faker.word().adverb()
                    3 -> faker.word().adjective()
                    4 -> faker.word().interjection()
                    else -> faker.word().preposition()
                }
                add(word)
            }
        }

        CrudContent(
            title = faker.lorem().word(),
            items = items,
            onBackClick = {},
            onRemoveAll = {},
            onRemoveSingle = {},
            onItemClick = {}
        )
    }
}
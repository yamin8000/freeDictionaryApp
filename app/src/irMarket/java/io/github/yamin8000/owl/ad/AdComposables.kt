/*
 *     Owl/Owl.app.main
 *     AdComposables.kt Copyrighted by Yamin Siahmargooei at 2023/4/22
 *     AdComposables.kt Last modified at 2023/4/22
 *     This file is part of Owl/Owl.app.main.
 *     Copyright (C) 2023  Yamin Siahmargooei
 *
 *     Owl/Owl.app.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Owl/Owl.app.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Owl.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.ad

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import io.github.yamin8000.owl.R

@Composable
fun TapsellAdContent(
    modifier: Modifier = Modifier,
    onCreated: (ViewGroup) -> Unit,
    onUpdate: (ViewGroup) -> Unit
) {
    val context = LocalContext.current
    Surface {
        AndroidView(
            modifier = modifier,
            update = onUpdate,
            factory = {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.standard_banner, null, false)
                    .findViewById<ViewGroup>(R.id.standardBanner)
                onCreated(view)
                view
            }
        )
    }
}
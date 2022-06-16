/*
 *     Owl: an android app for Owlbot Dictionary API
 *     Utility.kt Created by Yamin Siahmargooei at 2022/1/16
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

package io.github.yamin8000.owl.util

import android.content.ClipData
import android.content.ClipboardManager
import android.graphics.drawable.Drawable
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable

object Utility {

    /**
     * create a shimmer image
     *
     * @return shimmer drawable to load into imageview
     */
    fun getShimmer(): Drawable {
        val shimmer =
            Shimmer.AlphaHighlightBuilder() // The attributes for a ShimmerDrawable is set by this builder
                .setDuration(1800) // how long the shimmering animation takes to do one full sweep
                .setBaseAlpha(0.9f) //the alpha of the underlying children
                .setHighlightAlpha(0.8f) // the shimmer alpha amount
                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT).setAutoStart(true).build()

        // This is the placeholder for the imageView
        return ShimmerDrawable().apply {
            setShimmer(shimmer)
        }
    }

    fun copyToClipBoard(
        text: String,
        clipboardManager: ClipboardManager
    ) {
        val clip = ClipData.newPlainText(text, text)
        clipboardManager.setPrimaryClip(clip)
    }
}
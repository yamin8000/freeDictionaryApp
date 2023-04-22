/*
 *     Owl/Owl.app.main
 *     AdHelper.kt Copyrighted by Yamin Siahmargooei at 2023/4/22
 *     AdHelper.kt Last modified at 2023/4/22
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

import android.app.Activity
import android.view.ViewGroup
import io.github.yamin8000.owl.util.log
import ir.tapsell.plus.AdRequestCallback
import ir.tapsell.plus.AdShowListener
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.TapsellPlusBannerType
import ir.tapsell.plus.model.TapsellPlusAdModel
import ir.tapsell.plus.model.TapsellPlusErrorModel
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object AdHelper {

    suspend fun showTapsellAd(
        activity: Activity,
        adId: String,
        adView: ViewGroup?
    ) = suspendCoroutine<Unit> {
        TapsellPlus.showStandardBannerAd(
            activity,
            adId,
            adView,
            object : AdShowListener() {
                override fun onOpened(tapsellPlusAdModel: TapsellPlusAdModel) {
                    super.onOpened(tapsellPlusAdModel)
                    log(tapsellPlusAdModel.responseId)
                }

                override fun onError(tapsellPlusErrorModel: TapsellPlusErrorModel) {
                    super.onError(tapsellPlusErrorModel)
                    log(tapsellPlusErrorModel.errorMessage)
                }
            })
    }

    suspend fun requestTapsellAd(
        activity: Activity
    ) = suspendCancellableCoroutine { cancellableContinuation ->
        TapsellPlus.requestStandardBannerAd(
            activity,
            AdConstants.STANDARD_BANNER_ZONE_ID,
            TapsellPlusBannerType.BANNER_320x50,
            object : AdRequestCallback() {
                override fun response(ad: TapsellPlusAdModel?) {
                    super.response(ad)
                    cancellableContinuation.resume(ad?.responseId ?: "")
                }

                override fun error(error: String?) {
                    super.error(error)
                    log(error ?: "Unknown Tapsell Ad Request error")
                }
            }
        )
    }
}
/*
 *     Owl: an android app for Owlbot Dictionary API
 *     RequestHelpers.kt Created by Yamin Siahmargooei at 2022/8/21
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

package io.github.yamin8000.owl.network

import androidx.lifecycle.LifecycleOwner
import com.orhanobut.logger.Logger
import io.github.yamin8000.owl.model.Word
import io.github.yamin8000.owl.network.Web.asyncResponse
import io.github.yamin8000.owl.network.Web.getAPI

fun createRandomWordRequest(
    owner: LifecycleOwner,
    onResponse: (String) -> Unit
) {
//    Web.ninjaApiRetrofit.getAPI<APIs.NinjaAPI>()
//        .getRandomWord()
//        .asyncResponse(owner, {
//            onResponse(it.body()?.word ?: "")
//        }, { handleException(it) })
}

fun createSearchWordRequest(
    owner: LifecycleOwner,
    input: String,
    onSuccess: (Word) -> Unit,
    onFailed: ((Throwable) -> Unit)? = null
) {
    /*Web.retrofit.getAPI<APIs.OwlBotWordAPI>().searchWord(input.trim()).asyncResponse(owner, {
        val body = it.body()
        if (body == null) {
            handleNullResponseBody(it.code())
            onFailed?.invoke(Exception("No Body"))
        } else onSuccess(body)
    }, { throwable ->
        handleException(throwable)
        onFailed?.invoke(throwable)
    })*/
}

private fun handleException(
    throwable: Throwable
) {
    Logger.d(throwable.stackTraceToString())
    //Toast.makeText(this, getString(R.string.general_net_error), Toast.LENGTH_LONG).show()
}

private fun handleNullResponseBody(
    code: Int
) {
    /*val message = when (code) {
        401 -> getString(R.string.api_authorization_error)
        404 -> getString(R.string.definition_not_found)
        429 -> getString(R.string.api_throttled)
        else -> getString(R.string.general_net_error)
    }
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()*/
}
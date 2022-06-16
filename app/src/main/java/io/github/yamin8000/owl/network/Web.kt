/*
 *     Owl: an android app for Owlbot Dictionary API
 *     Web.kt Created by Yamin Siahmargooei at 2022/1/16
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

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.orhanobut.logger.Logger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Web {

    private const val BaseUrl = "https://owlbot.info/api/v4/"

    val retrofit: Retrofit by lazy(LazyThreadSafetyMode.NONE) { createRetrofit() }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * get service of given api/interface
     *
     * @param T type/class/interface of api
     * @return service for that api
     */
    inline fun <reified T> getAPI(): T = retrofit.create(T::class.java)

    /**
     * async callback
     *
     * this callback is aware of context or lifecycle
     *
     * @param T result type
     * @param lifeCycleOwner lifecycle owner of this request
     * @param onSuccess callback when request is successful
     * @param onFail callback when request is failed
     */
    inline fun <reified T> Call<T>.async(
        lifeCycleOwner: LifecycleOwner, crossinline onSuccess: (T?) -> Unit,
        crossinline onFail: (Throwable) -> Unit,
    ) {
        lifeCycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) this@async.cancel()
            }
        })

        this.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val body = response.body()
                onSuccess(body)
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                if (isCanceled) Logger.d("${call.request().url} is canceled")
                onFail(t)
            }
        })
    }

    /**
     * async callback which pass Response class instead of only data
     *
     * this is useful when you want to know http headers or http codes
     *
     * this callback is aware of context or lifecycle
     *
     * @param T result type
     * @param lifeCycleOwner lifecycle owner of this request
     * @param onSuccess callback when request is successful
     * @param onFail callback when request is failed
     */
    fun <T> Call<T>.asyncResponse(
        lifeCycleOwner: LifecycleOwner, onSuccess: (Response<T>) -> Unit,
        onFail: (Throwable) -> Unit,
    ) {
        lifeCycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) this@asyncResponse.cancel()
            }
        })

        this.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) = onSuccess(response)

            override fun onFailure(call: Call<T>, t: Throwable) {
                if (isCanceled) Logger.d("${call.request().url} is canceled")
                onFail(t)
            }
        })
    }
}
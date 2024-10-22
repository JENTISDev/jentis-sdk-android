package com.jentis.sdk.jentissdk.data

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.jentis.sdk.jentissdk.data.service.ApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    fun create(context: Context, trackDomain: String): ApiService {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)

            .addInterceptor(ChuckerInterceptor.Builder(context).build())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(trackDomain) // "https://qc3ipx.ckion-dev.jtm-demo.com"
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }
}

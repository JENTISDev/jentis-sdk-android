package com.jentis.sdk.jentissdk.data.service

import com.jentis.sdk.jentissdk.data.service.model.Root
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("/")
    suspend fun sendRootData(@Body root: Root): Response<Void>
}

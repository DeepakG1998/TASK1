package com.blogspot.devofandroid.task1

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("/api/test")
    suspend fun createEmployee(@Body requestBody: RequestBody): Response<ResponseBody>

}
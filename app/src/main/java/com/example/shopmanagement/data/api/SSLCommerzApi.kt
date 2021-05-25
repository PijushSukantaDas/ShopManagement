package com.example.biiaas.model.api

import com.example.shopmanagement.data.apimodels.SSLCommerzResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface SSLCommerzApi {

    @FormUrlEncoded
    @POST("get-sslgateway")
    suspend fun getSSLCommerzInfo(@Field("token") token: String, @Field("gatewayType") gatewayType: Int): Response<SSLCommerzResponse>
}
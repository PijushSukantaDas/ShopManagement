package com.example.shopmanagement.data.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstanceKT {
        companion object{

            val interceptor= HttpLoggingInterceptor().apply {
                this.level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder().apply {
                this.addInterceptor(interceptor)
                this.retryOnConnectionFailure(true)
            }.build()


            fun getRetrofitInstance(BASE_URL:String): Retrofit {
                return Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                        .build()
            }
        }

}
package com.example.shopmanagement.data.apimodels

import com.google.gson.annotations.SerializedName

data class SSLCommerzResponse(@field:SerializedName("request_url") val requestUrl: String,
                              @field:SerializedName("store_id") val storeId: String,
                              @field:SerializedName("store_password") val storePassword: String,
                              @field:SerializedName("msg") val message: String,
                              @field:SerializedName("success") val success: Int
)
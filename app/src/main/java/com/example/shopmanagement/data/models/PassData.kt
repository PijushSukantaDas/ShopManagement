package com.example.shopmanagement.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PassData(
    @SerializedName("api_key")
    @Expose
    var api_key : String?,
    @SerializedName("type")
    @Expose
    var textType : String,
    @SerializedName("contacts")
    @Expose
    var contacts:String,
    @SerializedName("senderid")
    @Expose
    var senderID : String,
    @SerializedName("msg")
    @Expose
    var message : String
)
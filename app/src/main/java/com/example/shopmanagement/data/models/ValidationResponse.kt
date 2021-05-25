package com.example.shopmanagement.data.models

data class ValidationResponse(
    val license: License,
    val msg: String,
    val success: Boolean
)
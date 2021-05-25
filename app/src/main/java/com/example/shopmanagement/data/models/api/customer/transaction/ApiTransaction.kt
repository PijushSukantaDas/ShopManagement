package com.example.shopmanagement.data.models.api.customer.transaction

data class ApiTransaction(
    val `data`: Data,
    val msg: String,
    val success: Int
)
package com.example.shopmanagement.data.models.api.payment

data class ApiPayment(
    val data : PaymentResponse,
    val msg: String,
    val success: Int
)
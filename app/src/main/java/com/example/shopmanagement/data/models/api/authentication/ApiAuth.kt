package com.example.shopmanagement.data.models.api.authentication

data class ApiAuth(
    val email: String,
    val license: License,
    val message: String
)
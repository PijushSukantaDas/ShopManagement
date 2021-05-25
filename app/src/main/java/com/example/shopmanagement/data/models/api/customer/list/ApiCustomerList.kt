package com.example.shopmanagement.data.models.api.customer.list

data class ApiCustomerList(
    val `data`: List<Data>,
    val msg: String,
    val success: Int
)
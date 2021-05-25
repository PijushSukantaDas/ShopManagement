package com.example.shopmanagement.data.models.api.product.list

data class ApiProductList(
    val `data`: List<Data>,
    val msg: String,
    val success: Int
)
package com.example.shopmanagement.data.models.api.category

data class ApiShopCategory(
    val `data`: List<ShopCategory>,
    val msg: String,
    val success: Int
)
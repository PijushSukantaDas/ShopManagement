package com.example.shopmanagement.data.models.api.category

data class ShopCategory(
    val category_name: String,
    val created_at: String,
    val deleted_at: Any,
    val description: String,
    val id: Int,
    val parent_id: Int,
    val updated_at: String
)
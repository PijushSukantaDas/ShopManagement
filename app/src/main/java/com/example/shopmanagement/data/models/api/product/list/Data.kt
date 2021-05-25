package com.example.shopmanagement.data.models.api.product.list

data class Data(
    val alert_quantity: Int,
    val category_id: String,
    val created_at: String,
    val deleted_at: String,
    val description: String,
    val ena: String,
    val id: Int,
    val imagefile: String,
    val min_mrp: Any,
    val mrp: Int,
    val name: String,
    val price: Int,
    val status: Int,
    val sub_category_id: String,
    val unit: Double,
    val updated_at: String,
    val user_id: Int,
    val code : String
)
package com.example.shopmanagement.data.models.api.product

data class Data(
    val alert_quantity: Int,
    val category_id: Int,
    val created_at: String,
    val description: String,
    val ena: String,
    val id: Int,
    val min_mrp: Double,
    val mrp: Double,
    val name: String,
    val price: Double,
    val status: Int,
    val sub_category_id: Int,
    val updated_at: String,
    val user_id: Int,
    val unit : Int,
    val imagefile : String,
    val deleted_at : String

)
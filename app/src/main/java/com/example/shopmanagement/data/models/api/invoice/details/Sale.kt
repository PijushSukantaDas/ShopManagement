package com.example.shopmanagement.data.models.api.invoice.details

data class Sale(
    val code: Int,
    val created_at: String,
    val deleted_at: String,
    val dell_quantity: Int,
    val id: Int,
    val invoice_id: Int,
    val name: String,
    val product_id: Int,
    val total_amount: Int,
    val unit_price: Int,
    val updated_at: String,
    val user_id: Int
)
package com.example.shopmanagement.data.models.api.purchase

data class Data(
    val created_at: String,
    val dailyaccount_id: Int,
    val deleted_at: String,
    val expired_date: String,
    val id: Int,
    val manufacturing_date: String,
    val name: String,
    val payment_id: Int,
    val product_count: Int,
    val product_id: Int,
    val purchase_date: String,
    val purchase_invoice: Int,
    val purchase_price: Int,
    val qty: String,
    val quantity: Int,
    val sell_quantity: Int,
    val supplier_id: Int,
    val total: Double,
    val total_amount: Double,
    val updated_at: String,
    val user_id: Int
)
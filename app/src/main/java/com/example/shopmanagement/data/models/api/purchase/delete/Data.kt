package com.example.shopmanagement.data.models.api.purchase.delete

data class Data(
    val created_at: String,
    val dailyaccount_id: Any,
    val deleted_at: String,
    val expired_date: Any,
    val id: Int,
    val manufacturing_date: Any,
    val payment_id: Any,
    val product_id: Int,
    val purchase_date: String,
    val purchase_invoice: Int,
    val purchase_price: Int,
    val quantity: Int,
    val sell_quantity: Int,
    val supplier_id: Int,
    val total_amount: Int,
    val updated_at: String,
    val user_id: Int
)
package com.example.shopmanagement.data.models.api.purchase

data class PurchaseData(
    val created_at: String,
    val dailyaccount_id: Any,
    val deleted_at: Any,
    val expired_date: Any,
    val id: Int,
    val manufacturing_date: Any,
    val payment_id: Any,
    val product_count: Int,
    val product_id: Int,
    val purchase_date: String,
    val purchase_invoice: Int,
    val purchase_price: Int,
    val qty: String,
    val quantity: Int,
    val sell_quantity: Int,
    val supplier_id: Int,
    val supplier_name: String,
    val total: Int,
    val total_amount: Int,
    val updated_at: String,
    val user_id: Int
)
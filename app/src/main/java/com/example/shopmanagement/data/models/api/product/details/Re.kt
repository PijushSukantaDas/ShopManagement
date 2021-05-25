package com.example.shopmanagement.data.models.api.product.details

data class Re(
    val code: String,
    val customer_name: String,
    val supplier_name : String,
    val date: String,
    val id: Int,
    val invoice_id: Int,
    val price: Int,
    val product_id: Int,
    val quantity: Int,
    val status: String,
    val total_amount: Double
)
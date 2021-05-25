package com.example.shopmanagement.data.models.api.customer.transaction

data class Info(
    val address: String,
    val created_at: String,
    val deleted_at: String,
    val due_amount: Double,
    val email: String,
    val gender: Int,
    val id: Int,
    val mobile: String,
    val name: String,
    val nid: String,
    val opening_balance: Double,
    val receipt_type: Int,
    val status: Int,
    val updated_at: String,
    val user_id: Int,
    val vat_no: String

)
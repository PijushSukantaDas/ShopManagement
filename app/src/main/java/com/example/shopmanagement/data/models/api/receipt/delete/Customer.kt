package com.example.shopmanagement.data.models.api.receipt.delete

data class Customer(
    val address: String,
    val created_at: String,
    val deleted_at: Any,
    val due_amount: Any,
    val email: Any,
    val gender: Int,
    val id: Int,
    val mobile: String,
    val name: String,
    val nid: Any,
    val opening_balance: Int,
    val receipt_type: Int,
    val status: Int,
    val updated_at: String,
    val user_id: Int,
    val vat_no: Any
)
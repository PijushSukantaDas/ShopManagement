package com.example.shopmanagement.data.models.api.customer.list

data class Data(
    val address: String,
    val created_at: String,
    val deleted_at: String,
    val due_amount: String,
    val email: String,
    val gender: Int,
    val id: Int,
    val invoice_amount: Int,
    val mobile: String,
    val name: String,
    val nid: String,
    val opening_balance: Int,
    val paid_amount: Int,
    val receipt_type: Int,
    val rtv_amount: Int,
    val status: Int,
    val updated_at: String,
    val user_id: Int,
    val vat_no: String
)
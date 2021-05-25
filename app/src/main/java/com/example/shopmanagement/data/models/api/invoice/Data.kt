package com.example.shopmanagement.data.models.api.invoice

data class Data(
    val bill_debtor_status: Int,
    val bill_no: Int,
    val bill_payment_status: Int,
    val cash_register_id: Int,
    val created_at: String,
    val created_with: Int,
    val customer_id: Int,
    val date: String,
    val deleted_at: String,
    val due_amount: Double,
    val id: Int,
    val partial_payment: Double,
    val payment_transaction_status: Int,
    val receipt_id: Int,
    val receipt_status: Int,
    val tax_percentage: Double,
    val total_amount: Double,
    val total_product_amount: Double,
    val updated_at: String,
    val user_id: Int,
    val vat_amount: Double
)
package com.example.shopmanagement.data.models.api.invoice.delete

data class Data(
    val bill_debtor_status: Int,
    val bill_no: Int,
    val bill_payment_status: Int,
    val cash_register_id: Any,
    val created_at: String,
    val created_with: Int,
    val customer_id: Int,
    val date: String,
    val deleted_at: String,
    val due_amount: Int,
    val id: Int,
    val partial_payment: Int,
    val payment_transaction_status: Int,
    val receipt_id: Int,
    val receipt_status: Int,
    val tax_percentage: Int,
    val total_amount: Int,
    val total_product_amount: Int,
    val updated_at: String,
    val user_id: Int,
    val vat_amount: Int
)
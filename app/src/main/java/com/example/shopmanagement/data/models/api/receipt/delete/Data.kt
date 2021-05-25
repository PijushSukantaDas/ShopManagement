package com.example.shopmanagement.data.models.api.receipt.delete

data class Data(
    val amount: Int,
    val cash_register_id: Any,
    val cheque_date: String,
    val cheque_no: Any,
    val clearance_date: String,
    val created_at: String,
    val customer: Customer,
    val customer_id: Int,
    val dailyaccount_id: Int,
    val deleted_at: String,
    val details: String,
    val discount: Int,
    val id: Int,
    val invoice_amount: Int,
    val invoice_id: Any,
    val ledger_id: Any,
    val receive_date: String,
    val rtv_amount: Int,
    val rtv_id: Any,
    val total: Int,
    val type: Int,
    val updated_at: String,
    val user_id: Int,
    val vat_amount: Int
)
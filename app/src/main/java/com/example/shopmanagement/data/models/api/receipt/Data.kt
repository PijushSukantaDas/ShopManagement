package com.example.shopmanagement.data.models.api.receipt

data class Data(
    val amount: Double,
    val cash_register_id: Int,
    val cheque_date: String,
    val cheque_no: String,
    val clearance_date: String,
    val created_at: String,
    val customer_id: Int,
    val dailyaccount_id: Int,
    val deleted_at: String,
    val details: String,
    val discount: Double,
    val id: Int,
    val invoice_amount: Double,
    val invoice_id: String,
    val ledger_id: Int,
    val receive_date: String,
    val rtv_amount: Double,
    val rtv_id: String,
    val total : Double,
    val type: Int,
    val updated_at: String,
    val user_id: Int,
    val vat_amount: Double
)
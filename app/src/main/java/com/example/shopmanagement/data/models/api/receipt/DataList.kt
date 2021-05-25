package com.example.shopmanagement.data.models.api.receipt

data class DataList(
    val amount: Int,
    val bills: String,
    val cash_register_id: String,
    val cheque_date: String,
    val cheque_no: String,
    val clearance_date: String,
    val created_at: String,
    val customer_id: Int,
    val dailyaccount_id: Int,
    val deleted_at: String,
    val details: String,
    val discount: Int,
    val expenses: Int,
    val id: Int,
    val invoice_amount: Int,
    val invoice_id: String,
    val ledger_id: Int,
    val receive_date: String,
    val rtv_amount: Int,
    val rtv_id: Int,
    val customer_name : String,
    val rtvs: List<Any>,
    val total: Double,
    val type: Int,
    val updated_at: String,
    val user_id: Int,
    val vat_amount: Int

)
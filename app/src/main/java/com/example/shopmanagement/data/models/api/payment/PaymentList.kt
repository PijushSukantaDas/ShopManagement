package com.example.shopmanagement.data.models.api.payment

data class PaymentList(
    val amount: Int,
    val cash_register_id: Any,
    val cheque_date: Any,
    val cheque_no: Any,
    val created_at: String,
    val dailyaccount_id: Int,
    val deleted_at: Any,
    val details: String,
    val discount: Int,
    val id: Int,
    val ledger_id: Any,
    val payment_date: String,
    val supplier_id: Int,
    val supplier_name: String,
    val total: Int,
    val type: Int,
    val updated_at: String,
    val user_id: Int
)
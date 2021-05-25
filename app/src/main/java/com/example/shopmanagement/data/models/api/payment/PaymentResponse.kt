package com.example.shopmanagement.data.models.api.payment

import java.util.*

data class PaymentResponse(
    val amount: Double,
    val cash_register_id: Int,
    val cheque_date : String,
    val cheque_no : String,
    val created_at: String,
    val dailyaccount_id : Int,
    val deleted_at: String,
    val details: String,
    val discount : Double,
    val id : Int,
    val ledger_idd: Int,
    val payment_date : String,
    val supplier_id : Int,
    val total : Double,
    val type: Int,
    val updated_at: Date,
    val user_id : Int
)
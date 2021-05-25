package com.example.shopmanagement.data.models.api.customer.transaction

data class Re(
    val amount: Double,
    val bill_no: Int?,
    val cheque_no: String?,
    val date: String,
    val id: Int,
    val receipt_id: Int?,
    val status: String,
    var balance : Double
)
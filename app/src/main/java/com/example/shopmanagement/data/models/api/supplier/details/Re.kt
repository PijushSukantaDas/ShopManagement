package com.example.shopmanagement.data.models.api.supplier.details

data class Re(
    val amount: Int,
    val cheque_no: Any,
    val date: String,
    val discount: Int,
    val id: Int,
    val purchase_invoice: Int,
    val status: String,
    val total: Int,
    val type: Int,
    var balance : Int
)
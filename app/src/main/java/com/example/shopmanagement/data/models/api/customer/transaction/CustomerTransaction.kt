package com.example.shopmanagement.data.models.api.customer.transaction

data class CustomerTransaction(
    var amount: Double,
    var bill_no: Int?,
    var cheque_no: String?,
    var date: String,
    var id: Int,
    var receipt_id: Int?,
    var status: String,
    var balance : Int
)
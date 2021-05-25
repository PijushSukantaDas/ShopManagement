package com.example.shopmanagement.data.models.card

data class SalesModel(
    val Date : String,
    val billNo : String,
    val customer : String,
    val quantity : Int,
    val receiptStatus : String,
    val invoiceAmount : String,
    val partialPaid : String,
    val dueAmount : String
)
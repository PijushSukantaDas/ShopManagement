package com.example.shopmanagement.data.models.api.receipt.details

data class Data(
    val receipts: Receipts,
    val total: Double,
    val totalamount: Double,
    val totaldiscount: Double,
    val totalvat: Double
)
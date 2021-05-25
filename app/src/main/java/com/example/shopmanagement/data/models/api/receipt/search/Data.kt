package com.example.shopmanagement.data.models.api.receipt.search

data class Data(
    val receipts: Receipts,
    val total: Int,
    val totalamount: Int,
    val totaldiscount: Int,
    val totalvat: Int
)
package com.example.shopmanagement.data.models.api.invoice.details

data class Data(
    val customer: Customer,
    val invoice: Invoice,
    val no_of_product: Int,
    val no_of_quantity: String,
    val sales: List<Sale>
)
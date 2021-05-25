package com.example.shopmanagement.data.models.api.supplier.list

data class ApiSupplierList(
    val `data`: List<Data>,
    val msg: String,
    val success: Int
)
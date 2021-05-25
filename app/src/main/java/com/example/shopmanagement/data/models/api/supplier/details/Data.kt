package com.example.shopmanagement.data.models.api.supplier.details

data class Data(
    val info: Info,
    val lastYearsCredit: Double,
    val lastYearsDebit: Double,
    val lastYearsDiscount: Double,
    val lastYearsPurchaseReturnTotal: Double,
    val res: List<Re>
)
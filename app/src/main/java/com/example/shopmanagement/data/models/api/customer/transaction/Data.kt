package com.example.shopmanagement.data.models.api.customer.transaction

data class Data(
    val info: Info,
    val lastYearsCredit: Double,
    val lastYearsDebitPayment: Double,
    val lastYearsReturn: Double,
    val lastYearsTotalInvoice: Double,
    val res: List<Re>
)
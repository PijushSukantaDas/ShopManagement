package com.mobile.postally.data.models.api.supplier.details

data class SupplierTransaction(
    var amount: Int,
    var cheque_no: Any,
    var date: String,
    var discount: Int,
    var id: Int,
    var purchase_invoice: Int,
    var status: String,
    var total: Int,
    var type: Int,
    var balance : Double
)
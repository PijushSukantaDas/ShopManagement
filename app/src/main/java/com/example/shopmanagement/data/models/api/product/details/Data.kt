package com.example.shopmanagement.data.models.api.product.details

data class Data(
    val codes: List<Code>,
    val grand_sale_total: Int,
    val grand_total: Int,
    val lastYearsPurchaseReturnTotal: Int,
    val lastYearsPurchaseTotal: Int,
    val lastYearsSaleReturnTotal: Int,
    val lastYearsSaleTotal: Int,
    val lastYearsTotalQuantity: Int,
    val lastYearsPurchaseReturnQty : Int,
    val lastYearsSaleReturnTotalQty : Int,
    val lastYearsTotalSaleQuantity: Int,
    val max_date: String,
    val min_date: String,
    val product: Product,
    val res: List<Re>,
    val total_invoice_price: Int,
    val total_invoice_quantity: Int,
    val total_quantity: Int,
    val total_return_price: Int,
    val total_return_quantity: Int,
    val total_sale_quantity: String
)
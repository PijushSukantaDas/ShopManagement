package com.example.shopmanagement.ui.purchase


import com.example.shopmanagement.data.models.PurchaseInvoice
import com.example.shopmanagement.data.models.api.purchase.PurchaseData

interface IPurchaseListener {
    fun editInvoice(invoice: PurchaseData)
    fun deleteInvoice(invoice: PurchaseData, position: Int)
    fun showInformation(invoice: PurchaseInvoice)
    fun showPurchaseDetails(purchaseData: PurchaseData)
}
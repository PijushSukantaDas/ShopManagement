package com.example.shopmanagement.ui.purchase.supplier

import com.example.shopmanagement.data.models.PurchaseInvoice

interface SupplierPurchaseListener {
    fun onSupplierInvoiceClick(purchaseInvoice: PurchaseInvoice)
}
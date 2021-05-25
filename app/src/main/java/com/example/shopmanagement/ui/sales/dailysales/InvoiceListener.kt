package com.example.shopmanagement.ui.sales.dailysales

import com.example.shopmanagement.data.models.Invoice
import com.example.shopmanagement.data.models.api.invoice.list.InvoiceData

interface InvoiceListener {
    fun editInvoice(invoice: InvoiceData)
    fun deleteInvoice(invoice: InvoiceData, position: Int)
    fun showInformation(invoice: Invoice)
    fun showInvoiceDetails(invoice: InvoiceData)
}
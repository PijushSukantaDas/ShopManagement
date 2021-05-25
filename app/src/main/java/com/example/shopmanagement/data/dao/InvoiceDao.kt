package com.example.shopmanagement.data.dao

import com.example.shopmanagement.data.models.Invoice
import javax.inject.Inject

class InvoiceDao @Inject constructor(): IInvoiceDao {
    override suspend fun addInvoiceIfNotExistsOrUpdateIfChanged(invoice: Invoice): Long {
        addInvoiceIfNotExistsOrUpdateIfChanged(invoice)
        return 0L
    }

    override suspend fun addInvoice(invoice: Invoice): Long {
        addInvoice(invoice)
        return 0L
    }

    override suspend fun invoiceId(): Int = invoiceId()

    override suspend fun getInvoiceList(): List<Invoice> {
        return getInvoiceList()
    }

    override suspend fun updateInvoice(
        totalAmount: Double,
        totalDue: Double,
        totalPaid: Double,
        id : Int
    ) {
        updateInvoice(totalAmount,totalDue,totalPaid,id)
    }

    override suspend fun delete(invoiceBillNo: Int) {
        delete(invoiceBillNo)
    }

    override suspend fun getCustomerInvoiceList(id: Int): List<Invoice> {
       return getCustomerInvoiceList(id)
    }

    override suspend fun getInvoiceByIDd(id: Int): Invoice {
        return getInvoiceByIDd(id)
    }

}
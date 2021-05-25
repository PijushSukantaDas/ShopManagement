package com.example.shopmanagement.data.repositories

import com.example.shopmanagement.data.dao.IInvoiceDao
import com.example.shopmanagement.data.models.Invoice
import com.example.shopmanagement.data.models.api.invoice.list.InvoiceData
import javax.inject.Inject

class InvoiceRepository @Inject constructor(private val invoiceDao: IInvoiceDao){
    suspend fun insertInvoiceToDatabase(invoice: Invoice): Pair<Invoice, Boolean> {
        val isAdded = invoiceDao.addInvoiceIfNotExistsOrUpdateIfChanged(invoice)

        return Pair(invoice,isAdded != 0L)
    }

    suspend fun insertInvoice(invoice: Invoice) : Long = invoiceDao.addInvoice(invoice)

    suspend fun invoiceId() : Int = invoiceDao.invoiceId()

    suspend fun getInvoiceList() : List<Invoice> = invoiceDao.getInvoiceList()

    suspend fun deleteInvoice(invoice: InvoiceData) = invoiceDao.delete(invoice.id)

   suspend fun updateInvoice(
        totalAmount: Double,
        totalDue: Double,
        totalPaid: Double,
        id : Int
    ) = invoiceDao.updateInvoice(totalAmount,totalDue,totalPaid,id)

    suspend fun getCustomerInvoiceList(id: Int): List<Invoice> = invoiceDao.getCustomerInvoiceList(id)

    suspend fun getInvoiceById(id : Int) : Invoice = invoiceDao.getInvoiceByIDd(id)
}
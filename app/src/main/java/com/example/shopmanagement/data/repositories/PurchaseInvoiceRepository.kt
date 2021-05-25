package com.example.shopmanagement.data.repositories

import com.example.shopmanagement.data.dao.IPurchaseInvoiceDao
import com.example.shopmanagement.data.models.PurchaseInvoice
import com.example.shopmanagement.data.models.api.purchase.PurchaseData
import javax.inject.Inject

class PurchaseInvoiceRepository @Inject constructor(private val iPurchaseInvoiceDao: IPurchaseInvoiceDao) {
    suspend fun insertPurchaseInvoice(purchaseInvoice: PurchaseInvoice) = iPurchaseInvoiceDao.insertPurchaseInvoice(purchaseInvoice)
    suspend fun getAllPurchaseInvoice() : List<PurchaseInvoice> = iPurchaseInvoiceDao.getAllPurchaseInvoice()
    suspend fun deleteInvoicePurchase(invoice: PurchaseData) = iPurchaseInvoiceDao.deleteInvoicePurchase(invoice.purchase_invoice)
    suspend fun getInvoiceDetailsById(id: Int): PurchaseInvoice  = iPurchaseInvoiceDao.getInvoiceDetailsById(id)
    suspend fun updateInvoice(totalAmount: Double, totalDue: Double, totalPaid: Double, id: Int) = iPurchaseInvoiceDao.updateInvoice(totalAmount,totalDue,totalPaid,id)
    suspend fun getSupplierInvoiceList(id : Int): List<PurchaseInvoice> = iPurchaseInvoiceDao.getSupplierInvoiceList(id)

}
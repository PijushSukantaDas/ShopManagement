package com.example.shopmanagement.domain

import com.example.shopmanagement.data.models.PurchaseInvoice
import com.example.shopmanagement.data.models.api.purchase.PurchaseData
import com.example.shopmanagement.data.repositories.PurchaseInvoiceRepository
import javax.inject.Inject

class PurchaseInvoiceUseCase @Inject constructor(private val purchaseInvoiceRepository: PurchaseInvoiceRepository) {
    private var invoiceList : List<PurchaseInvoice> = listOf()
    suspend fun insertPurchaseInvoice(purchaseInvoice: PurchaseInvoice) = purchaseInvoiceRepository.insertPurchaseInvoice(purchaseInvoice)
    suspend fun deleteInvoicePurchase(invoice: PurchaseData) = purchaseInvoiceRepository.deleteInvoicePurchase(invoice)
    suspend fun getInvoiceDetailsById(id : Int) : PurchaseInvoice = purchaseInvoiceRepository.getInvoiceDetailsById(id)
    suspend fun updatePurchaseInvoice(
        totalAmount: Double,
        totalDue: Double,
        totalPaid: Double,
        id : Int) = purchaseInvoiceRepository.updateInvoice(totalAmount,totalDue,totalPaid,id)

    suspend fun getSupplierInvoiceList(id: Int) : List<PurchaseInvoice>? {
        invoiceList = purchaseInvoiceRepository.getSupplierInvoiceList(id)
        return invoiceList
    }

    suspend fun getAllPurchaseInvoiceList(): List<PurchaseInvoice> {
        invoiceList = purchaseInvoiceRepository.getAllPurchaseInvoice()
        return invoiceList
    }

    fun totalInvoiceBill(): Double {
        var total = 0.0
        invoiceList.map {
            total += it.totalAmount
        }

        return total
    }

    fun totalInvoiceDue(): Double {
        var total = 0.0
        invoiceList.map {
            total += it.dueAmount
        }

        return total
    }

    fun totalInvoicePayment(): Double {
        var total = 0.0
        invoiceList.map {
            total += it.partialPayment
        }

        return total
    }

    fun clearInvoiceList() = invoiceList.apply {
        invoiceList = listOf()
    }

}

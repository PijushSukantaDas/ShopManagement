package com.example.shopmanagement.data.dao

import com.example.shopmanagement.data.models.Sales
import javax.inject.Inject

class SalesDao @Inject constructor(): ISalesDao {
    override suspend fun addSalesIfNotExistsOrUpdateIfChanged(sales: Sales): Long {
        addSalesIfNotExistsOrUpdateIfChanged(sales)
        return 0L
    }

    override suspend fun addSales(sales: Sales) {
        addSales(sales)
    }

    override suspend fun getSalesByInvoiceId(id: Int): List<Sales> {
        return getSalesByInvoiceId(id)
    }

    override suspend fun deleteSales(id: Int) {
        deleteSales(id)
    }

    override suspend fun deleteSalesItem(invoiceId: Int, productId: Int) {
        deleteSalesItem(invoiceId,productId)
    }

    override suspend fun updateSales(quantity: Double, amount: Double, invoiceId : Int, productId : Int) {
        updateSales(quantity,amount,invoiceId,productId)
    }

    override suspend fun dataExist(invoiceId: Int, productId: Int): Boolean {
        return dataExist(invoiceId,productId)
    }
//
//    override suspend fun getSalesById(id: String): Invoice? {
//        TODO("Not yet implemented")
//    }
}
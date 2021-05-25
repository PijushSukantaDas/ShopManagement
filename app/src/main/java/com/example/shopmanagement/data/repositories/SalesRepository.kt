package com.example.shopmanagement.data.repositories

import com.example.shopmanagement.data.dao.ISalesDao
import com.example.shopmanagement.data.models.Sales
import javax.inject.Inject

class SalesRepository @Inject constructor(private val iSalesDao: ISalesDao) {
    suspend fun addSalesProductToDatabase(sales : Sales): Pair<ISalesDao, Boolean> {
        val isAdded = iSalesDao.addSalesIfNotExistsOrUpdateIfChanged(sales)

        return Pair(iSalesDao , isAdded != 0L)
    }

    suspend fun insertSales(sales: Sales) = iSalesDao.addSales(sales)

    suspend fun getSalesByInvoiceId(id : Int) : List<Sales> = iSalesDao.getSalesByInvoiceId(id)
    suspend fun deleteSales(id: Int) {
        iSalesDao.deleteSales(id)
    }

    suspend fun updateSales(quantity: Double, amount: Double, invoiceId: Int, productId : Int) = iSalesDao.updateSales(quantity,amount,invoiceId,productId)

    suspend fun dataExist(invoiceId : Int, productId : Int) : Boolean = iSalesDao.dataExist(invoiceId,productId)

    suspend fun deleteSalesItem(invoiceId : Int, productId : Int) = iSalesDao.deleteSalesItem(invoiceId,productId)
}
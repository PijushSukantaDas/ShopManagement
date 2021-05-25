package com.example.shopmanagement.data.dao

import androidx.room.*
import com.example.shopmanagement.data.models.PurchaseInvoice

@Dao
interface IPurchaseInvoiceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPurchaseInvoice(invoicePurchase : PurchaseInvoice) :Long

    @Query("SELECT * FROM PurchaseInvoice ORDER BY id DESC")
    suspend fun getAllPurchaseInvoice() : List<PurchaseInvoice>

    @Query("DELETE FROM PurchaseInvoice WHERE billNo LIKE :billNo")
    suspend fun deleteInvoicePurchase(billNo : Int)

    @Query("SELECT * FROM PurchaseInvoice WHERE billNo LIKE :id")
    suspend fun getInvoiceDetailsById(id : Int): PurchaseInvoice

    @Query("UPDATE PurchaseInvoice SET totalAmount =:totalAmount, dueAmount =:totalDue, partialPayment =:totalPaid WHERE billNo LIKE :id")
    suspend fun updateInvoice(totalAmount: Double, totalDue: Double, totalPaid: Double, id : Int)

    @Query("SELECT * FROM PurchaseInvoice WHERE customerId LIKE :id")
    suspend fun getSupplierInvoiceList(id: Int): List<PurchaseInvoice>


}
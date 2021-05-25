package com.example.shopmanagement.data.dao

import androidx.room.*
import com.example.shopmanagement.data.models.Invoice

@Dao
interface IInvoiceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addInvoiceIfNotExistsOrUpdateIfChanged(invoice: Invoice): Long

    @Insert
    suspend fun addInvoice(invoice : Invoice) : Long

    @Query("SELECT id FROM INVOICE")
    suspend fun invoiceId() : Int

    @Query("SELECT * FROM Invoice ORDER BY id DESC")
    suspend fun getInvoiceList() : List<Invoice>

    @Query("UPDATE Invoice SET totalAmount =:totalAmount, dueAmount =:totalDue, partialPayment =:totalPaid WHERE id LIKE :invoiceId")
    suspend fun updateInvoice(totalAmount : Double,totalDue : Double,totalPaid : Double,invoiceId : Int)

    @Query("DELETE FROM invoice WHERE billNo LIKE :invoiceBillNo")
    suspend fun delete(invoiceBillNo : Int)

    @Query("SELECT * FROM Invoice WHERE customerId LIKE :id")
    suspend fun getCustomerInvoiceList(id: Int) : List<Invoice>



    @Query("SELECT * FROM Invoice WHERE id Like :id")
    suspend fun getInvoiceByIDd( id: Int) : Invoice
}
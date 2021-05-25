package com.example.shopmanagement.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shopmanagement.data.models.Sales

@Dao
interface ISalesDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSalesIfNotExistsOrUpdateIfChanged(sales : Sales): Long

    @Insert
    suspend fun addSales(sales : Sales)

    @Query("SELECT * FROM Sales WhERE invoiceID Like :id")
    suspend fun getSalesByInvoiceId(id : Int) : List<Sales>

    @Query("DELETE FROM sales WHERE code Like :id ")
    suspend fun deleteSales(id: Int)

    @Query("DELETE FROM Sales WHERE invoiceID LIKE :invoiceId AND productID LIKE :productId")
    suspend fun deleteSalesItem(invoiceId: Int,productId: Int)

    @Query("UPDATE Sales SET dellQuantity =:quantity, totalAmount =:amount WHERE invoiceID LIKE :invoiceId AND productID LIKE :productId")
    suspend fun updateSales(
        quantity: kotlin.Double,
        amount: kotlin.Double,
        invoiceId: kotlin.Int,
        productId: kotlin.Int
    )

    @Query("SELECT EXISTS(SELECT * FROM SALES WHERE invoiceID LIKE :invoiceId AND productID LIKE :productId)")
    suspend fun dataExist(invoiceId: Int,productId: Int) : Boolean
//
//    @Query("SELECT * FROM Sales WHERE id = :id")
//    suspend fun getSalesById(id: String): Invoice?

}
package com.example.shopmanagement.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.shopmanagement.data.models.Purchase

@Dao
interface IPurchaseDao {

    @Insert
    suspend fun addPurchaseIfNotExistsOrUpdateIfChanged(purchase: Purchase): Long

    @Insert
    suspend fun insertPurchase(purchase: Purchase) : Long

    @Query("SELECT * FROM Purchase")
    fun getAllPurchase(): LiveData<List<Purchase>>

    @Query("SELECT * FROM Purchase WHERE paymentId LIKE :billNo")
    suspend fun getPurchasesByBillNo(billNo: Int): List<Purchase>

    @Query("UPDATE Purchase SET totalAmount =:totalPrice, quantity =:quantity WHERE paymentId LIKE :billId")
    suspend fun updatePurchase(totalPrice: Double, quantity: Double, billId: Int)

    @Query("SELECT EXISTS(SELECT * FROM Purchase WHERE paymentId LIKE :billNo AND productID LIKE :productId)")
    suspend fun dataExist(billNo: Int,productId: Int) : Boolean


}
package com.example.shopmanagement.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.shopmanagement.data.models.Payment

@Dao
interface IPaymentDao {
    @Insert
    suspend fun insertPayment(payment: Payment) : Long

    @Query("SELECT * FROM Payment ORDER BY id DESC")
    suspend fun getListOfPayment() : List<Payment>

    @Query("SELECT * FROM Payment WHERE supplierId LIKE :supplierId")
    fun getPaymentBySupplierId(supplierId : Int) : LiveData<List<Payment>>

    @Query("UPDATE Payment SET amount =:amount, discount =:discount, total =:total WHERE id LIKE :id")
    suspend fun updatePayment(amount : Double, discount : Double, total : Double , id : Int)
}
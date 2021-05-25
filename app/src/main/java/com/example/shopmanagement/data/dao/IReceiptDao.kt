package com.example.shopmanagement.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.shopmanagement.data.models.Receipt

@Dao
interface IReceiptDao {
    @Insert
    suspend fun insertReceipt(receipt: Receipt) : Long

    @Query("SELECT * FROM Receipt ORDER BY id DESC")
    suspend fun getAllReceipt() : List<Receipt>

    @Query("UPDATE Receipt SET amount =:amount , discount =:discount, total =:total WHERE id =:id")
    suspend fun updateReceipt(amount : Double, discount : Double, total : Double , id : Int)
}
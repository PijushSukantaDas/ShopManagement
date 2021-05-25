package com.example.shopmanagement.data.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.shopmanagement.data.models.Sales

@Dao
interface IDailyAccountDao {
    @Insert
    suspend fun addSales(sales : Sales)
}
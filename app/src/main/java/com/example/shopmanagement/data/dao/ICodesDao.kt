package com.example.shopmanagement.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.shopmanagement.data.models.Codes

@Dao
interface ICodesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCodes(codes: Codes)
}
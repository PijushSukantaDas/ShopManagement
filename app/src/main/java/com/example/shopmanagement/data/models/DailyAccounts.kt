package com.example.shopmanagement.data.models

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*

@Entity(tableName = "DailyAccount", indices = [Index("id", unique = true)])
@TypeConverters(DateTypeConverter::class)
data class DailyAccounts(
    @PrimaryKey(autoGenerate = false)
    @NonNull
    val id : Int,
    val ledgerId : Int,
    val type : String,
    val disburseType : Int,
    val typeId : Int,
    val cashRegister : Int,
    val transactionType : Int,
    val title : String,
    val category : String,
    val amount : Double,
    val employeeId : Int,
    val description : String,
    val userId : Int,
    val createdAt : Date,
    val updatedAt : Date,
    val deletedAt : Date
)
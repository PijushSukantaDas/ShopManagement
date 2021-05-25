package com.example.shopmanagement.data.models

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*


@Entity(tableName = "Payment", indices = [Index("id", unique = true)])
@TypeConverters(DateTypeConverter::class)
data class Payment(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id : Int,
    val ledgerId : Int,
    val dailyAccountId : Int,
    val supplierId : Int,
    val cashRegisterId : Int,
    val type : Int,
    val chequeNo : String,
    val paymentDate : Date,
    val chequeDate : Date,
    val amount : Double,
    val discount : Double,
    val total : Double,
    val details : String,
    val createdAt : Date,
    val updatedAt : Date,
    val deletedAt : Date
)
package com.example.shopmanagement.data.models

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*

@Entity(tableName = "Receipt", indices = [Index("id", unique = true)])
@TypeConverters(DateTypeConverter::class)
data class Receipt (
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id : Int,
    val ledgerId : Int,
    val invoiceId : Int,
    val rtvId : String,
    val type : Int,
    val dailyAccountId : Int,
    val customerId : Int,
    val cashRegisterId : Int,
    val chequeNo : String,
    val receiveDate : Date,
    val chequeDate : Date,
    val clearanceDate : Date,
    val invoiceAmount : Double,
    val rtvAmount : Double,
    val vatAmount : Double,
    val amount : Double,
    val discount : Double,
    val total : Double,
    val details : String,
    val createdAt : Date,
    val updatedAt : Date,
    val deletedAt : Date
)
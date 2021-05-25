package com.example.shopmanagement.data.models

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*

@Entity(tableName = "Sales", indices = [Index("id", unique = true)])
@TypeConverters(DateTypeConverter::class)
data class Sales(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id : Int,
    val productID : Int,
    val code : String,
    val unitPrice : Double,
    val dellQuantity : Double,
    val invoiceID : Int,
    val totalAmount : Double,
    val createdAt : Date,
    val updatedAt : Date,
    val deletedAt : Date
)
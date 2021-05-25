package com.example.shopmanagement.data.models

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*

@Entity(tableName = "PurchaseInvoice", indices = [Index("id", unique = true)])
@TypeConverters(DateTypeConverter::class)
data class PurchaseInvoice (
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int,
    val date : Date,
    val totalAmount : Double,
    val vatAmount : Double,
    val totalProductAmount : Double,
    val dueAmount : Double,
    val partialPayment : Double,
    val paymentTransactionStatus : Int,
    val receiptId : Int,
    val cashRegisterId : Int,
    val receiptStatus : Int,
    val customerId : Int,
    val userId : Int,
    val billNo : Int,
    val taxPercentage : Double,
    val createdWith : Int,
    val billDebtorStatus : Int,
    val billPaymentStatus : Int,
    val createdAt : Date,
    val updatedAt : Date,
    val deleted : Date
)
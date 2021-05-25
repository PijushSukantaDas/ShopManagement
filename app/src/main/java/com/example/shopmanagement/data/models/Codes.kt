package com.example.shopmanagement.data.models

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*

@Entity(tableName = "Codes", indices = [Index("id", unique = true)])
@TypeConverters(DateTypeConverter::class)
data class Codes (
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id : Int,
    val productID : Int,
    val productCode : String,
    val createdAt : Date,
    val updatedAt : Date,
    val deletedAt : Date
)
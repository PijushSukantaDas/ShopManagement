package com.example.shopmanagement.data.models

import androidx.room.ColumnInfo

data class SupplierNameModel(
    @ColumnInfo(name= "name")
    val name: String
)
package com.example.shopmanagement.data.models

data class SalesProductModel(
    val id : Int,
    val name : String,
    var quantity : Double,
    var price : Double,
    var totalPrice: Double
)
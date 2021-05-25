package com.example.shopmanagement.data.models.api.purchase.details
data class Data (
	val purchase : Purchase,
	val purchases : List<Purchases>,
	val no_of_quantity : Int,
	val no_of_product : Int,
	val total : Int
)
package com.example.shopmanagement.data.models.api.purchase.details


data class Purchases (

	val id : Int,
	val product_id : Int,
	val dailyaccount_id : String,
	val manufacturing_date : String,
	val expired_date : String,
	val purchase_price : Int,
	val quantity : Int,
	val sell_quantity : Int,
	val supplier_id : Int,
	val purchase_date : String,
	val total_amount : Int,
	val user_id : Int,
	val purchase_invoice : Int,
	val payment_id : String,
	val created_at : String,
	val updated_at : String,
	val deleted_at : String,
	val name : String,
	val total : Int
)
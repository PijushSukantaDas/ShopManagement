package com.example.shopmanagement.data.models.api

import retrofit2.http.Field

data class UpdatePurchase(

    @Field("user_id")
    val userId : Int,

    @Field("remember_token")
    val rememberToken : String,

    @Field("supplier_id")
    val supplierId : Int,

    @Field("sub_total")
    val subTotal : Double,

    @Field("product_id[]")
    val product : List<Int>,

    @Field("quantity[]")
    val quantity : List<Double>,

    @Field("purchase_price[]")
    val price: List<Double>,

    @Field("total_amount[]")
    val total : List<Double>,

    @Field("date")
    val date : String,

    @Field("invoice_payment")
    val invoicePayment : Boolean,

    @Field("cheque_no")
    val checkNo : String,

    @Field("total")
    val payableAmount : Double,

    @Field("discount")
    val discount: Double,

    @Field("details")
    val details : String,

    @Field("amount")
    val totalAmount : Double,

    @Field("type")
    val type: Int

)
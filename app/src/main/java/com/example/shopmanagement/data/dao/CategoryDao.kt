package com.example.shopmanagement.data.dao

import javax.inject.Inject

class CategoryDao @Inject constructor() : ICategoryDao {

    private  val categories : List<String> = listOf("Shampoo","Toothpaste","Hair Color","Body Spray","Hair Oil")
    private val gender : List<String> = listOf("Male","Female")
    private val receiptType : List<String> = listOf("Bill to Bill","Ledger")
    private val subCategories : List<String> = listOf("Sub One","Sub Two")


    //Return Categories of Product
    override fun getCategories(): List<String> {
        return categories
    }

    //Return Gender of Customer
    override fun getGender(): List<String> {
        return gender
    }

    //Returns Sub Categories of Product
    override fun getSubCategories(): List<String> {
        return subCategories
    }

    //Return Receipt Type of Customer
    override fun getReceiptType(): List<String> {
        return receiptType
    }



}
package com.example.shopmanagement.data.dao

interface ICategoryDao {
    fun getCategories() : List<String>
    fun getGender() : List<String>
    fun getSubCategories() : List<String>
    fun getReceiptType() : List<String>

}
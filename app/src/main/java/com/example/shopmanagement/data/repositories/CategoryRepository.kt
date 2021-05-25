package com.example.shopmanagement.data.repositories

import com.example.shopmanagement.data.dao.ICategoryDao
import javax.inject.Inject

class CategoryRepository @Inject constructor(private val categoryDao: ICategoryDao)  {

    //Returns Category of Product from Repository
    fun getCategories() : List<String>{
        return categoryDao.getCategories()
    }

    //Returns Gender of customer
    fun getGender() : List<String>{
        return categoryDao.getGender()
    }

    //Returns Receipt type of Customer
    fun getReceipt() : List<String>{
        return categoryDao.getReceiptType()
    }

    //Return Sub Categories of Product
    fun getSubCategories() : List<String>{
        return  categoryDao.getSubCategories()
    }
}
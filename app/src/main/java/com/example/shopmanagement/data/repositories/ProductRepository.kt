package com.example.shopmanagement.data.repositories

import androidx.lifecycle.LiveData
import com.example.shopmanagement.data.dao.IProductDao
import com.example.shopmanagement.data.models.Product
import javax.inject.Inject

class ProductRepository @Inject constructor(private val iProductDao: IProductDao) {

    suspend fun getNumberOfProducts(): Int {
        return iProductDao.getNumberOfProducts()
    }

    fun getProductNames(): LiveData<List<String>> = iProductDao.getProductNames()

    suspend fun addProductIfNotExistsOrUpdateIfChanged(
        id: Int,
        name: String,
        sellingPrice: Double,
        category: String?,
        subCategory: String?,
        code: String?,
        description: String?,
        productImage: String?,
        alertQuantity: Int
    ){
        val product = Product(
            id,
            name,
            sellingPrice,
            category?:"",
            subCategory?:"",
            code?:"",
            description?:"",
            productImage?:"",
            alertQuantity
        )
        iProductDao.addProduct(product)

    }

    suspend fun getProductById(productId: Int): Product {
        return iProductDao.getProductById(productId)
    }

    suspend fun getSalesProduct(id : Int) : Product = iProductDao.getSalesProduct(id)

    fun getAllProducts(): LiveData<List<Product>> {
        return iProductDao.getAllProducts()
    }

    fun getProductByName(name :String) = iProductDao.getProductByName(name)

    suspend fun getProductNumbers() : Int = iProductDao.getNumberOfProducts()

    suspend fun updateAlertQuantity(id : Int,quantity : Double) = iProductDao.updateAlertQuantity(id,quantity)
    suspend fun getProductListById(id: Int) = iProductDao.getProductList(id)
    suspend fun deleteLocalProduct(id: Int) = iProductDao.deleteProduct(id)


}
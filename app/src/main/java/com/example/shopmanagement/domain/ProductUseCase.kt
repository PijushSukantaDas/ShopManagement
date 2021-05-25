package com.example.shopmanagement.domain

import androidx.lifecycle.LiveData
import com.example.shopmanagement.data.api.ApiHelper
import com.example.shopmanagement.data.models.Product
import com.example.shopmanagement.data.repositories.CategoryRepository
import com.example.shopmanagement.data.repositories.ProductRepository
import javax.inject.Inject

class ProductUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
    private val apiHelper: ApiHelper
    ) {

    suspend fun addProduct(
        id: Int,
        name: String,
        sellingPrice: Double,
        category: String,
        subCategory: String,
        code: String, description: String, productImage: String, alertQuantity: Int
    ) {
        productRepository.addProductIfNotExistsOrUpdateIfChanged(id, name, sellingPrice, category, subCategory, code, description, productImage,alertQuantity)
    }

    fun getAllProducts(): LiveData<List<Product>> {
        return productRepository.getAllProducts()
    }

    fun getCategories() : List<String>{
        return  categoryRepository.getCategories()
    }

    fun getCategoryByPosition(position: Int) :String{
        return getCategories()[position]
    }

    fun getSubCategories() : List<String> = categoryRepository.getSubCategories()

    fun getProductNames() : LiveData<List<String>> = productRepository.getProductNames()

    fun getProductByName(name : String) = productRepository.getProductByName(name)

    suspend fun getProductNumbers() : Int = productRepository.getProductNumbers()

    suspend fun getProductById(productId: Int): Product {
        return productRepository.getProductById(productId)
    }

    suspend fun getSalesProduct(id : Int) : Product = productRepository.getSalesProduct(id)
    suspend fun deleteApiProduct(product: Product) = apiHelper.deleteProduct(product)

    suspend fun searchProduct( nameOrCode : String ) = apiHelper.searchProduct(nameOrCode)
    suspend fun getProductList(id: Int) = productRepository.getProductListById(id)
    suspend fun deleteLocalProduct(id: Int) = productRepository.deleteLocalProduct(id)

}
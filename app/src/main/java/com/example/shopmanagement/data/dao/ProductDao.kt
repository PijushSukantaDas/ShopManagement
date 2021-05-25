package com.example.shopmanagement.data.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.shopmanagement.data.models.Product
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductDao @Inject constructor() : IProductDao {
    private val products = mutableSetOf<Product>()


    /**
     * @return true, if product is added otherwise false
     */
    override suspend fun addProductIfNotExistsOrUpdateIfChanged(product: Product): Long {
        val item = products.find { item -> item.id == product.id }
        if (item == null) { // product doesn't exist
            products.add(product)
            return UUID.randomUUID().timestamp()
        } else if(isProductDataChanged(product, item)) {
            item.name = product.name
            item.sellingPrice = product.sellingPrice
        }
        return 0L
    }


    private fun isProductDataChanged(
        product: Product,
        item: Product
    ) = !product.name.contentEquals(item.name) || product.sellingPrice != item.sellingPrice

     override suspend fun getProductById(id: Int): Product {
        return products.find { product ->
            product.id == id
        }!!
    }

    override suspend fun getSalesProduct(id: Int): Product {
        return getSalesProduct(id)
    }

    override fun getAllProducts(): LiveData<List<Product>> {
        return liveData {
            emit(products.toList())
        }
    }

    override fun getProductNames(): LiveData<List<String>> {
        return liveData {
            products.toList().map { product -> product.name }
        }
    }

    override suspend fun addProduct(product: Product) {
        products.add(product)
    }

    override suspend fun getNumberOfProducts(): Int {
        return products.size
    }

    override fun getProductByName(name: String): LiveData<Product> {
        return liveData {
            products
        }
    }

    override suspend fun updateAlertQuantity(id: Int,quantity : Double) {
        updateAlertQuantity(id,quantity)
    }

    override suspend fun getProductList(id: Int): List<Product> {
        return getProductList(id)
    }

    override suspend fun deleteProduct(id: Int) {
        deleteProduct(id)
    }


    fun getProductByPosition(position: Int): Product {
        return products.toList()[position]
    }
}
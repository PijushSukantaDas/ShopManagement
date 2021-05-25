package com.example.shopmanagement.data.dao

import com.example.shopmanagement.data.models.Product


interface InventoryDao {
    fun addProduct(product: Product)
    fun addProduct(product: Product, numberOfUnits: Int)
    fun removeProduct(product: Product)
    fun getNumberOfAvailableItems(product: Product): Int
}
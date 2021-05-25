package com.example.shopmanagement.data.repositories

import com.example.shopmanagement.data.dao.InventoryDao
import com.example.shopmanagement.data.models.Product
import timber.log.Timber
import javax.inject.Inject

class InventoryRepository @Inject constructor(private val inventoryDao: InventoryDao) {
    /**
     * @return if new product is added in product list, new product is returned, else returns null
     */
    fun addItemToInventory(product: Product, numberOfUnit: Int){
        inventoryDao.addProduct(product, numberOfUnit)
        Timber.d("===> added to Inventory: numbers: ${inventoryDao.getNumberOfAvailableItems(product)} \nproduct: $product")
    }

    fun getNumberOfAvailableItems(product: Product): Int {
        return inventoryDao.getNumberOfAvailableItems(product)
    }
}
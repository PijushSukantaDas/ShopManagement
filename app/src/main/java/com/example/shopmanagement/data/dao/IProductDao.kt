package com.example.shopmanagement.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shopmanagement.data.models.Product

@Dao
interface IProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProductIfNotExistsOrUpdateIfChanged(product: Product): Long

    @Query("SELECT * FROM PRODUCT WHERE id LIKE :id")
    suspend fun getProductById(id: Int) : Product

    @Query("SELECT * FROM product WHERE id LIKE :id")
    suspend fun getSalesProduct(id : Int) : Product

    @Query("SELECT * FROM PRODUCT")
    fun getAllProducts(): LiveData<List<Product>>

    @Query("SELECT name FROM PRODUCT")
    fun getProductNames(): LiveData<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProduct(product: Product)

    @Query("SELECT COUNT(id) FROM Product")
    suspend fun getNumberOfProducts(): Int

    @Query("SELECT * FROM Product WHERE name LIKE :name OR code LIKE :name")
    fun getProductByName(name : String) : LiveData<Product>

    @Query("UPDATE Product SET alertQuantity = :quantity WHERE id Like :id")
    suspend fun updateAlertQuantity(id : Int,quantity : Double)

    @Query("SELECT * FROM Product WHERE id LIKE :id OR code LIKE :id")
    suspend fun getProductList(id: Int) : List<Product>

    @Query("DELETE FROM Product WHERE id LIKE :id")
    suspend fun deleteProduct(id: Int)

}
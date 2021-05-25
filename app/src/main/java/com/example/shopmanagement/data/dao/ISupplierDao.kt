package com.example.shopmanagement.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.shopmanagement.data.models.Supplier

@Dao
interface ISupplierDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSupplierIfNotExistsOrUpdateIfChanged(supplier: Supplier): Long

    @Query("SELECT * FROM Supplier")
    fun getAllSupplier(): LiveData<List<Supplier>>

    @Query("SELECT name from Supplier")
    fun getSupplierName() : LiveData<List<String>>

    @Query("SELECT * FROM Supplier WHERE name LIKE :supplier OR mobile LIKE :supplier")
    suspend fun getSupplierDetails(supplier : String) : Supplier

    @Query("SELECT * FROM Supplier WHERE name LIKE :supplier OR mobile LIKE :supplier")
    fun getSupplierDetailsForPurchase(supplier : String) : LiveData<Supplier>

    @Query("SELECT * FROM Supplier WHERE id LIKE :supplierId")
    suspend fun getSupplierById(supplierId: Int) : Supplier

    @Query("UPDATE Supplier SET openingBalance =:balance WHERE id Like :supplierID")
    suspend fun updateBalance(supplierID: Int, balance: Double)

    @Delete
    suspend fun deleteSupplier(supplier: Supplier)

    @Query("SELECT * FROM Supplier WHERE id LIKE :id")
    suspend fun getSearchedSupplier(id: Int) : List<Supplier>
}
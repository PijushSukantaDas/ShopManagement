package com.example.shopmanagement.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.shopmanagement.data.models.Customer

@Dao
interface ICustomerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCustomerIfNotExistsOrUpdateIfChanged(customer: Customer): Long

    @Query("SELECT * FROM Customer")
    fun getAllCustomer(): LiveData<List<Customer>>

    @Query("SELECT name from Customer")
    fun getCustomerName() : LiveData<List<String>>

    @Query("SELECT * from Customer WHERE name LIKE :name OR customerMobile LIKE :name ")
    fun getCustomerByName(name : String) : LiveData<Customer>

    @Query("SELECT * from Customer WHERE id LIKE :id")
    suspend fun getCustomerById(id : Int) : Customer

    @Query("UPDATE Customer SET customerOpeningBalance = :balance WHERE id Like :id")
    suspend fun updateBalance(id : Int,balance : Double)

    @Query("SELECT * from Customer WHERE name LIKE :name OR customerMobile LIKE :name")
    suspend fun getReceiptCustomer(name: String): Customer

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer : Customer)

    @Delete
    suspend fun deleteCustomer(customer: Customer)

    @Query("SELECT * FROM Customer WHERE customerMobile LIKE :mobile")
    suspend fun searchCustomer(mobile: String) : List<Customer>

}
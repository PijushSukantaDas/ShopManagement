package com.example.shopmanagement.data.repositories

import androidx.lifecycle.LiveData
import com.example.shopmanagement.data.dao.ICustomerDao
import com.example.shopmanagement.data.models.Customer
import javax.inject.Inject

class CustomerRepository @Inject constructor(private val customerDao: ICustomerDao) {

    suspend fun addCustomerIfExistOrUpdate(
        id: Int,
        customerName: String,
        customerAddress: String,
        customerMobile: String,
        customerGender: String,
        customerNid: String,
        customerVat: String,
        customerEmail: String,
        customerOpeningBalance : Double,
        customerReceiptType : String
    ) : Pair<Customer, Boolean>{
        val customer = Customer(
            id,
            customerName,
            customerAddress,
            customerMobile,
            customerGender,
            customerNid,
            customerVat,
            customerEmail,
            customerOpeningBalance,
            customerReceiptType
        )

        val isAdded = customerDao.addCustomerIfNotExistsOrUpdateIfChanged(customer)
        return Pair(customer, isAdded != 0L)
    }

    fun getAllCustomer() : LiveData<List<Customer>>{
        return customerDao.getAllCustomer()
    }
    fun getCustomerName() = customerDao.getCustomerName()

    fun getCustomerByName(name : String) : LiveData<Customer> = customerDao.getCustomerByName(name)

    suspend fun updateBalance(id : Int, balance : Double) = customerDao.updateBalance(id, balance)

    suspend fun getCustomerById(id : Int) : Customer = customerDao.getCustomerById(id)

    suspend fun getReceiptCustomer(name: String): Customer = customerDao.getReceiptCustomer(name)
    suspend fun insertCustomer(customer : Customer) = customerDao.insertCustomer(customer)
    suspend fun deleteCustomer(customer: Customer) = customerDao.deleteCustomer(customer)
    suspend fun searchCustomerLocal(mobile: String) = customerDao.searchCustomer(mobile)

}
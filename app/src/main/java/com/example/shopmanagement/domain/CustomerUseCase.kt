package com.example.shopmanagement.domain

import androidx.lifecycle.LiveData
import com.example.shopmanagement.data.api.ApiHelper
import com.example.shopmanagement.data.models.Customer
import com.example.shopmanagement.data.models.Invoice
import com.example.shopmanagement.data.repositories.CategoryRepository
import com.example.shopmanagement.data.repositories.CustomerRepository
import com.example.shopmanagement.data.repositories.InvoiceRepository
import javax.inject.Inject

class CustomerUseCase @Inject constructor(
    private val customerRepository: CustomerRepository,
    private val categoryRepository: CategoryRepository,
    private val invoiceRepository: InvoiceRepository,
    private val apiHelper: ApiHelper
    ) {

    private var customerList : List<Customer> = listOf()

    suspend fun addCustomer(
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
    ){
        customerRepository.addCustomerIfExistOrUpdate(
            id ,
            customerName,
            customerAddress,
            customerMobile,
            customerGender,
            customerNid,
            customerVat,
            customerEmail,
            customerOpeningBalance,
            customerReceiptType)
    }

    suspend fun insertCustomer(customer : Customer) = customerRepository.insertCustomer(customer)

    fun getAllCustomer() : LiveData<List<Customer>> {
        customerList = customerRepository.getAllCustomer().value ?: listOf()
        return customerRepository.getAllCustomer()
    }

    suspend fun isCustomerEmpty(): Boolean {
        return getAllCustomer().value!!.isEmpty()
    }

    fun getGender() : List<String> = categoryRepository.getGender()

    fun onGenderItemClicked(position: Int): String {
        return getGender()[position]
    }

    fun getReceiptType() : List<String> = categoryRepository.getReceipt()

    fun getCustomerName() = customerRepository.getCustomerName()

    fun getCustomerByName(name : String) : LiveData<Customer> = customerRepository.getCustomerByName(name)

    suspend fun getReceiptCustomer(name : String) : Customer = customerRepository.getReceiptCustomer(name)

    suspend fun getCustomerById(id : Int) : Customer = customerRepository.getCustomerById(id)

    suspend fun updateBalance(id : Int, balance : Double) = customerRepository.updateBalance(id, balance)

    suspend fun getCustomerInvoiceList(id: Int) : List<Invoice> = invoiceRepository.getCustomerInvoiceList(id)
    suspend fun deleteCustomer(customer: Customer) = customerRepository.deleteCustomer(customer)

    /**Deletion Customer From Server**/
    suspend fun deleteApiCustomer(customer: Customer)  = apiHelper.deleteCustomer(customer)
    suspend fun searchCustomer(searchString: String, customerMobile: String)  = apiHelper.searchCustomer(searchString,customerMobile)
    suspend fun searchLocalCustomer(mobile: String) = customerRepository.searchCustomerLocal(mobile)

}
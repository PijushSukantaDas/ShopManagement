package com.example.shopmanagement.ui.customer

import com.example.shopmanagement.data.models.Customer

interface ICustomerListener {
    fun navigateToDetails(customer: Customer)
    fun editCustomer(customer: Customer)
    fun deleteCustomer(customer: Customer)
}
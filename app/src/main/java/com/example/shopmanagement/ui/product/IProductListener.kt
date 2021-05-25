package com.example.shopmanagement.ui.product

import com.example.shopmanagement.data.models.Product

interface IProductListener {
    fun navigateToDetails(product: Product)
    fun editProduct(product: Product)
    fun deleteProduct(product: Product)
}
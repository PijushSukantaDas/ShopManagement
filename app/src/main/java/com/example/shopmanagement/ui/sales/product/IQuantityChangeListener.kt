package com.example.shopmanagement.ui.sales.product

import com.example.shopmanagement.data.models.SalesProductModel
import com.example.shopmanagement.databinding.ProductSelectedLayoutBinding

interface IQuantityChangeListener {
    fun updateProduct(position: Int, quantity: Double, totalPrice: Double, price : Double)
    fun incrementQuantity(product : SalesProductModel)
    fun deleteProduct(product : SalesProductModel)
    fun showWeightDialog(
        product: SalesProductModel,
        position: Int,
        binding: ProductSelectedLayoutBinding
    )
}
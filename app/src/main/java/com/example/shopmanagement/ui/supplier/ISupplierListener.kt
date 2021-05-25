package com.example.shopmanagement.ui.supplier

import com.example.shopmanagement.data.models.Supplier

interface ISupplierListener {
    fun navigateToSupplierDetails(supplier : Supplier)
    fun editSupplier(supplier: Supplier)
    fun deleteSupplier(supplier: Supplier)
}
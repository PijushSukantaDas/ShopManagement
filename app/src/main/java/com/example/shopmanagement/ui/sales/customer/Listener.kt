package com.example.shopmanagement.ui.sales.customer

import com.example.shopmanagement.data.models.Invoice

interface Listener {
    fun showDetails(invoice: Invoice)
}
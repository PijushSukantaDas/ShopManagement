package com.example.shopmanagement.data.models.api.invoice.search

import com.example.shopmanagement.data.models.api.invoice.list.InvoiceData

data class Data(
    val current_page: Int,
    val `data`: List<InvoiceData>,
    val first_page_url: String,
    val from: Int,
    val last_page: Int,
    val last_page_url: String,
    val next_page_url: Any,
    val path: String,
    val per_page: Int,
    val prev_page_url: Any,
    val to: Int,
    val total: Int
)
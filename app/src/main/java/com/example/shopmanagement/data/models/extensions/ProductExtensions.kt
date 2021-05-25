package com.example.shopmanagement.data.models.extensions

import com.example.shopmanagement.data.models.Product

fun Product.isDataNotFound(): Boolean {
    return name.isEmpty() || sellingPrice == 0.0
}
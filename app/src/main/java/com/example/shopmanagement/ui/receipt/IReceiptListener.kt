package com.example.shopmanagement.ui.receipt

import com.example.shopmanagement.data.models.api.receipt.DataList

interface IReceiptListener {
    fun onReceiptClick(receipt: DataList)
    fun deleteReceipt(receipt: DataList)
    fun toReceiptDetails(receipt: DataList)
}
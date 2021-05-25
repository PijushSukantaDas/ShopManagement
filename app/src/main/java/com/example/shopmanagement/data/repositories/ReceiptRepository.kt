package com.example.shopmanagement.data.repositories

import com.example.shopmanagement.data.dao.IReceiptDao
import com.example.shopmanagement.data.models.Receipt
import javax.inject.Inject

class ReceiptRepository @Inject constructor(private val iReceiptDao: IReceiptDao) {
    suspend fun insertReceipt(receipt: Receipt) = iReceiptDao.insertReceipt(receipt)
    suspend fun getAllReceipt(): List<Receipt> = iReceiptDao.getAllReceipt()
    suspend fun updateReceipt(amount: Double, discount: Double, total: Double, editReceiptId: Int) = iReceiptDao.updateReceipt(amount,discount,total,editReceiptId)
}
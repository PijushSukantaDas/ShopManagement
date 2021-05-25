package com.example.shopmanagement.data.repositories

import com.example.shopmanagement.data.dao.IPaymentDao
import com.example.shopmanagement.data.models.Payment
import javax.inject.Inject

class PaymentRepository @Inject constructor(private val iPaymentDao: IPaymentDao) {
    suspend fun insertPayment(payment : Payment) = iPaymentDao.insertPayment(payment)
    suspend fun getListOfPayments(): List<Payment> = iPaymentDao.getListOfPayment()
    suspend fun updatePayment(amount: Double, discount: Double, total: Double, paymentId: Int) = iPaymentDao.updatePayment(amount,discount,total,paymentId)
}
package com.example.shopmanagement.ui.payment

import com.example.shopmanagement.data.models.api.payment.PaymentList

interface IPaymentListener {
    fun onPaymentItemClick(payment: PaymentList)
    fun deletePayment(payment: PaymentList)
    fun toPaymentDetails(payment: PaymentList)
}
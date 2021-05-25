package com.example.shopmanagement.domain

import com.example.shopmanagement.data.models.Payment
import com.example.shopmanagement.data.repositories.PaymentRepository
import javax.inject.Inject

class PaymentUseCase @Inject constructor(private val paymentRepository: PaymentRepository) {

    private var paymentList : List<Payment> = listOf()

    suspend fun insertPayment(payment: Payment) = paymentRepository.insertPayment(payment)
    suspend fun getListOfPayments() : List<Payment> {
        paymentList = paymentRepository.getListOfPayments()

        return paymentList
    }

    suspend fun updatePayment(amount : Double, discount : Double, total : Double, paymentId : Int) = paymentRepository.updatePayment(amount,discount,total,paymentId)

    /** Returning Total Amount of amount Column**/
    fun totalAmount(): Double {
        var amount = 0.0
        paymentList.map {
            amount += it.amount
        }

        return amount
    }

    /** Returning Total Amount of discount Column**/
    fun totalDiscount() : Double{
        var amount = 0.0
        paymentList.map {
            amount += it.discount
        }

        return amount
    }


    /** Returning Total Amount of total Column**/
    fun total() : Double{
        var amount = 0.0
        paymentList.map {
            amount += it.total
        }

        return amount
    }
}
package com.example.shopmanagement.domain

import com.example.shopmanagement.data.models.Receipt
import com.example.shopmanagement.data.repositories.ApiRepository
import com.example.shopmanagement.data.repositories.ReceiptRepository
import javax.inject.Inject

class ReceiptUseCase @Inject constructor(
    private val receiptRepository: ReceiptRepository,
    private val apiRepository: ApiRepository
) {
    private var receiptList : List<Receipt> = listOf()
    suspend fun insertReceipt(receipt: Receipt) = receiptRepository.insertReceipt(receipt)

    /** Returning List of All Receipt **/
    suspend fun getAllReceipt() : List<Receipt> {
        receiptList = receiptRepository.getAllReceipt()

        return receiptList
    }

    /** Returning Total Amount of amount Column**/
    fun totalAmount(): Double {
        var amount = 0.0
        receiptList.map {
            amount += it.amount
        }

        return amount
    }

    /** Returning Total Amount of discount Column**/
    fun totalDiscount() : Double{
        var amount = 0.0
        receiptList.map {
            amount += it.discount
        }

        return amount
    }


    /** Returning Total Amount of total Column**/
    fun total() : Double{
        var amount = 0.0
        receiptList.map {
            amount += it.total
        }

        return amount
    }

    suspend fun insertApiReceipt(receipt : Receipt) = apiRepository.insertReceipt(receipt)
    suspend fun updateReceipt(amount : Double, discount : Double, total: Double, editReceiptId: Int) = receiptRepository.updateReceipt(amount,discount,total,editReceiptId)
}
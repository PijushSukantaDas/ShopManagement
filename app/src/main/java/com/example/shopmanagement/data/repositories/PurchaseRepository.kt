package com.example.shopmanagement.data.repositories

import androidx.lifecycle.LiveData
import com.example.shopmanagement.data.dao.IPurchaseDao
import com.example.shopmanagement.data.models.Purchase
import javax.inject.Inject

class PurchaseRepository @Inject constructor(private val iPurchaseDao: IPurchaseDao) {

    suspend fun addOrUpdatePurchase(purchase : Purchase) : Pair<Purchase, Boolean>{
        val isAdded = iPurchaseDao.addPurchaseIfNotExistsOrUpdateIfChanged(purchase)
        return Pair(purchase, isAdded != 0L)
    }

    suspend fun insertPurchase(purchase: Purchase) = iPurchaseDao.insertPurchase(purchase)

    fun getPurchases() : LiveData<List<Purchase>> = iPurchaseDao.getAllPurchase()
    suspend fun getPurchasesByBillNo(billNo: Int): List<Purchase> = iPurchaseDao.getPurchasesByBillNo(billNo)
    suspend fun updatePurchase(totalPrice: Double, quantity: Double, billId: Int)  = iPurchaseDao.updatePurchase(totalPrice,quantity,billId)
    suspend fun dataExist(billNo: Int,productId:Int) : Boolean = iPurchaseDao.dataExist(billNo,productId)
}
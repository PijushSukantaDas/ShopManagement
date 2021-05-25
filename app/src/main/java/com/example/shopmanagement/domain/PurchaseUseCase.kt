package com.example.shopmanagement.domain

import androidx.lifecycle.liveData
import com.example.shopmanagement.data.api.ApiHelper
import com.example.shopmanagement.data.models.Purchase
import com.example.shopmanagement.data.models.api.purchase.PurchaseData
import com.example.shopmanagement.data.models.api.purchase.list.ApiPurchaseList
import com.example.shopmanagement.data.repositories.ProductRepository
import com.example.shopmanagement.data.repositories.PurchaseRepository
import com.example.shopmanagement.ui.utils.Resource
import kotlinx.coroutines.Dispatchers
import retrofit2.Response
import javax.inject.Inject

class PurchaseUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val supplierUseCase: SupplierUseCase,
    private val purchaseRepository: PurchaseRepository,
    private val apiHelper: ApiHelper
) {

    var purchaseList : ArrayList<PurchaseData> = arrayListOf()

    suspend fun insertPurchase(purchase: Purchase) = purchaseRepository.insertPurchase(purchase)

    suspend fun getPurchasesByBillNo(billNo: Int): List<Purchase> =  purchaseRepository.getPurchasesByBillNo(billNo)

    suspend fun updatePurchase(totalPrice: Double, quantity: Double, billId: Int) = purchaseRepository.updatePurchase(totalPrice,quantity,billId)

    suspend fun dataExist(billNo: Int,productId:Int) : Boolean = purchaseRepository.dataExist(billNo,productId)

    suspend fun getUserPurchaseList(page : Int): Response<ApiPurchaseList> {
        purchaseList = ArrayList(apiHelper.getUserPurchaseList(page).body()?.data?.purchases?.data ?: listOf())
        return apiHelper.getUserPurchaseList(page)
    }


    fun getSpecificPurchaseDetails(purchaseInvoiceId : Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = apiHelper.getSpecificPurchaseDetails(purchaseInvoiceId)
                )
            )
        }catch (exception : Exception){
            emit(Resource.error(data = null,message = exception.toString()))
        }
    }

    fun purchaseTotal(): Int {
        var total = 0
        purchaseList.map {
            total += it.total
        }
        return total
    }

    suspend fun deletePurchase(invoice: PurchaseData) = apiHelper.deletePurchase(invoice)

    fun purchaseList() = purchaseList

    fun deleteLocalPurchase(purchase : PurchaseData){
        purchaseList.remove(purchase)
    }

    suspend fun searchedWebPurchase(page : Int,search: String, fromDate: String, toDate: String, purchaseInvoiceNo : String)
    = apiHelper.searchPurchase(page,search,fromDate,toDate,purchaseInvoiceNo)

    fun setSearchList(list: List<PurchaseData>) {
        purchaseList = ArrayList(list)
    }

}
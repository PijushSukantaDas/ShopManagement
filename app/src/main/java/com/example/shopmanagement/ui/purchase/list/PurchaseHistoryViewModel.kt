package com.example.shopmanagement.ui.purchase.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.shopmanagement.data.models.Supplier
import com.example.shopmanagement.data.models.api.purchase.PurchaseData
import com.example.shopmanagement.data.repositories.paging.PurchasePagingSource
import com.example.shopmanagement.data.repositories.paging.PurchaseSearchSource
import com.example.shopmanagement.domain.PurchaseUseCase
import com.example.shopmanagement.domain.SupplierUseCase
import com.example.shopmanagement.ui.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PurchaseHistoryViewModel @Inject constructor(
    private val purchaseUseCase: PurchaseUseCase,
    private val supplierUseCase: SupplierUseCase
) : ViewModel(){

    var totalPurchase = MutableLiveData<String>()
    var searchString = MutableLiveData<String>()
    var customerNameOrMobile = MutableLiveData<String>()
    var invoiceBillNo = MutableLiveData<String>()
    var supplier = MutableLiveData<Supplier>()

    fun purchaseList() = Pager(
        PagingConfig(
            pageSize = 5
        ),
        pagingSourceFactory = {
            PurchasePagingSource(
                purchaseUseCase
            )
        }
    ).flow.cachedIn(viewModelScope)

    fun searchList(mobile: String?,purchaseInvoice : String,fromDate:String,toDate:String) = Pager(
        PagingConfig(
            pageSize = 5
        ),
        pagingSourceFactory = {
            PurchaseSearchSource(
                purchaseUseCase,
                mobile?:"",
                purchaseInvoice,
                fromDate,
                toDate
            )
        }
    ).flow.cachedIn(viewModelScope)

//    fun getPurchaseList() = liveData(Dispatchers.IO) {
//        emit(Resource.loading(data = null))
//        try {
//            emit(
//                Resource.success(
//                    data = purchaseUseCase.getUserPurchaseList(page)
//                )
//            )
//
//        }catch (exception : Exception){
//            emit(Resource.error(data = null, message = exception.toString()))
//        }
//    }

    fun getSpecificPurchaseDetails(purchaseInvoiceId: Int) = purchaseUseCase.getSpecificPurchaseDetails(purchaseInvoiceId)

    fun totalPurchase(){
        totalPurchase.value = purchaseUseCase.purchaseTotal().toString()
    }

    fun deletePurchase(invoice: PurchaseData) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = purchaseUseCase.deletePurchase(invoice)
                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.toString()))
        }
    }

    fun deleteFromLocalPurchaseList(invoice: PurchaseData)  = purchaseUseCase.deleteLocalPurchase(invoice)

    fun getPurchaseListLocal() = purchaseUseCase.purchaseList()

//    fun searchInvoice(search : String?, fromDate : String, toDate: String, purchaseInvoiceNo : String?)
//            = purchaseUseCase.searchedWebPurchase(search?:"",fromDate,toDate,purchaseInvoiceNo?:"")

    fun setSearchList(list : List<PurchaseData>) = purchaseUseCase.setSearchList(list)
    fun getSupplier(mobile: String?) = viewModelScope.launch {
        supplier.value = supplierUseCase.getSupplierDetails(mobile?:"")
    }

}
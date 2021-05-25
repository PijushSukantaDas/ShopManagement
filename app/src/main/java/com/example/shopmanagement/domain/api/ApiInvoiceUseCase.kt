package com.example.shopmanagement.domain.api

import androidx.lifecycle.liveData
import com.example.shopmanagement.data.api.ApiHelper
import com.example.shopmanagement.ui.utils.Resource
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ApiInvoiceUseCase @Inject constructor(
    private val apiHelper: ApiHelper
) {

    fun getSpecificInvoiceDetails(invoiceId : Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = apiHelper.getSpecificInvoiceDetails(invoiceId)
                )
            )
        }catch (exception : Exception){
            emit(Resource.error(data = null,message = exception.toString()))
        }
    }

}
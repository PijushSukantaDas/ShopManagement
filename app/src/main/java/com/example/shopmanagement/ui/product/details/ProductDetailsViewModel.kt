package com.example.shopmanagement.ui.product.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.shopmanagement.domain.api.ApiUseCase
import com.example.shopmanagement.ui.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val apiUseCase: ApiUseCase
) : ViewModel() {

    var productId = 0

    fun getProductDetails(productId : Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = apiUseCase.getProductDetails(
                        productId
                    )
                )
            )

        }catch (exception : Exception){
            emit(Resource.error(data = null,message = exception.toString()))
        }
    }

    fun getTotalQuantity() = apiUseCase.totalSpecificProductQuantity()

    fun getTotalPurchaseQuantity() = apiUseCase.totalPurchaseProductQuantity()
    fun productDateSearch(productId: Int, fromDate: String, toDate: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = apiUseCase.productDateSearch(
                        productId,
                        fromDate,
                        toDate
                    )
                )
            )

        }catch (exception : Exception){
            emit(Resource.error(data = null,message = exception.toString()))
        }
    }
}
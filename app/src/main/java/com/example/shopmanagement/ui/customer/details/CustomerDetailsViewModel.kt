package com.example.shopmanagement.ui.customer.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.shopmanagement.data.models.api.customer.transaction.Re
import com.example.shopmanagement.domain.CustomerUseCase
import com.example.shopmanagement.domain.api.ApiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class CustomerDetailsViewModel @Inject constructor(
    private val apiUseCase: ApiUseCase,
    private val customerUseCase: CustomerUseCase
): ViewModel() {


    fun getCustomerDetails(customerId: Int) = liveData(Dispatchers.IO) {
        emit(com.example.shopmanagement.ui.utils.Resource.loading(data = null))
        try {
            emit(
                com.example.shopmanagement.ui.utils.Resource.success(
                    data = apiUseCase.getCustomerDetails(
                        customerId
                    )
                )
            )
        }catch (exception : Exception){
            emit(com.example.shopmanagement.ui.utils.Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun apiCustomerBalance() = apiUseCase.getCustomerBalance()

    fun getApiCustomerTotalPayment() = apiUseCase.totalPayment()
    fun apiCustomerTotalInvoice() = apiUseCase.getTotalInvoice()
    fun getBalanceList() = apiUseCase.getCustomerBalanceList()
    fun getDateSearchReceiptList(customerId: Int, fromDate: String, toDate: String) = apiUseCase.getDateSearchReceiptList(customerId,fromDate,toDate)
    fun setBalanceList(res: List<Re>) = apiUseCase.setBalanceList(res)

}
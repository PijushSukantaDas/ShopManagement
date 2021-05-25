package com.example.shopmanagement.ui.sales.dailysales

import androidx.lifecycle.*
import androidx.paging.*
import com.example.shopmanagement.data.models.Customer
import com.example.shopmanagement.data.models.api.invoice.details.ApiSpecificInvoice
import com.example.shopmanagement.data.models.api.invoice.list.InvoiceData
import com.example.shopmanagement.data.repositories.paging.InvoicePagingSource
import com.example.shopmanagement.data.repositories.paging.InvoiceSearchSource
import com.example.shopmanagement.domain.CustomerUseCase
import com.example.shopmanagement.domain.InvoiceUseCase
import com.example.shopmanagement.domain.api.ApiUseCase
import com.example.shopmanagement.domain.api.ApiInvoiceUseCase
import com.example.shopmanagement.ui.utils.Event
import com.example.shopmanagement.ui.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SalesHistoryViewModel @Inject constructor(
    private val apiUseCase: ApiUseCase,
    private val apiInvoiceUseCase : ApiInvoiceUseCase,
    private val invoiceUseCase: InvoiceUseCase,
    private val customerUseCase: CustomerUseCase
) : ViewModel(){

    var searchString = MutableLiveData<String>()
    var invoiceCustomerDetails = MutableLiveData<ApiSpecificInvoice>()
    var invoiceTotal = MutableLiveData<String>()
    var customerNameOrMobile = MutableLiveData<String>()
    var invoiceBillNo = MutableLiveData<String>()
    var customer = MutableLiveData<Customer>()
    var total = MutableLiveData<Double>()

    var initTotal = MutableLiveData<Double>()


    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>>
        get() = _errorMessage

    fun searchValidation(): Boolean {
        _errorMessage.value = when{
            searchString.value.isNullOrEmpty()->{
                Event("Search Field is Empty!")
            }
            else->{
                return true
            }
        }

        return false
    }

    fun invoiceList(): Flow<PagingData<InvoiceData>> {
        val data = Pager(PagingConfig(
            pageSize = 5
        ),
            pagingSourceFactory = { InvoicePagingSource(invoiceUseCase) }
        ).flow

        return data
    }

    fun searchList(id : String?,billNo: String?,fromDate: String,toDate: String) = Pager(
        PagingConfig(
            pageSize = 5
        ),
        pagingSourceFactory = {
            InvoiceSearchSource(
                invoiceUseCase,
                id?:"",
                billNo?:"",
                fromDate,
                toDate
            )
        }
    ).flow.cachedIn(viewModelScope)


    fun getSpecificInvoiceDetails(invoiceId : Int): LiveData<Resource<Response<ApiSpecificInvoice>>> {
        return apiInvoiceUseCase.getSpecificInvoiceDetails(invoiceId)
    }

    /**Set Total Amount In invoiceTotal For showing in Total XML**/
    fun getInvoiceTotal() {
        invoiceTotal.value = invoiceUseCase.getInvoiceTotal().toString()
    }

    fun totalAmount() = invoiceUseCase.getTotal()


    /**Delete Invoice From Server**/
    fun deleteInvoice(invoice : InvoiceData) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = invoiceUseCase.deleteWebInvoice(invoice)
                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.toString()))
        }
    }

    fun deleteInvoiceFromList(invoice : InvoiceData) = invoiceUseCase.deleteInvoiceFromList(invoice)

    fun getLocalInvoiceList() = invoiceUseCase.invoiceList()

//    suspend fun searchInvoice(
//        customerId: String?,
//        fromDate: String,
//        toDate: String,
//        billNo: String?
//    ) = invoiceUseCase.searchInvoice(customerId?:"",fromDate,toDate,billNo?:"")



    fun setInvoiceList(list: List<InvoiceData>) = invoiceUseCase.setInvoiceUseCase(list)
    fun getCustomer(mobile : String?) = viewModelScope.launch {
       customer.value =  customerUseCase.getReceiptCustomer(mobile?:"")
    }


}
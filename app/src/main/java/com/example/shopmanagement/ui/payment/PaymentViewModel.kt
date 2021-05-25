package com.example.shopmanagement.ui.payment

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.shopmanagement.data.models.Payment
import com.example.shopmanagement.data.models.Supplier
import com.example.shopmanagement.data.models.api.payment.PaymentList
import com.example.shopmanagement.data.repositories.paging.PaymentSearchSource
import com.example.shopmanagement.data.repositories.paging.PaymentSource
import com.example.shopmanagement.domain.api.ApiUseCase
import com.example.shopmanagement.domain.PaymentUseCase
import com.example.shopmanagement.domain.SupplierUseCase
import com.example.shopmanagement.ui.utils.Event
import com.example.shopmanagement.ui.utils.FormateDate
import com.example.shopmanagement.ui.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val paymentUseCase: PaymentUseCase,
    private val supplierUseCase: SupplierUseCase,
    private val apiUseCase: ApiUseCase
) : ViewModel() {
    val payableAmount  = MutableLiveData<String?>()
    val discount = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    var supplierDetails = MutableLiveData<Supplier>()
    var afterDiscountAmount = MutableLiveData<Double>()
    var payment = MutableLiveData<PaymentList>()
    var customerNameOrMobile = MutableLiveData<String>()
    var fromEditBtn = false
    var editPaymentId = 0

    var totalAmount = MutableLiveData<String>()
    var totalDiscount = MutableLiveData<String>()
    var totalOfTotal = MutableLiveData<String>()

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>>
        get() = _errorMessage

    private val _paymentList = MutableLiveData<List<Payment>>()
    val paymentList : LiveData<List<Payment>>
        get() = _paymentList

    private val _navigate = MutableLiveData<Event<Boolean>>()
    val navigate: LiveData<Event<Boolean>>
        get() = _navigate

    //Getting List Of Payment From Database
    fun getListOfPayment(){
        viewModelScope.launch {
            _paymentList.value = paymentUseCase.getListOfPayments()

            totalAmount()
            totalDiscount()
            totalOfTotal()
        }

    }

    fun searchValidation(): Boolean {
        _errorMessage.value = when{
            customerNameOrMobile.value.isNullOrEmpty()->{
                Event("Search Field is Empty!")
            }
            else->{
                return true
            }
        }

        return false
    }

    fun insertLocalPayment(){
        if(afterDiscountAmount.value!!.toString().isNotEmpty()){
            viewModelScope.launch {
                if(!fromEditBtn){
                    updateSupplierBalance()
                    insertPayment()
                }else{
                    updatePayment()
                }


            }
        }

    }

    private suspend fun updatePayment() {
            payableAmount.value?.let { amount->
                discount.value?.let { discount->
                    afterDiscountAmount.value?.let { total->
                        paymentUseCase.updatePayment(amount.toDouble(),discount.toDouble(),total,editPaymentId)
                    }
                }
            }

    }

    private suspend fun insertPayment() {
        paymentUseCase.insertPayment(
            Payment(
                0,
                0,
                0,
                supplierDetails.value?.id!!,
                0,
                0,
                "0",
                Calendar.getInstance().time,
                Calendar.getInstance().time,
                payableAmount.value!!.toDouble(),
                discount.value?.toDouble()?:0.0,
                afterDiscountAmount.value?:0.0,
                description.value?:"Empty Description",
                Calendar.getInstance().time,
                Calendar.getInstance().time,
                Calendar.getInstance().time
            )
        )
    }

    private suspend fun updateSupplierBalance() {
        afterDiscountAmount.value?.let {total ->
            supplierUseCase.updateSupplierBalance(supplierDetails.value!!.id,supplierDetails.value!!.openingBalance - total)
        }
    }

    fun calculateDue(amount : Double, discount : Double) {
        afterDiscountAmount.value = amount + discount
    }

    fun getSupplier() : LiveData<List<String>> = supplierUseCase.getSupplierName()

    fun totalAmount(){
        totalAmount.value = apiUseCase.getPaymentTotalAmount().toString()
    }

    private fun totalDiscount(){
        totalDiscount.value = paymentUseCase.totalDiscount().toString()
    }

    private fun totalOfTotal(){
        totalOfTotal.value = paymentUseCase.total().toString()
    }

    fun onPaymentItemClick(payment: PaymentList) {
        editPaymentId = payment.id
        this.payment.value = payment
//        getSupplierById(payment.supplierId)
    }

    private fun getSupplierById(supplierId: Int) {
        viewModelScope.launch {
            supplierDetails.value = supplierUseCase.getSupplierById(supplierId)
        }
    }

    fun getSupplierByName(name : String) {
        viewModelScope.launch {
            supplierDetails.value = supplierUseCase.getSupplierDetails(name)
        }

    }

    fun paymentList() = Pager(
        PagingConfig(
            pageSize = 5
        ),
        pagingSourceFactory = {
            PaymentSource(
                apiUseCase
            )
        }
    ).flow.cachedIn(viewModelScope)

    fun searchPayment(mobile : String?, fromDate: String,toDate: String) = Pager(
        PagingConfig(
            pageSize = 5
        ),
        pagingSourceFactory = {PaymentSearchSource(
            apiUseCase,mobile,fromDate,toDate
        )}
    ).flow.cachedIn(viewModelScope)

//    fun getApiPaymentList() = liveData(Dispatchers.IO) {
//        emit(Resource.loading(data = null))
//        try {
//            emit(
//                Resource.success(
//                    data = apiUseCase.getUserPaymentList(page)
//                )
//            )
//        }catch (exception : Exception){
//            emit(Resource.error(data = null, message = exception.toString()))
//        }
//    }


    fun insertApiPayment(insertDate : String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                if(!fromEditBtn){
                    Resource.success(
                        data = apiUseCase.insertPayment(
                            Payment(
                                0,
                                0,
                                0,
                                supplierDetails.value?.id!!,
                                0,
                                0,
                                "0",
                                Calendar.getInstance().time,
                                Calendar.getInstance().time,
                                afterDiscountAmount.value?.toDouble()?:0.0,
                                discount.value?.toDouble()?:0.0,
                                payableAmount.value?.toDouble()?:0.0,
                                description.value?:"",
                                Calendar.getInstance().time,
                                Calendar.getInstance().time,
                                Calendar.getInstance().time

                            )
                        )
                    )
                }else{
                    Resource.success(
                        data = apiUseCase.editPayment(
                            Payment(
                                editPaymentId,
                                0,
                                0,
                                supplierDetails.value?.id!!,
                                0,
                                0,
                                "0",
                                FormateDate.formatStringToDate(insertDate),
                                Calendar.getInstance().time,
                                afterDiscountAmount.value?.toDouble()?:0.0,
                                discount.value?.toDouble()?:0.0,
                                payableAmount.value?.toDouble()?:0.0,
                                description.value?:"",
                                Calendar.getInstance().time,
                                Calendar.getInstance().time,
                                Calendar.getInstance().time

                            )
                        )
                    )
                }

            )
        }catch (exception : Exception){
            emit(Resource.error(data = null, message = exception.toString()))
        }
    }

    fun setSupplier(payment: PaymentList) {
        this.payment.value = payment
        editPaymentId = payment.id
        viewModelScope.launch {
            supplierDetails.value = supplierUseCase.getSupplierById(payment.supplier_id)
        }
    }

    fun deletePayment(payment: PaymentList) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = apiUseCase.deletePayment(payment)
                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.toString()))
        }
    }

    fun deleteFromLocal(payment: PaymentList) = apiUseCase.deleteLocalPayment(payment)

    fun getPaymentList() = apiUseCase.getPaymentList()

//    fun searchPayment(
//        search: String,
//        fromDate: String,
//        toDate: String
//    ) = apiUseCase.searchPayment(search,fromDate,toDate)
//    fun setPaymentList(data: List<PaymentList>) = apiUseCase.setPaymentList(data)

    fun editPayment() {
        payment.value?.let {
            setSupplier(it)
        }

    }

    fun clearEditPaymentData() {
        supplierDetails.value = null
        fromEditBtn = false
        payment.value = null
    }
}
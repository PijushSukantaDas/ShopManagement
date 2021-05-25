package com.example.shopmanagement.ui.receipt

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.shopmanagement.data.models.Customer
import com.example.shopmanagement.data.models.Receipt
import com.example.shopmanagement.data.models.api.receipt.DataList
import com.example.shopmanagement.data.repositories.paging.ReceiptSearchSource
import com.example.shopmanagement.data.repositories.paging.ReceiptSource
import com.example.shopmanagement.domain.api.ApiUseCase
import com.example.shopmanagement.domain.CustomerUseCase
import com.example.shopmanagement.domain.ReceiptUseCase
import com.example.shopmanagement.ui.utils.Event
import com.example.shopmanagement.ui.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ReceiptViewModel @Inject constructor(
    private val receiptUseCase: ReceiptUseCase,
    private val customerUseCase: CustomerUseCase,
    private val apiUseCase: ApiUseCase
    ) : ViewModel() {

    /**supplier_receipt_fragment XML AMount , Discount , Total and Description Field Bind With these three variable**/
    val payableAmount  = MutableLiveData<String>()
    val discount = MutableLiveData<String?>()
    var afterDiscountAmount = MutableLiveData<Double>()
    val description = MutableLiveData<String>()
    var customerNameOrMobile = MutableLiveData<String>()
    var searchReceiptList = MutableLiveData<List<DataList>>()


    var receipt = MutableLiveData<DataList>()
    var editReceiptId = 0


    var totalAmount = MutableLiveData<String>()
    var totalDiscount = MutableLiveData<String>()
    var totalOfTotal = MutableLiveData<String>()

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>>
        get() = _errorMessage


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

    /**Flag For Insuring Navigation From Edit Receipt of General Receipt Button**/
    var fromEdit = false
    var receiptId = 0


    /**List Of Receipt**/
    private val _receiptList = MutableLiveData<List<Receipt>>()
    val receiptList : LiveData<List<Receipt>>
            get() = _receiptList

    /**Customer Data**/
   val customerDetails = MutableLiveData<Customer>()



    /**Navigate When navigate True**/
    private val _navigate = MutableLiveData<Event<Boolean>>()
    val navigate: LiveData<Event<Boolean>>
        get() = _navigate


    /**Searching Receipt using Receipt Id**/
    fun searchReceipt(id: String?,fromDate: String,toDate: String) = Pager(
        PagingConfig(
            pageSize = 5
        ),
        pagingSourceFactory = {
            ReceiptSearchSource(
                apiUseCase,id?:"",fromDate,toDate
            )
        }
    ).flow.cachedIn(viewModelScope)

    //Getting List Of Receipt From Database
    fun getListOfReceipt(){
        viewModelScope.launch {
            _receiptList.value = receiptUseCase.getAllReceipt()

            totalAmount()
            totalDiscount()
            totalOfTotal()
        }

    }

    fun insertOrUpdateReceipt(){

        if(afterDiscountAmount.value!!.toString().isNotEmpty()){
            viewModelScope.launch {
                if(!fromEdit){
                    updateCustomerBalance()
                    insertReceipt()
                }else{
                    updateReceipt()
                }

            }
        }

    }

    private suspend fun updateReceipt() {
        payableAmount.value?.let { amount->
            discount.value?.let { discount->
                afterDiscountAmount.value?.let { total->
                    receiptUseCase.updateReceipt(amount.toDouble(),discount.toDouble(),total,editReceiptId)
                }

            }

        }
    }

    private suspend fun insertReceipt() {
        receiptUseCase.insertReceipt(
            Receipt(
                0,
                0,
                1,
                "0",
                0,
                0,
                customerDetails.value?.id!!,
                0,
            "0",
                Calendar.getInstance().time,
                Calendar.getInstance().time,
                Calendar.getInstance().time,
                0.0,
                0.0,
                0.0,
                payableAmount.value?.toDouble()?:0.0,
                discount.value?.toDouble()?:0.0,
                afterDiscountAmount.value?:0.0,
                description.value?:"Empty Description",
                Calendar.getInstance().time,
                Calendar.getInstance().time,
                Calendar.getInstance().time
            )
        )
    }

    private suspend fun updateCustomerBalance() {
        afterDiscountAmount.value?.let {total ->
            customerUseCase.updateBalance(customerDetails.value!!.id,customerDetails.value!!.customerOpeningBalance - total)
        }

    }

    fun calculateDue(amount : Double, discount : Double) {
        afterDiscountAmount.value = amount + discount
    }

    fun totalAmount(){
        totalAmount.value = apiUseCase.getReceiptTotalAmount().toString()
    }

    fun totalDiscount(){
        totalDiscount.value = receiptUseCase.totalDiscount().toString()
    }

    private fun totalOfTotal(){
        totalOfTotal.value = receiptUseCase.total().toString()
    }

    fun onReceiptItemClick(receipt: DataList) {
        editReceiptId = receipt.id
        this.receipt.value = receipt
//        getCustomerById(receipt.customer_id)

    }
    private fun getCustomerById(customerId : Int) {
        viewModelScope.launch {
            customerDetails.value = customerUseCase.getCustomerById(customerId)
        }
    }

    fun getCustomer(customerName : String) {
        viewModelScope.launch {
            customerDetails.value = customerUseCase.getReceiptCustomer(customerName)
        }
    }

    fun getCustomerServerDetails(id: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = apiUseCase.getCustomerDetails(id)
                )
            )
        }catch (exception : Exception){
            emit(Resource.error(data = null,message = exception.toString()))
        }
    }

    fun getCustomerName() : LiveData<List<String>> = customerUseCase.getCustomerName()

//    fun getUserReceiptList() = liveData(Dispatchers.IO) {
//        emit(Resource.loading(data = null))
//        try {
//            emit(
//                Resource.success(
//                    data = apiUseCase.getUserReceiptList(page)
//                )
//            )
//        }catch (exception : Exception){
//            emit(Resource.error(data = null, message = exception.toString()))
//        }
//    }

    fun receiptList() = Pager(
        PagingConfig(
            pageSize = 5
        ),
        pagingSourceFactory = {
            ReceiptSource(
                apiUseCase
            )
        }
    ).flow.cachedIn(viewModelScope)

    fun insertOrUpdateApiReceipt() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            if(!fromEdit){
                emit(
                    Resource.success(
                        Resource.success(
                            data = apiUseCase.insertReceipt(
                                Receipt(
                                    0,
                                    0,
                                    0,
                                    "0",
                                    0,
                                    0,
                                    customerDetails.value?.id?:0,
                                    0,
                                    "0",
                                    Calendar.getInstance().time,
                                    Calendar.getInstance().time,
                                    Calendar.getInstance().time,
                                    0.0,
                                    0.0,
                                    0.0,
                                    payableAmount.value?.toDouble()?:0.0,
                                    discount.value?.toDouble()?:0.0,
                                    afterDiscountAmount.value?:0.0,
                                    "0",
                                    Calendar.getInstance().time,
                                    Calendar.getInstance().time,
                                    Calendar.getInstance().time
                                )
                            )
                        )
                    )
                )
            }else{
                emit(
                    Resource.success(
                        Resource.success(
                            data = apiUseCase.updateReceipt(
                                Receipt(
                                    receiptId,
                                    0,
                                    0,
                                    "0",
                                    0,
                                    0,
                                    customerDetails.value?.id?:0,
                                    0,
                                    "0",
                                    Calendar.getInstance().time,
                                    Calendar.getInstance().time,
                                    Calendar.getInstance().time,
                                    0.0,
                                    0.0,
                                    0.0,
                                    payableAmount.value?.toDouble()?:0.0,
                                    discount.value?.toDouble()?:0.0,
                                    afterDiscountAmount.value?:0.0,
                                    "0",
                                    Calendar.getInstance().time,
                                    Calendar.getInstance().time,
                                    Calendar.getInstance().time
                                )
                            )
                        )
                    )
                )
            }

        }catch (exception : Exception){
            Log.i("Receipt Error","$exception")
            emit(Resource.error(data = null , message = exception.toString()))
        }
    }

    fun setReceipt(receipt : DataList) {
        this.receipt.value = receipt
        receiptId = receipt.id
        viewModelScope.launch {
            customerDetails.value = customerUseCase.getCustomerById(receipt.customer_id)
        }
    }

    fun deleteReceipt(receipt: DataList) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = apiUseCase.deleteReceipt(receipt)
                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.toString()))
        }
    }

    fun deleteFormLocalList(receipt: DataList) = apiUseCase.deleteReceiptLocal(receipt)
    fun getReceiptList()  = apiUseCase.getReceiptList()

    fun setReceiptList(data: List<DataList>) = apiUseCase.setReceiptList(data)

    fun editReceipt() {
        fromEdit = true
        receipt.value?.let {
            setReceipt(it)
        }
    }

    fun clearEditReceiptData(){
        receipt.value = null
        customerDetails.value = null
        fromEdit = false
    }

}
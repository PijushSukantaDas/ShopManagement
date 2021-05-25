package com.example.shopmanagement.ui.customer

import androidx.lifecycle.*
import com.example.shopmanagement.domain.api.ApiUseCase
import com.example.shopmanagement.domain.CustomerUseCase
import com.example.shopmanagement.data.models.Customer
import com.example.shopmanagement.data.models.api.customer.Data
import com.example.shopmanagement.ui.utils.Event
import com.example.shopmanagement.ui.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(private val customerUseCase: CustomerUseCase, private val apiUseCase: ApiUseCase) : ViewModel() {

    lateinit var customerEdit : Customer
    var customerId = 0
    var editCustomerFlag = false
    val customerName = MutableLiveData<String>()
    val customerAddress = MutableLiveData<String>()
    val customerMobile = MutableLiveData<String>()
    val customerVat = MutableLiveData<String>()
    val customerEmail = MutableLiveData<String>()
    val customerNid = MutableLiveData<String>()
    val customerGender = MutableLiveData<String>()
    val customerInitialBalance = MutableLiveData<String>()
    val customerBillType = MutableLiveData<String>()

    private val _isCustomerEmpty = MutableLiveData<Boolean>()
    val isCustomerEmpty : LiveData<Boolean>
        get() = _isCustomerEmpty

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage : LiveData<Event<String>>
        get() = _errorMessage

    private val _navigate = MutableLiveData<Event<Boolean>>()
    val navigate : LiveData<Event<Boolean>>
        get() = _navigate

    fun createLocalCustomer(customerResponse : Data?) {

        customerResponse?.let {
            if(createCustomerValidation()) {

                viewModelScope.launch {
                    customerUseCase.insertCustomer(
                        Customer(
                            customerResponse.id,
                            customerResponse.name,
                            customerResponse.address,
                            customerResponse.mobile,
                            "1",
                            customerNid.value?:"0",
                            customerVat.value?:"0",
                            customerEmail.value?:"null",
                            customerResponse.opening_balance,
                            customerResponse.receipt_type.toString()
                        )
                    )
                }
            }else{
                _navigate.value = Event(false)
            }
        }


    }


    private fun createCustomerValidation(): Boolean {
        return when {
            customerName.value.isNullOrEmpty() -> {
                _errorMessage.value = Event("Empty Name!")
                false
            }
            customerAddress.value.isNullOrEmpty() -> {
                _errorMessage.value = Event("Empty Address Field")
                false
            }
            customerMobile.value.isNullOrEmpty()->{
                _errorMessage.value = Event("Empty Mobile Field")
                false
            }

            else ->{
                true
            }
        }
    }

    fun getGender() : List<String> = customerUseCase.getGender()

    fun onGenderItemClicked(position: Int): String {
        return customerUseCase.onGenderItemClicked(position)
    }

    fun getReceiptType() : List<String> = customerUseCase.getReceiptType()


    fun createUser() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                if(!editCustomerFlag){
                    Resource.success(data = apiUseCase.createCustomer(
                        Customer(
                            0,
                            customerName.value?:"NO Name",
                            customerAddress.value?:"No Address",
                            customerMobile.value?:"No Mobile",
                            customerGender.value?:"Unknown",
                            customerNid.value?:"unknown",
                            customerVat.value?:"unknown",
                            customerEmail.value?:"unknown",
                            customerInitialBalance.value?.toDouble()?:0.0,
                            "1"
                        )))
                }else{
                    Resource.success(data = apiUseCase.updateCustomer(
                        Customer(
                            customerEdit.id,
                            customerName.value?:"NO Name",
                            customerAddress.value?:"No Address",
                            customerMobile.value?:"No Mobile",
                            customerGender.value?:"Unknown",
                            customerNid.value?:"null",
                            customerVat.value?:"null",
                            customerEmail.value?:"null",
                            customerInitialBalance.value?.toDouble()?:0.0,
                            "1"
                        )
                    ))
                }
            )
        } catch (exception: Exception) {
            Timber.d("$exception")
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun setEditCustomerField(customer: Customer) {
        customerEdit = customer
        customerName.value = customer.customerName
        customerAddress.value = customer.customerAddress
        customerMobile.value = customer.customerMobile
        customerVat.value = customer.customerVat?:""
        customerEmail.value = customer.customerEmail?:""
        customerNid.value = customer.customerNid?:""
        customerGender.value = if(customer.customerGender == "1"){
            "Male"
        }else{
            "Female"
        }
        customerInitialBalance.value = customer.customerOpeningBalance.toString()
        customerBillType.value = customer.customerReceiptType?:""
    }

    fun clearFieldData() {
        customerName.value = ""
        customerAddress.value = ""
        customerMobile.value = ""
        customerVat.value = ""
        customerEmail.value = ""
        customerNid.value = ""
        customerGender.value = ""
        customerInitialBalance.value = ""
        customerBillType.value = ""
    }

}
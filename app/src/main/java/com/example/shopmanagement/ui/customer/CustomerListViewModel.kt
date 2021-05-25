package com.example.shopmanagement.ui.customer

import androidx.lifecycle.*
import com.example.shopmanagement.data.models.Customer
import com.example.shopmanagement.domain.CustomerUseCase
import com.example.shopmanagement.ui.utils.Event
import com.example.shopmanagement.ui.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerListViewModel @Inject constructor(
    private val customerUseCase: CustomerUseCase
) : ViewModel() {

    var customerList : LiveData<List<Customer>> = customerUseCase.getAllCustomer()

    var searchString = MutableLiveData<String>()
    var searchMobile = MutableLiveData<String>()

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>>
        get() = _errorMessage

    private var _searchCustomerList = MutableLiveData<List<Customer>>()
    val searchCustomerList : LiveData<List<Customer>>
        get() = _searchCustomerList


    fun deleteLocalCustomer(customer: Customer) {
        viewModelScope.launch {
            customerUseCase.deleteCustomer(customer)

        }
    }

    fun searchValidation(): Boolean {
        _errorMessage.value = when{
            searchString.value.isNullOrEmpty() && searchMobile.value.isNullOrEmpty()->{
                Event("Search Field is Empty!")
            }
            else->{
                return true
            }
        }

        return false
    }

    fun deleteCustomer(customer: Customer) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = customerUseCase.deleteApiCustomer(customer)
                )
            )
        }catch (exception : Exception){
            emit(Resource.error(data = null,message = exception.toString()))
        }
    }

    fun searchCustomer() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = customerUseCase.searchCustomer(searchString.value?:"",searchMobile.value?:"")
                )
            )
        }catch (exception : Exception){
            emit(Resource.error(data = null,message = exception.toString()))
        }
    }

    fun searchFromLocal(mobile: String) = viewModelScope.launch {
        _searchCustomerList.value = customerUseCase.searchLocalCustomer(mobile)
    }

}
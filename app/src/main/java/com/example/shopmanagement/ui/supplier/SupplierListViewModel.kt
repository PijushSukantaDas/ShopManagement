package com.example.shopmanagement.ui.supplier

import androidx.lifecycle.*
import com.example.shopmanagement.data.models.Supplier
import com.example.shopmanagement.data.models.api.supplier.search.ApiSupplierSearch
import com.example.shopmanagement.domain.SupplierUseCase
import com.example.shopmanagement.ui.utils.Event
import com.example.shopmanagement.ui.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SupplierListViewModel @Inject constructor(private val supplierUseCase: SupplierUseCase) : ViewModel(){

    var supplierList : LiveData<List<Supplier>> = supplierUseCase.getAllSupplier()
    var searchString  = MutableLiveData<String>()
    var searchMobile = MutableLiveData<String>()
    var searchedSupplierList = MutableLiveData<List<Supplier>>()

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>>
        get() = _errorMessage

    private val _searchResponse = MutableLiveData<Resource<Response<ApiSupplierSearch>>>()
    val searchResponse : LiveData<Resource<Response<ApiSupplierSearch>>>
        get() = _searchResponse

    fun deleteSupplier(supplier : Supplier) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = supplierUseCase.deleteApiSupplier(supplier)
                )
            )
        }catch (exception : Exception){
            emit(Resource.error(data = null,message = exception.toString()))
        }
    }

    fun deleteLocalSupplier(supplier : Supplier) = viewModelScope.launch {
        supplierUseCase.deleteLocalSupplier(supplier)
    }

    fun searchSupplier() = supplierUseCase.searchSupplier(searchString.value?:"",searchMobile.value?:"")


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


    fun searchSupplierFromLocal(id: Int) = viewModelScope.launch {
        searchedSupplierList.value = supplierUseCase.getSearchedSupplier(id)
    }
}
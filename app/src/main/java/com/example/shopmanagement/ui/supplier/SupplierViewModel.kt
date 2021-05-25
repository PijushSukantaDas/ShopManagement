package com.example.shopmanagement.ui.supplier

import android.net.Uri
import androidx.lifecycle.*
import com.example.shopmanagement.data.models.Supplier
import com.example.shopmanagement.data.models.api.supplier.ApiSupplier
import com.example.shopmanagement.data.models.api.supplier.Data
import com.example.shopmanagement.domain.api.ApiUseCase
import com.example.shopmanagement.domain.SupplierUseCase
import com.example.shopmanagement.ui.utils.Event
import com.example.shopmanagement.ui.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SupplierViewModel @Inject constructor(
    private val supplierUseCase: SupplierUseCase,
    private val apiUseCase: ApiUseCase
) : ViewModel(){

    lateinit var editSupplier : Supplier
    var editSupplierFlag = false
    var imageUri: Uri? = null
    val companyName = MutableLiveData<String>()
    val ownerName = MutableLiveData<String>()
    val contactPersonName = MutableLiveData<String>()
    val address = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val mobile = MutableLiveData<String>()
    val tinNo = MutableLiveData<String>()
    val taxNo = MutableLiveData<String>()
    val vatNo = MutableLiveData<String>()
    val bstiNo = MutableLiveData<String>()
    val companyLogo = MutableLiveData<String>()
    val openingBalance = MutableLiveData<String>()

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage : LiveData<Event<String>>
        get() = _errorMessage

    private val _navigate = MutableLiveData<Event<Boolean>>()
    val navigate : LiveData<Event<Boolean>>
        get() = _navigate

    private val _imagePickerEvent = MutableLiveData<Event<Any>>()
    val imagePickerEvent : LiveData<Event<Any>>
        get() = _imagePickerEvent

    private val _resource = MutableLiveData<Resource<Response<ApiSupplier>>>()
    val resource : LiveData<Resource<Response<ApiSupplier>>>
        get() =  _resource

    fun createLocalCustomer(data: Data?) {
        data?.let {
            if(supplierValidation()){
                viewModelScope.launch {
                    data?.let {
                        supplierUseCase.createSupplier(
                            data.id,
                            data.name,
                            data.owner_name,
                            data.contact_person_name,
                            data.tin_no,
                            data.tax_no,
                            data.vat_no,
                            data.bsti_no,
                            data.mobile,
                            data.address,
                            data.email,
                            imageUri?.toString() ?: "",
                            data.opening_balance.toDouble(),
                            1,
                            Calendar.getInstance().getTime(),
                            Calendar.getInstance().getTime()
                        )
                    }

                }

            }
        }

    }


    private fun supplierValidation(): Boolean {
        return when {
            companyName.value.isNullOrEmpty() -> {
                _errorMessage.value = Event("Empty Company Name!!")
                false
            }
            contactPersonName.value.isNullOrEmpty()->{
                _errorMessage.value = Event("Empty Contact Person Name!!")
                false
            }
            address.value.isNullOrEmpty()->{
                _errorMessage.value = Event("Empty Address!!")
                false
            }
            mobile.value.isNullOrEmpty()->{
                _errorMessage.value = Event("Empty mobile!!")
                false
            }

            else -> {
                true
            }
        }
    }

    fun onImageSelectClicked() {
        _imagePickerEvent.value = Event(Any())
    }


    fun createSupplier() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                if(!editSupplierFlag){
                    Resource.success(data = apiUseCase.createSupplier(
                        Supplier(
                            0,
                            companyName.value?:"No Name",
                            ownerName.value?:"No Owner",
                            contactPersonName.value?:"No Contact Person",
                            tinNo.value?:"null",
                            taxNo.value?:"null",
                            vatNo.value?:"null",
                            bstiNo.value?:"null",
                            mobile.value?:"null",
                            address.value?:"null",
                            email.value?:"null",
                            "",
                            openingBalance.value?.toDouble()?:0.0,
                            1,
                            Calendar.getInstance().time,
                            Calendar.getInstance().time
                        )
                    ))
                }else{
                    Resource.success(data = apiUseCase.updateSupplier(
                        Supplier(
                            editSupplier.id,
                            companyName.value?:"No Name",
                            ownerName.value?:"No Owner",
                            contactPersonName.value?:"No Contact Person",
                            tinNo.value?:"null",
                            taxNo.value?:"null",
                            vatNo.value?:"null",
                            bstiNo.value?:"null",
                            mobile.value?:"null",
                            address.value?:"null",
                            email.value?:"null",
                            "",
                            openingBalance.value?.toDouble()?:0.0,
                            1,
                            Calendar.getInstance().time,
                            Calendar.getInstance().time
                        )
                    ))
                }
            )
        } catch (exception: Exception) {
            Timber.d("$exception")
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun clearSupplierData(){
        companyName.value = ""
        ownerName.value = ""
        contactPersonName.value = ""
        address.value = ""
        email.value = ""
        mobile.value = ""
        tinNo.value = ""
        taxNo.value = ""
        vatNo.value = ""
        bstiNo.value = ""
        openingBalance.value = ""
    }

    fun setSupplierInfo(supplier: Supplier) {
        editSupplier = supplier
        companyName.value = supplier.name
        ownerName.value = supplier.ownerName
        contactPersonName.value = supplier.contactPerson
        address.value = supplier.address
        email.value = supplier.email
        mobile.value = supplier.mobile
        tinNo.value = supplier.tinNo
        taxNo.value = supplier.taxNo
        vatNo.value = supplier.vatNo
        bstiNo.value = supplier.bstiNo
        openingBalance.value = supplier.openingBalance.toString()
    }


}
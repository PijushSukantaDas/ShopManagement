package com.example.shopmanagement.ui.product

import android.net.Uri
import androidx.lifecycle.*
import com.example.shopmanagement.data.models.Codes
import com.example.shopmanagement.data.models.Product
import com.example.shopmanagement.data.models.api.product.Data
import com.example.shopmanagement.domain.api.ApiUseCase
import com.example.shopmanagement.domain.CodesUseCase
import com.example.shopmanagement.domain.ProductUseCase
import com.example.shopmanagement.ui.utils.Event
import com.example.shopmanagement.ui.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productUseCase : ProductUseCase,
    private val codesUseCase: CodesUseCase,
    private val apiUseCase: ApiUseCase
) : ViewModel() {

    private var category: String = ""
    var editProductFlag = false
    lateinit var productEdit : Product
    var productId = 0

    val nameLiveData = MutableLiveData<String>()

    val sellingPriceLiveData = MutableLiveData<String>()

    val alertQuantityLiveData = MutableLiveData<String>()

    val codeLiveData = MutableLiveData<String>()

    val enaLiveData = MutableLiveData<String>()

    val descriptionLiveData = MutableLiveData<String>()

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>>
        get() = _errorMessage

    private val _imagePickerEvent = MutableLiveData<Event<Any>>()
    val imagePickerEvent: LiveData<Event<Any>>
        get() = _imagePickerEvent

    private val _navigate = MutableLiveData<Event<Boolean>>()
    val navigate: LiveData<Event<Boolean>>
        get() = _navigate

    var imageUri: Uri? = null




    fun createLocalProduct(data: Data?) {

        data?.let {

                viewModelScope.launch {
                    productUseCase.addProduct(
                        data.id,
                        data.name,
                        data.mrp,
                        data.category_id.toString(),
                        data.sub_category_id.toString(),
                        codeLiveData.value?:data.ena,
                        data.description,
                        data.imagefile,
                        data.alert_quantity
                    )

                    codesUseCase.insertCodes(
                        Codes(
                            0,
                            data.id,
                            codeLiveData.value ?: data.id.toString(),
                            Calendar.getInstance().time,
                            Calendar.getInstance().time,
                            Calendar.getInstance().time
                        )
                    )
                }

            }




    }

    private fun createProductFieldValidation(): Boolean {
        _errorMessage.value = when {
            nameLiveData.value.isNullOrEmpty() -> {
                Event("Empty Name")
            }
            sellingPriceLiveData.value.isNullOrEmpty() -> {
                Event("Empty Product Selling Price")
            }

            ((sellingPriceLiveData.value ?: "0").toInt() <= 0) -> {
                Event("Selling Price is not valid")
            }
            else -> {
                return true
            }
        }
        return false
    }

    fun onCategoryItemClicked(position: Int) {
        category = productUseCase.getCategoryByPosition(position)
    }

    fun getSubCategories(): List<String> = productUseCase.getSubCategories()

    fun onImageSelectClicked() {
        _imagePickerEvent.value = Event(Any())
    }

    fun createProduct() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                if(!editProductFlag){
                    Resource.success(data = apiUseCase.createProduct(
                        Product(
                            0,
                            nameLiveData.value?:"No Name",
                            sellingPriceLiveData.value?.toDouble()?:0.0,
                            "null",
                            "null",
                            codeLiveData.value?:"0",
                            descriptionLiveData.value?:"Null",
                            imageUri?.toString() ?: "",
                            alertQuantityLiveData.value?.toString()?.toInt()?:0
                        )

                    ))
                }else{
                    Resource.success(data = apiUseCase.updateProduct(
                        Product(
                            productEdit.id,
                            nameLiveData.value?:"No Name",
                            sellingPriceLiveData.value?.toDouble()?:0.0,
                            "null",
                            "null",
                            codeLiveData.value?:"0",
                            descriptionLiveData.value?:"Null",
                            imageUri?.toString() ?: "",
                            alertQuantityLiveData.value?.toInt()?:0
                        )

                    ))
                }
            )
        } catch (exception: Exception) {
            Timber.d("$exception")
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun clearEditProductData(){
        nameLiveData.value = ""
        sellingPriceLiveData.value = ""
        alertQuantityLiveData.value = ""
        codeLiveData.value = ""
        enaLiveData.value = ""
        descriptionLiveData.value = ""
    }

    fun editProductInfo(product: Product) {
        productId = product.id
        productEdit = product
        nameLiveData.value = product.name
        sellingPriceLiveData.value = product.sellingPrice.toString()
        alertQuantityLiveData.value = product.alertQuantity.toString()
        codeLiveData.value = product.code.toString()
        enaLiveData.value = ""
        descriptionLiveData.value = product.description?:""
    }

}
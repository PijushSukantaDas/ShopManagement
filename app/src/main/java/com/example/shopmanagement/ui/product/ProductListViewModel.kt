package com.example.shopmanagement.ui.product

import androidx.lifecycle.*
import com.example.shopmanagement.data.models.Product
import com.example.shopmanagement.domain.ProductUseCase
import com.example.shopmanagement.ui.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(private val productUseCase: ProductUseCase) : ViewModel() {
    var productList : LiveData<List<Product>> = productUseCase.getAllProducts()
    var list  = MutableLiveData<ArrayList<Product>>()
    var product = MutableLiveData<String>()
    var productDetails = MutableLiveData<Product>()

    var searchProductString = MutableLiveData<String>()

    fun deleteProduct(product: Product) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = productUseCase.deleteApiProduct(product)
                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.toString()))
        }
    }

    fun searchApiProduct(search : String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = productUseCase.searchProduct( search)
                )

            )
        }catch (exception : Exception){
            emit(Resource.error(data = null,message = exception.toString()))
        }
    }

    fun searchProduct(id : Int) = viewModelScope.launch {
        list.value = ArrayList(productUseCase.getProductList(id))
    }

    fun deleteLocalProduct(id: Int) = viewModelScope.launch {
        productUseCase.deleteLocalProduct(id)
    }


}
package com.example.shopmanagement.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.shopmanagement.data.api.ApiHelper
import com.example.shopmanagement.data.models.Supplier
import com.example.shopmanagement.data.models.api.supplier.search.ApiSupplierSearch
import com.example.shopmanagement.data.repositories.SupplierRepository
import com.example.shopmanagement.ui.utils.Resource
import kotlinx.coroutines.Dispatchers
import retrofit2.Response
import java.util.*
import javax.inject.Inject

class SupplierUseCase @Inject constructor(

    private val supplierRepository: SupplierRepository,
    private val apiHelper: ApiHelper
) {

    suspend fun createSupplier(
        id:Int,
        name : String,
        ownerName : String,
        contactPerson : String,
        tinNo : String,
        taxNo : String,
        vatNo : String,
        bstiNo : String,
        mobile : String,
        address : String,
        email : String,
        image : String,
        openingBalance : Double,
        status : Int,
        createdAt : Date,
        updateAt : Date
    ){
        supplierRepository.addSupplierIfNotExistsOrUpdateIfChanged(
            id,
            name,
            ownerName,
            contactPerson,
            tinNo,
            taxNo,
            vatNo,
            bstiNo,
            mobile,
            address,
            email,
            image,
            openingBalance,
            status,
            createdAt,
            updateAt
        )
    }

    fun getAllSupplier() : LiveData<List<Supplier>> = supplierRepository.getAllSupplier()

    fun getSupplierName(): LiveData<List<String>>  = supplierRepository.getSupplierName()
    suspend fun getSupplierDetails(supplier : String) : Supplier = supplierRepository.getSupplierDetails(supplier)
    fun getSupplierDetailsPurchase(supplier: String) = supplierRepository.getSupplierDetailsPurchase(supplier)
    suspend fun getSupplierById(supplierId : Int): Supplier = supplierRepository.getSupplierById(supplierId)
    suspend fun updateSupplierBalance(supplierID : Int, balance : Double) = supplierRepository.updateSupplierBalance(supplierID,balance)

    suspend fun deleteApiSupplier(supplier: Supplier) = apiHelper.deleteSupplier(supplier)
    suspend fun deleteLocalSupplier(supplier: Supplier) = supplierRepository.deleteLocalSupplier(supplier)

    /**Search Supplier From Server**/
    fun searchSupplier(search: String, mobile: String): LiveData<Resource<Response<ApiSupplierSearch>>> = liveData(Dispatchers.IO) {
        emit(com.example.shopmanagement.ui.utils.Resource.loading(data = null))
        try {
            emit(
                com.example.shopmanagement.ui.utils.Resource.success(
                    data = apiHelper.searchSupplier(search,mobile)
                )
            )

        }catch (exception : Exception){
            emit(com.example.shopmanagement.ui.utils.Resource.error(data = null,message = exception.toString()))
        }
    }

    suspend fun getSearchedSupplier(id: Int) = supplierRepository.getSearchedSupplier(id)
}
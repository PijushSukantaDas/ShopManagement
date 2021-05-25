package com.example.shopmanagement.data.repositories

import androidx.lifecycle.LiveData
import com.example.shopmanagement.data.dao.ISupplierDao
import com.example.shopmanagement.data.models.Supplier
import java.util.*
import javax.inject.Inject

class SupplierRepository @Inject constructor(private val iSupplierDao: ISupplierDao) {

    suspend fun addSupplierIfNotExistsOrUpdateIfChanged(
        id:Int,
        name : String,
        ownerName : String?,
        contactPerson : String,
        tinNo : String?,
        taxNo : String?,
        vatNo : String?,
        bstiNo : String?,
        mobile : String,
        address : String,
        email : String,
        image : String?,
        openingBalance : Double?,
        status : Int,
        createdAt : Date,
        updateAt : Date
    ) : Pair<Supplier,Boolean>{
        val supplier = Supplier(
            id,
            name,
            ownerName?:"",
            contactPerson,
            tinNo?:"0",
            taxNo?:"0",
            vatNo?:"0",
            bstiNo?:"0",
            mobile?:"0",
            address?:"0",
            email,
            image?:"0",
            openingBalance?:0.0,
            status,
            createdAt,
            updateAt
        )

        val isAdded = iSupplierDao.addSupplierIfNotExistsOrUpdateIfChanged(supplier)

        return Pair(supplier, isAdded != 0L)
    }

    fun getAllSupplier() : LiveData<List<Supplier>> = iSupplierDao.getAllSupplier()

    fun getSupplierName(): LiveData<List<String>>  = iSupplierDao.getSupplierName()
    suspend fun getSupplierDetails(supplier: String) : Supplier = iSupplierDao.getSupplierDetails(supplier)
    fun getSupplierDetailsPurchase(supplier: String) = iSupplierDao.getSupplierDetailsForPurchase(supplier)
    suspend fun getSupplierById(supplierId : Int): Supplier = iSupplierDao.getSupplierById(supplierId)
    suspend fun updateSupplierBalance(supplierID: Int, balance: Double) = iSupplierDao.updateBalance(supplierID,balance)
    suspend fun deleteLocalSupplier(supplier: Supplier) = iSupplierDao.deleteSupplier(supplier)
    suspend fun getSearchedSupplier(id: Int) = iSupplierDao.getSearchedSupplier(id)

}
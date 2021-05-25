package com.example.shopmanagement.data.repositories


import androidx.lifecycle.liveData
import com.example.shopmanagement.data.api.ApiHelper
import com.example.shopmanagement.data.models.*
import com.example.shopmanagement.data.models.api.customer.ApiCustomer
import com.example.shopmanagement.data.models.api.customer.list.ApiCustomerList
import com.example.shopmanagement.data.models.api.product.list.ApiProductList
import com.example.shopmanagement.data.models.api.supplier.list.ApiSupplierList
import com.example.shopmanagement.domain.CustomerUseCase
import com.example.shopmanagement.ui.utils.Resource
import kotlinx.coroutines.Dispatchers
import retrofit2.Response
import timber.log.Timber
import java.text.SimpleDateFormat

import javax.inject.Inject

class ApiRepository @Inject constructor(
    private val apiHelper : ApiHelper,
    private val customerUseCase: CustomerUseCase,
    private val supplierRepository: SupplierRepository,
    private val productRepository: ProductRepository
) {

    suspend fun createCustomer(customer : Customer): Response<ApiCustomer> {
        val response = apiHelper.createCustomer(customer)
        Timber.d(response.toString())
        return response
    }

    suspend fun updateCustomer(customer : Customer) = apiHelper.updateCustomer(customer)

    suspend fun createSupplier(supplier : Supplier) = apiHelper.createSupplier(supplier)
    suspend fun updateSupplier(supplier : Supplier) = apiHelper.updateSupplier(supplier)


    suspend fun createProduct(product : Product) = apiHelper.createProduct(product)
    suspend fun updateProduct(product : Product) = apiHelper.updateProduct(product)

    suspend fun insertReceipt(receipt : Receipt) = apiHelper.insertReceipt(receipt)
    suspend fun insertPayment(payment : Payment) = apiHelper.insertPayment(payment)
    suspend fun insertInvoice(
        quantityList: List<Double>,
        totalProductPrice: List<Double>,
        productUnitPrice: List<Double>,
        productIdArray: List<Int>,
        id: Int,
        subTotal: Double,
        partialPaid: Double,
        dueAmount: Double,
        discount : Double,
        invoicePayment:Boolean,
        details : String
    ) = apiHelper.insertInvoice(
        quantityList,
        totalProductPrice,
        productUnitPrice,
        productIdArray,
        id,
        subTotal,
        partialPaid,
        dueAmount,
        discount,
        invoicePayment,
        details
    )

    suspend fun insertPurchase(
        quantityList: List<Double>,
        totalProductPrice: List<Double>,
        productUnitPrice: List<Double>,
        productIdArray: List<Int>,
        id: Int,
        subTotal: Double,
        partialPaid: Double,
        dueAmount: Double,
        discount : Double,
        invoicePayment : Boolean,
        details : String
    ) = apiHelper.insertPurchase(
        quantityList,
        totalProductPrice,
        productUnitPrice,
        productIdArray,
        id,
        subTotal,
        partialPaid,
        dueAmount,
        discount,
        invoicePayment,
        details
    )

    suspend fun updateInvoice(
        invoiceId : Int,
        quantityList: List<Double>,
        totalProductPrice: List<Double>,
        productUnitPrice: List<Double>,
        productIdArray: List<Int>,
        id: Int,
        subTotal: Double,
        partialPaid: Double,
        dueAmount: Double,
        discount : Double,
        invoicePayment : Boolean,
        details : String
    ) = apiHelper.updateInvoice(
        invoiceId,
        quantityList,
        totalProductPrice,
        productUnitPrice,
        productIdArray,
        id,
        subTotal,
        partialPaid,
        dueAmount,
        discount,
        invoicePayment,
        details
    )


    suspend fun updatePurchase(
        supplierId : Int,
        purchaseInvoiceId : Int,
        quantityList: List<Double>,
        totalProductPrice: List<Double>,
        productUnitPrice: List<Double>,
        productIdArray: List<Int>,
        id: Int,
        subTotal: Double,
        partialPaid: Double,
        dueAmount: Double,
        discount : Double,
        invoicePayment : Boolean,
        details : String
    ) = apiHelper.updatePurchase(
        supplierId,
        purchaseInvoiceId,
        quantityList,
        totalProductPrice,
        productUnitPrice,
        productIdArray,
        id,
        subTotal,
        partialPaid,
        dueAmount,
        discount,
        invoicePayment,
        details
    )

    suspend fun getCustomerDetails(customerId : Int) = apiHelper.getCustomerDetails(customerId)

    suspend fun getSupplierDetails(supplierId : Int) = apiHelper.getSupplierDetails(supplierId)


    suspend fun getProductDetails(productId : Int) = apiHelper.getProductDetails(productId)

    suspend fun getUserReceiptList(page: Int)= apiHelper.getUserReceiptList(page)

    suspend fun getUserPaymentList(page: Int) = apiHelper.getUserPaymentList(page)
    suspend fun updatePayment(payment: Payment) = apiHelper.updatePayment(payment)
    suspend fun updateReceipt(receipt: Receipt) = apiHelper.updateReceipt(receipt)
    suspend fun deleteReceipt(receiptId: Int) = apiHelper.deleteReceipt(receiptId)

    suspend fun deletePayment(paymentId : Int) = apiHelper.deletePayment(paymentId)

    suspend fun searchReceipt(page:Int,search: String, fromDate: String, toDate: String) = apiHelper.searchReceipt(page ,search,fromDate,toDate)

    suspend fun searchPayment(page: Int,search: String, fromDate: String, toDate: String) = apiHelper.searchPayment(page,search,fromDate,toDate)

    fun dateSearchReceipt(customerId: Int, fromDate: String, toDate: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = apiHelper.dateSearchReceipt(customerId,fromDate,toDate)
                )
            )
        }catch (exception : java.lang.Exception){
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    suspend fun productDateSearch(productId: Int, fromDate: String, toDate: String) = apiHelper.productDateSearch(productId,fromDate,toDate)
    suspend fun searchSupplierPurchase(id: Int, fromDate: String, toDate: String) = apiHelper.searchSupplierPurchase(id,fromDate,toDate)


    /**Storing Customer To Local Database Getting From Web Server**/
    suspend fun getUserCustomer() = storeCustomerToLocalDB(apiHelper.getAllCustomer())


    /**Storing to Local Database Customer**/
    private suspend fun storeCustomerToLocalDB(response: Response<ApiCustomerList>) {
        response.body()?.let {
            when(it.success)
            {
                200->{
                    it.data.map {customer->
                        customerUseCase.insertCustomer(
                            Customer(
                                customer.id,
                                customer.name,
                                customer.address,
                                customer.mobile,
                                "1",
                                customer.nid,
                                customer.vat_no,
                                customer.email,
                                customer.opening_balance.toDouble(),
                                customer.receipt_type.toString()
                            )
                        )
                    }
                }
                else->{

                }
            }
        }
    }

    /**Storing Supplier to Local Database Getting from Web server**/
    suspend fun getUserSupplier() = storeSupplierToLocalDB(apiHelper.getAllSupplier())

    /**Storing to Local Database Customer**/
    private suspend fun storeSupplierToLocalDB(allSupplier: Response<ApiSupplierList>) = allSupplier.body()?.let {
        when(it.success){
            200->{
                it.data.map { data->
                    supplierRepository.addSupplierIfNotExistsOrUpdateIfChanged(
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
                            data.imagefile,
                            data.opening_balance.toDouble(),
                            1,
                            SimpleDateFormat("yyyy-MM-dd").parse(data.created_at),
                            SimpleDateFormat("yyyy-MM-dd").parse(data.updated_at)

                    )

                }
            }
            else->{

            }
        }
    }

    /**Storing Supplier to Local Database Getting from Web server**/
    suspend fun getUserProduct() = storeProductToLocalDB(apiHelper.getAllProduct())

    /**Storing to Local Database Customer**/
    private suspend fun storeProductToLocalDB(allProduct: Response<ApiProductList>) = allProduct.body()?.let {

        when(it.success){
            200->{
                it.data.map {data->
                    productRepository.addProductIfNotExistsOrUpdateIfChanged(
                        data.id,
                        data.name,
                        data.mrp.toDouble(),
                        data.category_id,
                        data.sub_category_id,
                        data.code,
                        data.description,
                        data.imagefile,
                        data.alert_quantity
                    )
                }
            }
            else->{

            }
        }

    }

}
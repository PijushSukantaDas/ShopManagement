package com.example.shopmanagement.domain.api

import com.example.shopmanagement.data.models.*
import com.example.shopmanagement.data.models.api.customer.transaction.ApiTransaction
import com.example.shopmanagement.data.models.api.customer.transaction.Re
import com.example.shopmanagement.data.models.api.payment.list.ApiPaymentList
import com.example.shopmanagement.data.models.api.payment.PaymentList
import com.example.shopmanagement.data.models.api.product.details.ApiProductDetails
import com.example.shopmanagement.data.models.api.receipt.details.ApiReceiptDetails
import com.example.shopmanagement.data.models.api.receipt.DataList
import com.example.shopmanagement.data.models.api.supplier.details.ApiSupplierDetails
import com.example.shopmanagement.data.repositories.ApiRepository
import retrofit2.Response
import javax.inject.Inject

class ApiUseCase @Inject constructor(private val apiRepository : ApiRepository) {
    private var customerTransactionList : List<Re> = listOf()
    private var supplierTransactionList : List<com.example.shopmanagement.data.models.api.supplier.details.Re> = listOf()
    private var paymentList : ArrayList<PaymentList> = arrayListOf()
    private var receiptList : ArrayList<DataList> = arrayListOf()

    private var balanceList : ArrayList<Double> = arrayListOf()
    private var specificProductTransactionList : ArrayList<com.example.shopmanagement.data.models.api.product.details.Re> = arrayListOf()

    suspend fun createCustomer(customer : Customer) = apiRepository.createCustomer(customer)
    suspend fun updateCustomer(customer : Customer) = apiRepository.updateCustomer(customer)

    suspend fun createSupplier(supplier : Supplier) = apiRepository.createSupplier(supplier)
    suspend fun updateSupplier(supplier : Supplier) = apiRepository.updateSupplier(supplier)
    suspend fun getSupplierDetails(supplierId : Int): Response<ApiSupplierDetails> {
        supplierTransactionList = apiRepository.getSupplierDetails(supplierId).body()?.data?.res ?: listOf()
        return apiRepository.getSupplierDetails(supplierId)
    }

    suspend fun createProduct(product : Product) = apiRepository.createProduct(product)
    suspend fun updateProduct(product : Product) = apiRepository.updateProduct(product)
    suspend fun getProductDetails(productId : Int): Response<ApiProductDetails> {
        specificProductTransactionList = ArrayList(apiRepository.getProductDetails(productId).body()?.data?.res ?: listOf())
        return apiRepository.getProductDetails(productId)
    }

    suspend fun insertReceipt(receipt : Receipt) = apiRepository.insertReceipt(receipt)
    suspend fun getUserReceiptList(page: Int): Response<ApiReceiptDetails> {
        receiptList = ArrayList(apiRepository.getUserReceiptList(page).body()?.data?.receipts?.data ?: listOf())

        return apiRepository.getUserReceiptList(page)
    }

    suspend fun insertPayment(payment : Payment) = apiRepository.insertPayment(payment)

    suspend fun getUserPaymentList(page: Int): Response<ApiPaymentList> {
        paymentList = ArrayList(apiRepository.getUserPaymentList(page).body()?.data?.data ?: listOf())
        return apiRepository.getUserPaymentList(page)
    }


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
    ) = apiRepository.insertInvoice(
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
        invoicePayment:Boolean,
        details : String
    ) = apiRepository.updateInvoice(
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
        invoicePayment:Boolean,
        details : String
    ) = apiRepository.insertPurchase(
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
    ) = apiRepository.updatePurchase(
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


    suspend fun getCustomerDetails(customerId : Int): Response<ApiTransaction> {
        customerTransactionList = apiRepository.getCustomerDetails(customerId).body()?.data?.res?: listOf()
        return apiRepository.getCustomerDetails(customerId)
    }



    fun getCustomerBalance(): Double {
        var total = 0.0
        customerTransactionList.map {
            if(it.status == "receipt"){
                total -= it.amount
            }else{
                total += it.amount
            }

        }

        return total
    }

    fun totalPayment() : Double {
        var total = 0.0
        customerTransactionList.map {
            if(it.status == "receipt"){
                total += it.amount
            }

        }

        return total
    }

    fun getTotalInvoice(): Double {
        var total = 0.0
        customerTransactionList.map {
            if(it.status == "invoice"){
                total += it.amount
            }

        }

        return total
    }

    fun getSupplierBalance(): Double {
        var total = 0.0
        supplierTransactionList.map {
            if(it.status == "payment"){
                total -= it.amount
            }else{
                total += it.amount
            }

        }

        return total
    }

    fun getSupplierPayment(): Double {
        var total = 0.0
        supplierTransactionList.map {
            if(it.status == "payment"){
                total += it.amount
            }

        }

        return total
    }


    fun getSupplierInvoice() : Double{
        var total = 0.0
        supplierTransactionList.map {
            if(it.status == "purchase"){
                total += it.amount
            }

        }

        return total
    }

    /**Payment List Total Amount**/
    fun getPaymentTotalAmount(): Double {
        var total = 0.0

        paymentList.map {
            total += it.amount
        }

        return total
    }

    /**Receipt List Total Amount**/
    fun getReceiptTotalAmount(): Double {
        var total = 0.0

        receiptList.map {
            total += it.amount
        }

        return total
    }




    fun getCustomerBalanceList(): List<Re> {
        var total = 0.0
        customerTransactionList.map {
            when(it.status){
                "invoice"->{
                    total += it.amount
                    it.balance = total
                }
                else->{
                    total -= it.amount
                    it.balance = total
                }
            }
        }

        return customerTransactionList
    }

    fun getSupplierBalanceList(): List<com.example.shopmanagement.data.models.api.supplier.details.Re> {
        var total = 0
        supplierTransactionList.map {
            when(it.status){
                "purchase"->{
                    total += it.amount
                    it.balance = total
                }
                else->{
                    total -= it.amount
                    it.balance = total
                }
            }
        }
        return supplierTransactionList
    }

    suspend fun editPayment(payment: Payment) = apiRepository.updatePayment(payment)
    suspend fun updateReceipt(receipt: Receipt) = apiRepository.updateReceipt(receipt)
    suspend fun deleteReceipt(receipt: DataList) = apiRepository.deleteReceipt(receipt.id)
    suspend fun deletePayment(payment: PaymentList) = apiRepository.deletePayment(payment.id)
    fun deleteReceiptLocal(receipt: DataList) {
        receiptList.remove(receipt)
    }

    fun getReceiptList() = receiptList

    fun deleteLocalPayment(payment: PaymentList) {
        paymentList.remove(payment)
    }

    fun getPaymentList() = paymentList


    fun totalSpecificProductQuantity(): Int {
        var total = 0
        specificProductTransactionList.map {
            if(it.status == "sale"){
                total -= it.quantity
            }else{
                total += it.quantity
            }
        }

        return total
    }

    fun totalPurchaseProductQuantity(): Int {
        var total = 0
        specificProductTransactionList.map {
            if(it.status == "purchase"){
                total += it.quantity
            }
        }

        return total
    }

    suspend fun searchReceipt(
        page: Int,
        search: String,
        fromDate: String,
        toDate: String): Response<ApiReceiptDetails> {
        val response = apiRepository.searchReceipt(page,search,fromDate,toDate)
        receiptList = ArrayList(response.body()?.data?.receipts?.data?: listOf())
        return response
    }

    fun setReceiptList(data: List<DataList>) {
        receiptList = ArrayList(data)
    }

    suspend fun searchPayment(
        page: Int,
        search: String,
        fromDate: String,
        toDate: String
    ) = apiRepository.searchPayment(page,search,fromDate,toDate)

    fun setPaymentList(data: List<PaymentList>) {
        paymentList = ArrayList(data)
    }

    fun getDateSearchReceiptList(customerId: Int, fromDate: String, toDate: String) = apiRepository.dateSearchReceipt(customerId,fromDate,toDate)

    fun setBalanceList(res: List<Re>) {
       customerTransactionList = res
    }

    suspend fun productDateSearch(productId: Int, fromDate: String, toDate: String) = apiRepository.productDateSearch(productId,fromDate,toDate)
    suspend fun searchSupplierPurchase(id: Int, fromDate: String, toDate: String): Response<ApiSupplierDetails> {
        supplierTransactionList = apiRepository.searchSupplierPurchase(id,fromDate,toDate).body()?.data?.res ?: listOf()
        return apiRepository.searchSupplierPurchase(id,fromDate,toDate)
    }

    suspend fun getUserCustomer() = apiRepository.getUserCustomer()
    suspend fun getUserProduct() = apiRepository.getUserProduct()
    suspend fun getUserSupplier() = apiRepository.getUserSupplier()


}
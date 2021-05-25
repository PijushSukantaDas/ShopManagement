package com.example.shopmanagement.data.api

import android.content.Context
import com.example.shopmanagement.data.api.RetrofitInstanceKT.Companion.getRetrofitInstance
import com.example.shopmanagement.data.models.*
import com.example.shopmanagement.data.models.api.customer.ApiCustomer
import com.example.shopmanagement.data.models.api.purchase.PurchaseData
import com.example.shopmanagement.network.Url
import com.example.shopmanagement.ui.utils.ConstantKeys
import com.example.shopmanagement.ui.utils.FormateDate
import com.example.shopmanagement.ui.utils.Preference
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Response
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class ApiHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val category = Preference(context).getString("CATEGORY_ID")?.toInt()?:0
    private val userId = Preference(context).getInt("USER_ID")
    private val rememberToken = Preference(context).getString("REMEMBER_TOKEN")?:""

    var userIdMap = hashMapOf("user_id" to userId)
    private var rememberTokenMap = hashMapOf("remember_token" to rememberToken )


    /**Authentication**/
    suspend fun authenticate(
        mobile: String?,
        shopName: String,
        address: String,
        email: String?,
        name: String,
        expireDate: String,
        message: String,
        otp: Boolean,
        emailOtp: Boolean,
        status : Int
    ) = getRetrofitInstance(Url.licenseUrl).create(DataService::class.java)
        .validate(
            name,
            shopName,
            mobile?:"",
            ConstantKeys.EMPTY,
            email?:"",
            ConstantKeys.EMPTY,
            ConstantKeys.PROJECT_ID,
            mobile?:email!!,
            ConstantKeys.EMPTY,
            expireDate,
            "1",
            "Postally"+ FormateDate.formatDateToString(Date()),
            otp,
            message,
            emailOtp,
            status
        )

    /**Create User**/
    suspend fun createUser(userId:String,mobile: String,companyName:String,address: String,categoryId:String) = getRetrofitInstance(Url.BASE_URL)
        .create(DataService::class.java)
        .createUser(
            userId,
            mobile,
            companyName,
            categoryId
        )

    /**DASHBOARD**/
    suspend fun getDashBoardData() = getRetrofitInstance(Url.BASE_URL).create(DataService::class.java)
        .getDashBoardData(
            userIdMap,
            rememberTokenMap
        )

    suspend fun getShopCategory() = getRetrofitInstance(Url.BASE_URL).create(DataService::class.java)
        .getShopCategory()

    /** ---Product--- **/
    /**Create**/
    /**Update**/
    /**Delete**/

    /**Creating Product On Web Server**/
    suspend fun createProduct(product: Product) = getRetrofitInstance(Url.BASE_URL).create(
        DataService::class.java).createProduct(
        userId = userId,
        rememberToken = rememberToken,
        productName = product.name,
        price = product.sellingPrice,
        alertQuantity = product.alertQuantity,
        ena =  "",
        description = product.description?:"",
        statusId = 1,
        categoryId = category,
        code = product.code?:""
    )

    /**Updating Product information on Web Server**/
    suspend fun updateProduct(product: Product) = getRetrofitInstance(Url.BASE_URL).create(
        DataService::class.java).updateProduct(
        product.id,
        userId,
        rememberToken,
        product.name,
        product.sellingPrice,
        product.alertQuantity,
        product.code.toString(),
        product.description?:"",
        1,
        product.code?:""
    )

    /**Returns Product Details With List Of Product Purchase**/
    suspend fun getProductDetails(productId : Int) = getRetrofitInstance(Url.BASE_URL).create(
        DataService::class.java)
        .getProductDetails(
            productId,
            userIdMap,
            rememberTokenMap
        )


    /** ---CUSTOMER--- **/
    /**Create**/
    /**Update**/
    /**Delete**/


    /**Create Customer , Supplier & Product**/
    suspend fun createCustomer(customer : Customer): Response<ApiCustomer> {

        try{
            val response =  getRetrofitInstance(Url.BASE_URL).create(DataService::class.java).createCustomer(
                userId,
                rememberToken,
                customer.customerName,
                customer.customerAddress,
                customer.customerMobile,
                1,
                customer.customerNid?:"",
                customer.customerVat?:"",
                customer.customerEmail?:"",
                customer.customerOpeningBalance,
                customer.customerReceiptType?.toInt()?:0,
                1
            )
            return response
        }catch (e : Exception){
            Timber.d(e.message)
        }

        return Response.error(0,null)
    }

    /**Updating Customer information on Web Server**/
    suspend fun updateCustomer(customer : Customer) =getRetrofitInstance(Url.BASE_URL).create(
        DataService::class.java).updateCustomer(
        customer.id,
        userId,
        rememberToken,
        customer.customerName,
        customer.customerAddress,
        customer.customerMobile,
        1,
        customer.customerNid?:"",
        customer.customerVat?:"",
        customer.customerEmail?:"",
        customer.customerOpeningBalance,
        customer.customerReceiptType?.toInt()?:0,
        1
    )

    /**Customer Details With Customer Transaction List of Invoice , Receipt and Invoice**/
    suspend fun getCustomerDetails(customerId : Int) = getRetrofitInstance(Url.BASE_URL).create(
        DataService::class.java)
        .customerTransaction(
            customerId,
            userIdMap,
            rememberTokenMap
        )

    /**Deleting Customer**/
    suspend fun deleteCustomer(customer: Customer) = getRetrofitInstance(Url.BASE_URL).create(
        DataService::class.java)
        .deleteCustomer(
            customer.id,
            userIdMap,
            rememberTokenMap
        )

    /** ---SUPPLIER--- **/
    /**Create**/
    /**Update**/
    /**Delete**/


    /**Creating Supplier On Web Server**/
    suspend fun createSupplier(supplier : Supplier) = getRetrofitInstance(Url.BASE_URL).create(
        DataService::class.java).createSupplier(
        userId,
        rememberToken,
        supplier.name,
        supplier.ownerName,
        supplier.contactPerson,
        supplier.tinNo?:"",
        supplier.taxNo?:"",
        supplier.vatNo?:"",
        supplier.bstiNo?:"",
        supplier.email?:"",
        supplier.address,
        supplier.mobile,
        supplier.openingBalance
    )

    /**Updating Supplier information on Web Server**/
    suspend fun updateSupplier(supplier : Supplier) = getRetrofitInstance(Url.BASE_URL).create(
        DataService::class.java).updateSupplier(
        supplier.id,
        userId,
        rememberToken,
        supplier.name,
        supplier.ownerName,
        supplier.contactPerson,
        supplier.tinNo?:"",
        supplier.taxNo?:"",
        supplier.vatNo?:"",
        supplier.bstiNo?:"",
        supplier.email?:"",
        supplier.address,
        supplier.mobile,
        supplier.openingBalance
    )

    /**Returns Supplier Details List Supplier purchase List**/
    suspend fun getSupplierDetails(supplierId : Int) = getRetrofitInstance(Url.BASE_URL).create(
        DataService::class.java)
        .supplierTransaction(
            supplierId,
            userIdMap,
            rememberTokenMap
        )

    /**Deleting Customer**/
    suspend fun deleteSupplier(supplier : Supplier) = getRetrofitInstance(Url.BASE_URL).create(
        DataService::class.java)
        .deleteSupplier(
            supplier.id,
            userIdMap,
            rememberTokenMap
        )



    /** ---PURCHASE--- **/
    /**Create**/
    /**Update**/
    /**Delete**/

    /**Inserting Purchase Info In Web Server**/
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
    ) = getRetrofitInstance(Url.BASE_URL)
        .create(DataService::class.java)
        .insertPurchase(
            userId,
            rememberToken,
            id,
            subTotal,
            productIdArray,
            quantityList,
            productUnitPrice,
            totalProductPrice,
            FormateDate.formatDateToString(Calendar.getInstance().time),
            invoicePayment,
            "",
            partialPaid,
            discount,
            details,
            subTotal+discount,
            0
        )

    /**Returns User Invoice List**/
    suspend fun getUserPurchaseList(page: Int) = getRetrofitInstance(Url.BASE_URL).create(
        DataService::class.java)
        .getUserPurchaseList(
            hashMapOf("page" to page),
            userIdMap,
            rememberTokenMap
        )

    /**Returns User Specific Purchase Details**/
    suspend fun getSpecificPurchaseDetails(purchaseId : Int) = getRetrofitInstance(Url.BASE_URL).create(
        DataService::class.java)
        .getSpecificPurchaseDetails(
            purchaseId,
            userIdMap,
            rememberTokenMap
        )

    /**Update Purchase**/
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

    ) = getRetrofitInstance(Url.BASE_URL).create(DataService::class.java)
        .updatePurchase(
            purchaseInvoiceId,
            userId,
            rememberToken,
            id,
            subTotal,
            productIdArray,
            quantityList,
            productUnitPrice,
            totalProductPrice,
            FormateDate.formatDateToString(Calendar.getInstance().time),
            invoicePayment,
            "",
            partialPaid,
            discount,
            details,
            subTotal+discount,
            0

        )

    /**Inserting Supplier Payment information into Payment Table**/
    suspend fun insertPayment(payment : Payment) = getRetrofitInstance(Url.BASE_URL)
        .create(DataService::class.java)
        .insertPayment(
            userId,
            rememberToken,
            payment.id,
            payment.ledgerId,
            payment.dailyAccountId,
            payment.supplierId,
            payment.cashRegisterId,
            payment.type,
            payment.chequeNo,
            FormateDate.formatDateToString(payment.paymentDate),
            FormateDate.formatDateToString(payment.chequeDate),
            payment.total,
            payment.discount,
            payment.amount,
            payment.details,
            payment.createdAt,
            payment.updatedAt,
            payment.deletedAt
        )

    suspend fun deletePurchase(purchase : PurchaseData) = getRetrofitInstance(Url.BASE_URL).create(
        DataService::class.java)
        .deletePurchase(
            purchase.purchase_invoice,
            userIdMap,
            rememberTokenMap
        )

    /** ---SALES--- **/
    /**Create**/
    /**Update**/
    /**Delete**/


    /**Inserting Invoice To Web Database**/
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
    ) = getRetrofitInstance(Url.BASE_URL)
        .create(DataService::class.java)
        .insertInvoice(
            userId,
            rememberToken,
            id,
            subTotal,
            partialPaid,
            dueAmount,
            0,
            "Nothing",
            productIdArray,
            quantityList,
            productUnitPrice,
            totalProductPrice,
            FormateDate.formatDateToString(Calendar.getInstance().time),
            invoicePayment,
            "",
            partialPaid,
            discount,
            details
        )


    /**Update Invoice Information**/
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
    ) = getRetrofitInstance(Url.BASE_URL)
        .create(DataService::class.java)
        .updateInvoice(
            invoiceId,
            userId,
            rememberToken,
            id,
            subTotal,
            partialPaid,
            dueAmount,
            0,
            "Nothing",
            productIdArray,
            quantityList,
            productUnitPrice,
            totalProductPrice,
            FormateDate.formatDateToString(Calendar.getInstance().time),
            invoicePayment,
            "",
            partialPaid,
            discount,
            details
        )



    /**Returns User Invoice List**/
    suspend fun getUserInvoiceList(page : Int) = getRetrofitInstance(Url.BASE_URL).create(
        DataService::class.java)
        .getUserInvoiceList(
            hashMapOf("page" to page),
            userIdMap,
            rememberTokenMap
        )


    /**Returns User Invoice List**/
    suspend fun getSpecificInvoiceDetails(invoiceId: Int) = getRetrofitInstance(Url.BASE_URL).create(
        DataService::class.java)
        .getSpecificInvoiceDetails(
            invoiceId,
            userIdMap,
            rememberTokenMap
        )


    /**Delete Invoice**/
    suspend fun deleteInvoice(invoiceId : Int) = getRetrofitInstance(Url.BASE_URL).create(
        DataService::class.java)
        .deleteInvoice(
            invoiceId,
            userIdMap,
            rememberTokenMap
        )

    /** ---RECEIPT--- **/
    /**Create**/
    /**Update**/
    /**Delete**/

    /**Inserting Customer Receipt information To Receipt Table**/
    suspend fun insertReceipt(receipt: Receipt) = getRetrofitInstance(Url.BASE_URL).create(
        DataService::class.java).insertReceipt(
        userId,
        rememberToken,
        receipt.ledgerId,
        receipt.invoiceId.toString(),
        receipt.rtvId,
        receipt.type,
        receipt.dailyAccountId,
        receipt.customerId,
        receipt.cashRegisterId,
        receipt.chequeNo,
        FormateDate.formatDateToString(receipt.receiveDate),
        FormateDate.formatDateToString(receipt.chequeDate),
        FormateDate.formatDateToString(receipt.clearanceDate),
        receipt.invoiceAmount ,
        receipt.rtvAmount ,
        receipt.vatAmount ,
        receipt.amount ,
        receipt.discount ,
        receipt.total ,
        receipt.details
    )

    /**Returns User Receipt List**/
    suspend fun getUserReceiptList(page: Int) = getRetrofitInstance(Url.BASE_URL).create(DataService::class.java)
        .getUserReceiptList(
            hashMapOf("page" to page),
            userIdMap,
            rememberTokenMap
        )

    /**Updating Receipt Information For Specific Customer**/
    suspend fun updateReceipt(receipt: Receipt) = getRetrofitInstance(Url.BASE_URL).create(
        DataService::class.java)
        .updateReceipt(
            receipt.id,
            userId,
            rememberToken,
            0,
            receipt.invoiceId.toString(),
            "0",
            0,
            0,
            receipt.customerId,
            0,
            "0",
            FormateDate.formatDateToString(receipt.receiveDate),
            FormateDate.formatDateToString(receipt.chequeDate),
            FormateDate.formatDateToString(receipt.clearanceDate),
            receipt.invoiceAmount,
            0.0,
            0.0,
            receipt.amount,
            receipt.discount,
            receipt.total,
            receipt.details
        )

    /**Delete Receipt**/
    suspend fun deleteReceipt(receiptId : Int) = getRetrofitInstance(Url.BASE_URL).create(
        DataService::class.java)
        .deleteReceipt(
            receiptId,
            userIdMap,
            rememberTokenMap
        )

    /** ---PAYMENT--- **/
    /**Create**/
    /**Update**/
    /**Delete**/

    /**Update Supplier Specific Payment Information**/
    suspend fun updatePayment(payment: Payment) = getRetrofitInstance(Url.BASE_URL).create(
        DataService::class.java)
        .updatePayment(
            payment.id,
            userId,
            rememberToken,
            0,
            0,
            payment.supplierId,
            0,
            0,
            "0",
            FormateDate.formatDateToString(payment.paymentDate),
            FormateDate.formatDateToString(payment.chequeDate),
            payment.amount,
            payment.discount,
            payment.total,
            "Update Payment",
            payment.updatedAt
        )


    /**Returns User Invoice List**/
    suspend fun getUserPaymentList(page: Int) = getRetrofitInstance(Url.BASE_URL).create(DataService::class.java)
        .getUserPaymentList(
            hashMapOf("page" to page),
            userIdMap,
            rememberTokenMap
        )


    /**Delete payment**/
    suspend fun deletePayment(paymentId : Int) = getRetrofitInstance(Url.BASE_URL).create(
        DataService::class.java)
        .deletePayment(
            paymentId,
            userIdMap,
            rememberTokenMap
        )

    /**Search Customer**/
    suspend fun searchCustomer(search : String, customerMobile : String) = getRetrofitInstance(Url.BASE_URL).create(
        DataService::class.java)
                .searchCustomer(
                    hashMapOf("name" to search),
                    hashMapOf("mobile" to customerMobile),
                    userIdMap,
                    rememberTokenMap
                )




    /**Search Supplier**/
    suspend fun searchSupplier(search: String, mobile: String) = getRetrofitInstance(Url.BASE_URL).create(
        DataService::class.java)
        .searchSupplier(
            hashMapOf("name" to search),
            hashMapOf("mobile" to mobile),
            userIdMap,
            rememberTokenMap
        )


    /**Search Invoice by id or customer Id**/
    /**Search By Bill No**/
    /**Search By Customer Id**/
    /**search By Date Range**/
    suspend fun searchInvoice(search: String, fromDate: String, toDate: String, billNo: String,page:Int) = getRetrofitInstance(Url.BASE_URL).create(
        DataService::class.java)
        .searchInvoice(
            hashMapOf("page" to page),
            hashMapOf("customer_id" to search),
            hashMapOf("bill_no" to billNo),
            hashMapOf("date_range" to "$fromDate - $toDate"),
            userIdMap,
            rememberTokenMap
        )




    /**Search Purchase by Purchase Invoice no and supplier Id**/
    /**Search By Purchase Invoice**/
    /**Search By Supplier Id**/
    suspend fun searchPurchase(
        page: Int,
        search: String,
        fromDate: String,
        toDate: String,
        purchaseInvoiceNo: String
    ) = getRetrofitInstance(Url.BASE_URL).create(DataService::class.java)
        .searchPurchase(
            hashMapOf("page" to page),
            hashMapOf("supplier_id" to search),
            hashMapOf("po_no" to purchaseInvoiceNo),
            hashMapOf("date_range" to "$fromDate - $toDate"),
            userIdMap,
            rememberTokenMap
        )


    /**Payment Search**/
    suspend fun searchReceipt(page: Int,search: String, fromDate: String, toDate: String) = getRetrofitInstance(Url.BASE_URL).create(
        DataService::class.java)
        .searchReceipt(
            hashMapOf("page" to page),
            hashMapOf("customer_id" to search),
            hashMapOf("date_range" to "$fromDate - $toDate"),
            userIdMap,
            rememberTokenMap
        )


    /**Payment Search**/
    suspend fun searchPayment(page: Int,search: String, fromDate: String, toDate: String) = getRetrofitInstance(Url.BASE_URL).create(
        DataService::class.java)
        .searchPayment(
            hashMapOf("page" to page),
            hashMapOf("supplier_id" to search),
            hashMapOf("date_range" to "$fromDate - $toDate"),
            userIdMap,
            rememberTokenMap
        )

    /**Customer Search By Date**/
    suspend fun dateSearchReceipt(customerId: Int, fromDate: String, toDate: String) = getRetrofitInstance(Url.BASE_URL).create(
        DataService::class.java)
        .dateReceiptSearch(
            customerId,
            hashMapOf("date_range" to "$fromDate - $toDate"),
            userIdMap,
            rememberTokenMap
        )

    suspend fun searchProduct( nameOrCode: String) = getRetrofitInstance(Url.BASE_URL).create(
        DataService::class.java)
        .searchProduct(
            hashMapOf("search_text" to nameOrCode),
            userIdMap,
            rememberTokenMap
        )

    suspend fun deleteProduct(product: Product) = getRetrofitInstance(Url.BASE_URL).create(
        DataService::class.java)
        .deleteProduct(
            product.id,
            userIdMap,
            rememberTokenMap
        )

    suspend fun productDateSearch(productId: Int, fromDate: String, toDate: String) = getRetrofitInstance(Url.BASE_URL).create(
        DataService::class.java)
        .productDateSearch(
            productId,
            hashMapOf("date_range" to "$fromDate - $toDate"),
            userIdMap,
            rememberTokenMap
        )

    suspend fun searchSupplierPurchase(id: Int, fromDate: String, toDate: String) = getRetrofitInstance(Url.BASE_URL).create(
        DataService::class.java)
        .searchSupplierPurchase(
            id,
            hashMapOf("date_range" to "$fromDate - $toDate"),
            userIdMap,
            rememberTokenMap
        )

    suspend fun getAllCustomer() = getRetrofitInstance(Url.BASE_URL).create(DataService::class.java)
        .getUserCustomer(
            hashMapOf("limit" to "-1"),
            userIdMap,
            rememberTokenMap
        )

    suspend fun getAllSupplier() = getRetrofitInstance(Url.BASE_URL).create(DataService::class.java)
        .getUserSupplier(
            hashMapOf("limit" to "-1"),
            userIdMap,
            rememberTokenMap
        )

    suspend fun getAllProduct() = getRetrofitInstance(Url.BASE_URL).create(DataService::class.java)
        .getUserProduct(
            hashMapOf("limit" to "-1"),
            userIdMap,
            rememberTokenMap
        )
}
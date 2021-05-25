package com.example.shopmanagement.data.api


import com.example.shopmanagement.data.models.api.authentication.ApiAuth
import com.example.shopmanagement.data.models.api.category.ApiShopCategory
import com.example.shopmanagement.data.models.api.customer.ApiCustomer
import com.example.shopmanagement.data.models.api.customer.delete.ApiCustomerDelete
import com.example.shopmanagement.data.models.api.customer.list.ApiCustomerList
import com.example.shopmanagement.data.models.api.customer.search.ApiCustomerSearch
import com.example.shopmanagement.data.models.api.customer.transaction.ApiTransaction
import com.example.shopmanagement.data.models.api.dashboard.ApiDashBoard
import com.example.shopmanagement.data.models.api.invoice.ApiInvoice
import com.example.shopmanagement.data.models.api.invoice.delete.ApiInvoiceDelete
import com.example.shopmanagement.data.models.api.invoice.details.ApiSpecificInvoice
import com.example.shopmanagement.data.models.api.invoice.list.ApiInvoiceList
import com.example.shopmanagement.data.models.api.invoice.search.ApiInvoiceSearch
import com.example.shopmanagement.data.models.api.payment.ApiPayment
import com.example.shopmanagement.data.models.api.payment.delete.ApiPaymentDelete
import com.example.shopmanagement.data.models.api.payment.list.ApiPaymentList
import com.example.shopmanagement.data.models.api.payment.search.ApiPaymentSearch
import com.example.shopmanagement.data.models.api.product.ApiProduct
import com.example.shopmanagement.data.models.api.product.details.ApiProductDetails
import com.example.shopmanagement.data.models.api.product.list.ApiProductList
import com.example.shopmanagement.data.models.api.product.search.ApiProductSearch
import com.example.shopmanagement.data.models.api.purchase.ApiPurchase
import com.example.shopmanagement.data.models.api.purchase.delete.ApiPurchaseDelete
import com.example.shopmanagement.data.models.api.purchase.details.ApiPurchaseDetails
import com.example.shopmanagement.data.models.api.purchase.list.ApiPurchaseList
import com.example.shopmanagement.data.models.api.purchase.search.ApiPurchaseSearch
import com.example.shopmanagement.data.models.api.receipt.ApiReceipt
import com.example.shopmanagement.data.models.api.receipt.delete.ApiReceiptDelete
import com.example.shopmanagement.data.models.api.receipt.details.ApiReceiptDetails
import com.example.shopmanagement.data.models.api.supplier.ApiSupplier
import com.example.shopmanagement.data.models.api.supplier.delete.ApiSupplierDelete
import com.example.shopmanagement.data.models.api.supplier.details.ApiSupplierDetails
import com.example.shopmanagement.data.models.api.supplier.list.ApiSupplierList
import com.example.shopmanagement.data.models.api.supplier.search.ApiSupplierSearch
import com.example.shopmanagement.data.models.api.user.ApiUser
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*
import java.util.*
import kotlin.collections.HashMap

interface DataService {

    @FormUrlEncoded
    @POST("license")
    suspend fun validate(
        @Field("name") name: String,
        @Field("client_company_name") clientOrCompanyName: String,
        @Field("mobile") number: String,
        @Field("worknumber") workNumber: String,
        @Field("email") email: String,
        @Field("address") address: String,
        @Field("project_id") projectID: String,
        @Field("url") url: String,
        @Field("hit_path") hitPath: String,
        @Field("expire_at") expireAt: String,
        @Field("times_activated_max") timesActivatedMax: String,
        @Field("license_key") license_key: String,
        @Field("otp") otp: Boolean,
        @Field("msg") msg: String,
        @Field("email_otp") emailOtp: Boolean,
        @Field("status") status : Int
    ) : Response<ApiAuth>

    @FormUrlEncoded
    @POST("/smsapi?")
    suspend fun mimSmsRequest(
        @Field("api_key") API_KEY:String,
        @Field("type") text_Type:String,
        @Field("contacts") contacts:String,
        @Field("senderid") sender_id:String,
        @Field("msg") message:String
    ) : Response<ResponseBody>

    /**User Creation**/
    @FormUrlEncoded
    @POST("auth")
    suspend fun createUser(
        @Field("user_id") userId:String,
        @Field("phone") phone : String,
        @Field("company_name") companyName:String,
        @Field("category_id") categoryId : String
    ):Response<ApiUser>

    /**DashBoard**/
    @GET("dashboard")
    suspend fun getDashBoardData(
        @QueryMap
        userId : Map<String,Int>,
        @QueryMap
        rememberToken : Map<String,String>
    ) : Response<ApiDashBoard>

    /**Shop Category**/
    @GET("category")
    suspend fun getShopCategory() : Response<ApiShopCategory>


    /**Creating Customer**/
    @FormUrlEncoded
    @POST("customer")
    suspend fun createCustomer(
        @Field("user_id")
        userId : Int,
        @Field("remember_token")
        rememberToken : String,
        @Field("name")
        customerName: String,
        @Field("address")
        customerAddress: String,
        @Field("mobile")
        customerMobile: String,
        @Field("gender")
        customerGender: Int,
        @Field("nid")
        customerNid: String,
        @Field("vat_no")
        customerVat: String,
        @Field("email")
        customerEmail: String,
        @Field("opening_balance")
        customerOpeningBalance : Double,
        @Field("receipt_type")
        customerReceiptType : Int,
        @Field("status")
        status : Int
    ) : Response<ApiCustomer>


    /**Update Customer**/
    @FormUrlEncoded
    @PUT("customer/{id}")
    suspend fun updateCustomer(
        @Path(value = "id", encoded = false)
        key: Int,
        @Field("user_id")
        userId : Int,
        @Field("remember_token")
        rememberToken : String,
        @Field("name")
        customerName: String,
        @Field("address")
        customerAddress: String,
        @Field("mobile")
        customerMobile: String,
        @Field("gender")
        customerGender: Int,
        @Field("nid")
        customerNid: String,
        @Field("vat_no")
        customerVat: String,
        @Field("email")
        customerEmail: String,
        @Field("opening_balance")
        customerOpeningBalance : Double,
        @Field("receipt_type")
        customerReceiptType : Int,
        @Field("status")
        status : Int
    ) : Response<ApiCustomer>

    /**Delete Customer**/
    @DELETE("customer/{id}")
    suspend fun deleteCustomer(
        @Path(value = "id", encoded = false)
        id : Int,
        @QueryMap
        userId : Map<String,Int>,
        @QueryMap
        rememberToken : Map<String,String>
    ) : Response<ApiCustomerDelete>

    /**Delete Supplier**/
    @DELETE("supplier/{id}")
    suspend fun deleteSupplier(
        @Path(value = "id", encoded = false)
        id : Int,
        @QueryMap
        userId : Map<String,Int>,
        @QueryMap
        rememberToken : Map<String,String>
    ) : Response<ApiSupplierDelete>

    /**Creating Supplier**/
    @FormUrlEncoded
    @POST("supplier")
    suspend fun createSupplier(
        @Field("user_id")
        userId : Int,
        @Field("remember_token")
        rememberToken : String,
        @Field("name")
        companyName: String,
        @Field("owner_name")
        ownerName : String,
        @Field("contact_person_name")
        contactPerson : String,
        @Field("tin_no")
        tinNo : String,
        @Field("tax_no")
        taxNo : String,
        @Field("vat_no")
        vatNo : String,
        @Field("bsti_no")
        bstiNo : String,
        @Field("email")
        supplierEmail: String,
        @Field("address")
        address : String,
        @Field("mobile")
        supplierMobile : String,
        @Field("opening_balance")
        customerOpeningBalance : Double
    ) : Response<ApiSupplier>


    /**Update Supplier**/
    @FormUrlEncoded
    @PUT("supplier/{id}")
    suspend fun updateSupplier(
        @Path(value = "id", encoded = false)
        key: Int,
        @Field("user_id")
        userId : Int,
        @Field("remember_token")
        rememberToken : String,
        @Field("name")
        companyName: String,
        @Field("owner_name")
        ownerName : String,
        @Field("contact_person_name")
        contactPerson : String,
        @Field("tin_no")
        tinNo : String,
        @Field("tax_no")
        taxNo : String,
        @Field("vat_no")
        vatNo : String,
        @Field("bsti_no")
        bstiNo : String,
        @Field("email")
        supplierEmail: String,
        @Field("address")
        address : String,
        @Field("mobile")
        supplierMobile : String,
        @Field("opening_balance")
        customerOpeningBalance : Double
    ) : Response<ApiSupplier>


    /**Create Product**/
    @FormUrlEncoded
    @POST("product")
    suspend fun createProduct(
        @Field("user_id")
        userId : Int,
        @Field("remember_token")
        rememberToken : String,
        @Field("name")
        productName : String,
        @Field("mrp")
        price : Double,
        @Field("alert_quantity")
        alertQuantity : Int,
        @Field("ena")
        ena : String,
        @Field("description")
        description : String,
        @Field("status")
        statusId : Int,
        @Field("category_id")
        categoryId : Int,
        @Field("code")
        code : String
    ) : Response<ApiProduct>

    /**Update Product**/
    @FormUrlEncoded
    @PUT("product/{id}")
    suspend fun updateProduct(
        @Path(value = "id", encoded = false)
        key: Int,
        @Field("user_id")
        userId : Int,
        @Field("remember_token")
        rememberToken : String,
        @Field("name")
        productName : String,
        @Field("mrp")
        price : Double,
        @Field("alert_quantity")
        alertQuantity : Int,
        @Field("ena")
        ena : String,
        @Field("description")
        description : String,
        @Field("status")
        statusId : Int,
        @Field("code")
        code : String
    ) : Response<ApiProduct>

    /**Insert Receipt**/
    @FormUrlEncoded
    @POST("receipt")
    suspend fun insertReceipt(
        @Field("user_id")
        userId: Int,
        @Field("remember_token")
        rememberToken: String,
        @Field("ledger_id")
        ledgerId: Int,
        @Field("invoice_id")
        invoiceId: String,
        @Field("rtv_id")
        rtvId: String,
        @Field("type")
        type: Int,
        @Field("dailyaccount_id")
        dailyAccountId: Int,
        @Field("customer_id")
        customerId: Int,
        @Field("cash_register_id")
        cashRegisterId: Int,
        @Field("cheque_no")
        chequeNo: String,
        @Field("receive_date")
        receiveDate: String,
        @Field("cheque_date")
        chequeDate: String,
        @Field("clearance_date")
        clearanceDate: String,
        @Field("invoice_amount")
        invoiceAmount: Double,
        @Field("rtv_amount")
        rtvAmount: Double,
        @Field("vat_amount")
        vatAmount: Double,
        @Field("amount")
        amount: Double,
        @Field("discount")
        discount: Double,
        @Field("total")
        total: Double,
        @Field("details")
        details: String
    ) : Response<ApiReceipt>


    /**Delete Specific Receipt**/
    @DELETE("receipt/{id}")
    suspend fun deleteReceipt(
        @Path(value = "id", encoded = false)
        id : Int,
        @QueryMap
        userId : Map<String,Int>,
        @QueryMap
        rememberToken : Map<String,String>
    ) : Response<ApiReceiptDelete>


    /**Update Receipt**/
    @FormUrlEncoded
    @PUT("receipt/{id}")
    suspend fun updateReceipt(
        @Path(value = "id", encoded = false)
        key: Int,
        @Field("user_id")
        userId: Int,
        @Field("remember_token")
        rememberToken: String,
        @Field("ledger_id")
        ledgerId: Int,
        @Field("invoice_id")
        invoiceId: String,
        @Field("rtv_id")
        rtvId: String,
        @Field("type")
        type: Int,
        @Field("dailyaccount_id")
        dailyAccountId: Int,
        @Field("customer_id")
        customerId: Int,
        @Field("cash_register_id")
        cashRegisterId: Int,
        @Field("cheque_no")
        chequeNo: String,
        @Field("receive_date")
        receiveDate: String,
        @Field("cheque_date")
        chequeDate: String,
        @Field("clearance_date")
        clearanceDate: String,
        @Field("invoice_amount")
        invoiceAmount: Double,
        @Field("rtv_amount")
        rtvAmount: Double,
        @Field("vat_amount")
        vatAmount: Double,
        @Field("amount")
        amount: Double,
        @Field("discount")
        discount: Double,
        @Field("total")
        total: Double,
        @Field("details")
        details: String
    ) : Response<ApiReceipt>


    /**Delete Receipt**/



    /**Insert Payment into Database Table**/
    @FormUrlEncoded
    @POST("payment")
    suspend fun insertPayment(
        @Field("user_id")
        userId: Int,
        @Field("remember_token")
        rememberToken: String,
        @Field("id")
        id: Int,
        @Field("ledger_id")
        ledgerId: Int,
        @Field("dailyaccount_id")
        dailyAccountId: Int,
        @Field("supplier_id")
        supplierId: Int,
        @Field("cash_register_id")
        cashRegisterId: Int,
        @Field("type")
        type: Int,
        @Field("cheque_no")
        chequeNo: String,
        @Field("payment_date")
        paymentDate: String,
        @Field("cheque_date")
        chequeDate: String,
        @Field("amount")
        amount: Double,
        @Field("discount")
        discount: Double,
        @Field("total")
        total: Double,
        @Field("details")
        details: String,
        @Field("created_at")
        createdAt: Date,
        @Field("updated_at")
        updatedAt: Date,
        @Field("deleted_at")
        deletedAt: Date

    ) : Response<ApiPayment>

    /**Update Payment into Database Table**/
    @FormUrlEncoded
    @PUT("payment/{id}")
    suspend fun updatePayment(
        @Path(value = "id", encoded = false)
        key: Int,
        @Field("user_id")
        userId : Int,
        @Field("remember_token")
        rememberToken : String,
        @Field("ledger_id")
        ledgerId : Int,
        @Field("dailyaccount_id")
        dailyAccountId : Int,
        @Field("supplier_id")
        supplierId : Int,
        @Field("cash_register_id")
        cashRegisterId : Int,
        @Field("type")
        type : Int,
        @Field("cheque_no")
        chequeNo : String,
        @Field("payment_date")
        paymentDate : String,
        @Field("cheque_date")
        chequeDate : String,
        @Field("amount")
        amount : Double,
        @Field("discount")
        discount : Double,
        @Field("total")
        total : Double,
        @Field("details")
        details : String,
        @Field("updated_at")
        updatedAt : Date
    ) : Response<ApiPayment>

    /**Delete Specific Payment Information**/
    @DELETE("payment/{id}")
    suspend fun deletePayment(
        @Path(value = "id", encoded = false)
        id : Int,
        @QueryMap
        userId : Map<String,Int>,
        @QueryMap
        rememberToken : Map<String,String>
    ) : Response<ApiPaymentDelete>

    /**Insert Invoice**/
    @FormUrlEncoded
    @POST("invoice")
    suspend fun insertInvoice(
        @Field("user_id")
        userId : Int,
        @Field("remember_token")
        rememberToken : String,
        @Field("customer_id")
        customerId : Int,
        @Field("sub_total")
        subTotal : Double,
        @Field("partial_payment")
        partialPayment : Double,
        @Field("due_amount")
        dueAmount : Double,
        @Field("type")
        type : Int,
        @Field("details")
        details : String,
        @Field("product[]")
        product : List<Int>,
        @Field("dell_quantity[]")
        quantity : List<Double>,
        @Field("unit_price[]")
        price: List<Double>,
        @Field("total_amount[]")
        total : List<Double>,
        @Field("date")
        date : String,
        @Field("invoice_payment")
        invoicePayment : Boolean,
        @Field("cheque_no")
        checkNo : String,
        @Field("total")
        payableAmount : Double,
        @Field("discount_amount")
        discount: Double,
        @Field("receipt_details")
        receiptDetails : String
    ) : Response<ApiInvoice>

    /**Update Invoice**/
    @FormUrlEncoded
    @PUT("invoice/{id}")
    suspend fun updateInvoice(
        @Path(value = "id", encoded = false)
        key: Int,
        @Field("user_id")
        userId : Int,
        @Field("remember_token")
        rememberToken : String,
        @Field("customer_id")
        customerId : Int,
        @Field("sub_total")
        subTotal : Double,
        @Field("partial_payment")
        partialPayment : Double,
        @Field("due_amount")
        dueAmount : Double,
        @Field("type")
        type : Int,
        @Field("details")
        details : String,
        @Field("product[]")
        product : List<Int>,
        @Field("dell_quantity[]")
        quantity : List<Double>,
        @Field("unit_price[]")
        price: List<Double>,
        @Field("total_amount[]")
        total : List<Double>,
        @Field("date")
        date : String,
        @Field("invoice_payment")
        invoicePayment : Boolean,
        @Field("cheque_no")
        checkNo : String,
        @Field("total")
        payableAmount : Double,
        @Field("discount_amount")
        discount: Double,
        @Field("receipt_details")
        receiptDetails : String
    ) : Response<ApiInvoice>

    /**Delete Invoice**/
    @DELETE("invoice/{id}")
    suspend fun deleteInvoice(
        @Path(value = "id", encoded = false)
        id : Int,
        @QueryMap
        userId : Map<String,Int>,
        @QueryMap
        rememberToken : Map<String,String>
    ) : Response<ApiInvoiceDelete>

    /**Insert Purchase**/
    @FormUrlEncoded
    @POST("purchase")
    suspend fun insertPurchase(
        @Field("user_id")
        userId : Int,
        @Field("remember_token")
        rememberToken : String,
        @Field("supplier_id")
        supplierId : Int,
        @Field("sub_total")
        subTotal : Double,
        @Field("product_id[]")
        product : List<Int>,
        @Field("quantity[]")
        quantity : List<Double>,
        @Field("purchase_price[]")
        price: List<Double>,
        @Field("total_amount[]")
        total : List<Double>,
        @Field("date")
        date : String,
        @Field("invoice_payment")
        invoicePayment : Boolean,
        @Field("cheque_no")
        checkNo : String,
        @Field("total")
        payableAmount : Double,
        @Field("discount")
        discount: Double,
        @Field("details")
        details : String,
        @Field("amount")
        totalAmount : Double,
        @Field("type")
        type: Int
    ) : Response<ApiPurchase>




    /**Update Purchase**/
    @FormUrlEncoded
    @PUT("purchase/{id}")
    suspend fun updatePurchase(
        @Path(value = "id", encoded = false)
        id : Int,
        @Field("user_id")
        userId : Int,
        @Field("remember_token")
        rememberToken : String,
        @Field("supplier_id")
        supplierId : Int,
        @Field("sub_total")
        subTotal : Double,
        @Field("product_id[]")
        product : List<Int>,
        @Field("quantity[]")
        quantity : List<Double>,
        @Field("purchase_price[]")
        price: List<Double>,
        @Field("total_amount[]")
        total : List<Double>,
        @Field("date")
        date : String,
        @Field("invoice_payment")
        invoicePayment : Boolean,
        @Field("cheque_no")
        checkNo : String,
        @Field("total")
        payableAmount : Double,
        @Field("discount")
        discount: Double,
        @Field("details")
        details : String,
        @Field("amount")
        totalAmount : Double,
        @Field("type")
        type: Int

    ) : Response<ApiPurchase>

    /**Delete Purchase**/
    @DELETE("purchase/{id}")
    suspend fun deletePurchase(
        @Path(value = "id", encoded = false)
        id : Int,
        @QueryMap
        userId : Map<String,Int>,
        @QueryMap
        rememberToken : Map<String,String>
    ) : Response<ApiPurchaseDelete>

    /**Customer Transaction Information**/
    @GET("customer/{id}")
    suspend fun customerTransaction(
        @Path(value = "id", encoded = false)
        id : Int,
        @QueryMap
        userId : Map<String,Int>,
        @QueryMap
        rememberToken : Map<String,String>
    ) : Response<ApiTransaction>

    /**Supplier Transaction Information**/
    @GET("supplier/{id}")
    suspend fun supplierTransaction(
        @Path(value = "id", encoded = false)
        id : Int,
        @QueryMap
        userId : Map<String,Int>,
        @QueryMap
        rememberToken : Map<String,String>
    ) : Response<ApiSupplierDetails>


    /**Returns Application Users List of Invoice**/
    @GET("invoice")
    suspend fun getUserInvoiceList(
        @QueryMap
        page : Map<String,Int>,
        @QueryMap
        userId : Map<String,Int>,
        @QueryMap
        rememberToken : Map<String,String>
    ) : Response<ApiInvoiceList>


    /**Returns Application Users List of Invoice**/
    @GET("product/{id}")
    suspend fun getProductDetails(
        @Path(value = "id", encoded = false)
        id : Int,
        @QueryMap
        userId : Map<String,Int>,
        @QueryMap
        rememberToken : Map<String,String>
    ) : Response<ApiProductDetails>




    /**Get User Receipt List**/
    @GET("receipt")
    suspend fun getUserReceiptList(
        @QueryMap
        page: Map<String, Int>,
        @QueryMap
        userId : Map<String,Int>,
        @QueryMap
        rememberToken : Map<String,String>
    ) : Response<ApiReceiptDetails>

    /**Get User Payment List**/
    @GET("payment")
    suspend fun getUserPaymentList(
        @QueryMap
        page: Map<String, Int>,
        @QueryMap
        userId : Map<String,Int>,
        @QueryMap
        rememberToken : Map<String,String>
    ) : Response<ApiPaymentList>

    /**Get Invoice Information Of Sales List**/
    @GET("invoice/{id}")
    suspend fun getSpecificInvoiceDetails(
        @Path(value = "id", encoded = false)
        id : Int,
        @QueryMap
        userId : Map<String,Int>,
        @QueryMap
        rememberToken : Map<String,String>
    ) : Response<ApiSpecificInvoice>

    /**Returns Application Users List of Purchase**/
    @GET("purchase")
    suspend fun getUserPurchaseList(
        @QueryMap
        page : HashMap<String, Int>,
        @QueryMap
        userId : Map<String,Int>,
        @QueryMap
        rememberToken : Map<String,String>
    ): Response<ApiPurchaseList>

    /**Get Invoice Information Of Sales List**/
    @GET("purchase/{id}")
    suspend fun getSpecificPurchaseDetails(
        @Path(value = "id", encoded = false)
        id : Int,
        @QueryMap
        userId : Map<String,Int>,
        @QueryMap
        rememberToken : Map<String,String>
    ) : Response<ApiPurchaseDetails>



    /**Search Customer By Mobile Name Or Number**/
    @GET("customer")
    suspend fun searchCustomer(
        @QueryMap
        hashMapOfName : HashMap<String, String>,
        @QueryMap
        hashMapOfCustomer : HashMap<String, String>,
        @QueryMap
        userIdMap : HashMap<String, Int>,
        @QueryMap
        rememberTokenMap : HashMap<String, String>
    ) : Response<ApiCustomerSearch>

    /**Search Supplier by Mobile or Name**/
    @GET("supplier")
    suspend fun searchSupplier(
        @QueryMap
        hashMapOfName : HashMap<String, String>,
        @QueryMap
        hashMapOfMobile : HashMap<String, String>,
        @QueryMap
        userIdMap : HashMap<String, Int>,
        @QueryMap
        rememberTokenMap : HashMap<String, String>
    ) : Response<ApiSupplierSearch>


    /**Invoice Search**/
    @GET("invoice")
    suspend fun searchInvoice(
        @QueryMap
        hashMapOfPage : HashMap<String, Int>,
        @QueryMap
        hashMapOfId: HashMap<String, String>,
        @QueryMap
        hashMapOfBill: HashMap<String, String>,
        @QueryMap
        hashMapOfDateRange: HashMap<String, String>,
        @QueryMap
        userIdMap : HashMap<String, Int>,
        @QueryMap
        rememberTokenMap : HashMap<String, String>
    ) : Response<ApiInvoiceSearch>

    /**Purchase Search**/
    @GET("purchase")
    suspend fun searchPurchase(
        @QueryMap
        page: Map<String, Int>,
        @QueryMap
        hashMapOfId : HashMap<String, String>,
        @QueryMap
        hashMapOfPoNo : HashMap<String, String>,
        @QueryMap
        hashMapOfDate : HashMap<String, String>,
        @QueryMap
        userIdMap : HashMap<String, Int>,
        @QueryMap
        rememberTokenMap : HashMap<String, String>
    ) : Response<ApiPurchaseSearch>


    /**Receipt Search by Customer Id**/
    @GET("receipt")
    suspend fun searchReceipt(
        @QueryMap
        page: Map<String, Int>,
        @QueryMap
        hashMapOfName : HashMap<String, String>,
        @QueryMap
        hashMapOfMobile : HashMap<String, String>,
        @QueryMap
        userIdMap : HashMap<String, Int>,
        @QueryMap
        rememberTokenMap : HashMap<String, String>
    ) : Response<ApiReceiptDetails>


    /**Payment Search by Supplier Id**/
    @GET("payment")
    suspend fun searchPayment(
        @QueryMap
        page: Map<String, Int>,
        @QueryMap
        hashMapOfName : HashMap<String, String>,
        @QueryMap
        hashMapOfDate : HashMap<String, String>,
        @QueryMap
        userIdMap : HashMap<String, Int>,
        @QueryMap
        rememberTokenMap : HashMap<String, String>
    ) : Response<ApiPaymentSearch>

    @GET("customer/{id}")
    suspend fun dateReceiptSearch(
        @Path(value = "id", encoded = false)
        id : Int,
        @QueryMap
        hashMapOf: HashMap<String, String>,
        @QueryMap
        userIdMap: HashMap<String, Int>,
        @QueryMap
        rememberTokenMap: HashMap<String, String>
    ) : Response<ApiTransaction>

    @GET("product")
    suspend fun searchProduct(
        @QueryMap
        hashMapOfCode: HashMap<String, String>,
        @QueryMap
        userIdMap: HashMap<String, Int>,
        @QueryMap
        rememberTokenMap: HashMap<String, String>
    ) : Response<ApiProductSearch>

    @DELETE("product/{id}")
    suspend fun deleteProduct(
        @Path(value = "id", encoded = false)
        id : Int,
        @QueryMap
        userId : Map<String,Int>,
        @QueryMap
        rememberToken : Map<String,String>
    ) : Response<ApiProduct>

    @GET("product/{id}")
    suspend fun productDateSearch(
        @Path(value = "id", encoded = false)
        id: Int,
        @QueryMap
        hashMapOf: HashMap<String, String>,
        @QueryMap
        userIdMap: HashMap<String, Int>,
        @QueryMap
        rememberTokenMap : HashMap<String, String>
    ): Response<ApiProductDetails>

    @GET("supplier/{id}")
    suspend fun searchSupplierPurchase(
        @Path(value = "id", encoded = false)
        id: Int,
        @QueryMap
        hashMapOf: HashMap<String, String>,
        @QueryMap
        userIdMap: HashMap<String, Int>,
        @QueryMap
        rememberTokenMap: HashMap<String, String>
    ) : Response<ApiSupplierDetails>

    @GET("customer")
    suspend fun getUserCustomer(
        @QueryMap
        hashMapOfLimit : HashMap<String, String>,
        @QueryMap
        userIdMap : HashMap<String, Int>,
        @QueryMap
        rememberTokenMap : HashMap<String, String>
    ) : Response<ApiCustomerList>

    @GET("supplier")
    suspend fun getUserSupplier(
        @QueryMap
        hashMapOfLimit : HashMap<String, String>,
        @QueryMap
        userIdMap : HashMap<String, Int>,
        @QueryMap
        rememberTokenMap : HashMap<String, String>
    ) : Response<ApiSupplierList>

    @GET("product")
    suspend fun getUserProduct(
        @QueryMap
        hashMapOfLimit : HashMap<String, String>,
        @QueryMap
        userIdMap : HashMap<String, Int>,
        @QueryMap
        rememberTokenMap : HashMap<String, String>
    ) : Response<ApiProductList>

}
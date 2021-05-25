package com.example.shopmanagement.ui.purchase

import androidx.lifecycle.*
import com.example.shopmanagement.data.models.*
import com.example.shopmanagement.data.models.api.purchase.Data
import com.example.shopmanagement.data.models.api.purchase.PurchaseData
import com.example.shopmanagement.data.models.api.purchase.details.ApiPurchaseDetails
import com.example.shopmanagement.domain.*
import com.example.shopmanagement.domain.api.ApiUseCase
import com.example.shopmanagement.ui.utils.Event
import com.example.shopmanagement.ui.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.random.Random

@HiltViewModel
class PurchaseViewModel @Inject constructor(
    private val purchaseUseCase: PurchaseUseCase,
    private val supplierUseCase: SupplierUseCase,
    private val purchaseInvoiceUseCase: PurchaseInvoiceUseCase,
    private val salesUseCase: SalesUseCase,
    private val productUseCase: ProductUseCase,
    private val paymentUseCase: PaymentUseCase,
    private val apiUseCase: ApiUseCase
) : ViewModel(){

    var companyName = MutableLiveData<String?>()
    var ownerName = MutableLiveData<String>()
    var contactPersonName = MutableLiveData<String>()
    var mobile = MutableLiveData<String>()
    var address = MutableLiveData<String>()

    var billId = 0
    var customerName = MutableLiveData<String?>()
    var discount: Double = 0.0
    var validProduct : Boolean = false
    lateinit var name: String
    var confirmClick = false
    var supplierDetails = ""

    val supplierInvoiceListTotalBill = MutableLiveData<Double>()
    val supplierInvoiceTotalDue = MutableLiveData<Double>()
    val supplierInvoiceListTotalPaid = MutableLiveData<Double>()
    var discountTv = MutableLiveData<String>()
    var description = MutableLiveData<String>()
    var paymentTv = MutableLiveData<String>()
    var purchaseData = MutableLiveData<PurchaseData>()

    val supplierInvoiceListTotalDue = MutableLiveData<Double>()

    private val purchaseProduct = MutableLiveData<Product>()
    var totalBill = MutableLiveData<Double>()
    var totalItem = MutableLiveData<Int>()
    var totalQuantity = MutableLiveData<Double>()


    var billNo : Int = 0
    var payment : Double = 0.0
    var currentDue = MutableLiveData<String>()
    var totalPurchaseBill : Double = 0.0
    var fromEditScreen : Boolean = false

    private val _apiInvoice = MutableLiveData<ApiPurchaseDetails>()
    val apiInvoice : LiveData<ApiPurchaseDetails>
        get() = _apiInvoice

    private val _supplier = MutableLiveData<Supplier>()
    val supplier : LiveData<Supplier>
        get() = _supplier


    private val _allPurchaseInvoice = MutableLiveData<ArrayList<PurchaseInvoice>>()
    val allPurchaseInvoice : LiveData<ArrayList<PurchaseInvoice>>
        get() = _allPurchaseInvoice

    private val _navigate = MutableLiveData<Event<Boolean>>()
    val navigate: LiveData<Event<Boolean>>
        get() = _navigate

    private var _purchasedProduct = MutableLiveData<MutableList<Product>>()
    val purchasedProduct : LiveData<MutableList<Product>>
        get() = _purchasedProduct

    private val _product = MutableLiveData<ArrayList<SalesProductModel>>()
    val product : LiveData<ArrayList<SalesProductModel>>
        get() = _product

    private val _purchaseInvoice = MutableLiveData<ArrayList<PurchaseInvoice>>()
    val purchaseInvoice : LiveData<ArrayList<PurchaseInvoice>>
        get() = _purchaseInvoice

    private val _purchaseInvoiceDetails = MutableLiveData<PurchaseInvoice>()
    val purchaseInvoiceDetails : LiveData<PurchaseInvoice>
        get() = _purchaseInvoiceDetails

    private val _purchaseList = MutableLiveData<List<Purchase>>()
    val purchaseList : LiveData<List<Purchase>>
        get() = _purchaseList

    private val _supplierInvoiceList = MutableLiveData<List<PurchaseInvoice>>()
    val supplierInvoiceList : LiveData<List<PurchaseInvoice>>
        get() = _supplierInvoiceList

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>>
        get() = _errorMessage

    init {
        _purchasedProduct.value = mutableListOf()
    }


    fun addToPurchaseList(product: Product){
       _purchasedProduct.value?.add(product)
        Timber.d("List ${_purchasedProduct.value}")
    }


    // Purchase List Fragment

    fun getPurchaseInvoiceList(){
        viewModelScope.launch {
            _allPurchaseInvoice.value = purchaseInvoiceUseCase.getAllPurchaseInvoiceList() as ArrayList<PurchaseInvoice>
        }
    }



    fun deletePurchaseInvoice(invoice: PurchaseData, position: Int) {
        _allPurchaseInvoice.value?.removeAt(position)

        viewModelScope.launch {
            purchaseInvoiceUseCase.deleteInvoicePurchase(invoice)
        }

    }

    fun purchaseInvoiceEdit(invoice : PurchaseData) {
        salesUseCase.product.clear()
        billId = invoice.purchase_invoice
        viewModelScope.launch {
            getPurchaseByBillNo(invoice.purchase_invoice)
            getPurchaseInvoiceDetails(invoice.purchase_invoice)
            _supplier.value = supplierUseCase.getSupplierById(invoice.supplier_id)
            getPurchaseInvoiceListById(invoice.supplier_id)
            getProductList()
            totalQuantity()
            totalAmount()
        }
    }

    private suspend fun getPurchaseInvoiceListById(supplierId : Int) {
        _supplierInvoiceList.value = purchaseInvoiceUseCase.getSupplierInvoiceList(supplierId)
        totalInvoiceListDue()
    }


    private suspend fun getPurchaseInvoiceDetails(billNo: Int) {
        _purchaseInvoiceDetails.value = purchaseInvoiceUseCase.getInvoiceDetailsById(billNo)
    }

    private suspend fun getPurchaseByBillNo(billNo: Int) {
        _purchaseList.value = purchaseUseCase.getPurchasesByBillNo(billNo)

        var i=0
        while(i<purchaseList.value!!.size){
            purchaseProduct.value = productUseCase.getSalesProduct(purchaseList.value!![i].productId)

            salesUseCase.addProductToList(
                SalesProductModel(
                    purchaseProduct.value!!.id,
                    purchaseProduct.value!!.name,
                    purchaseList.value!![i].quantity,
                    purchaseList.value!![i].purchasePrice,
                    purchaseList.value!![i].purchasePrice * purchaseList.value!![i].quantity
                ),
                false
            )
            i++
        }


    }

    fun totalAmount(){
        totalBill.value = salesUseCase.totalBill()
    }

    fun totalQuantity() {
        totalQuantity.value = salesUseCase.totalQuantity()
    }

    fun totalItem(){
        totalItem.value = _product.value!!.size
    }

    // Select Supplier Work

    fun getSupplierName() : LiveData<List<String>> = supplierUseCase.getSupplierName()

    fun getSupplierDetails(supplier : String){
        viewModelScope.launch {
            val supplierDetails = supplierUseCase.getSupplierDetails(supplier)
            _supplier.value = supplierDetails
            _supplierInvoiceList.value = purchaseInvoiceUseCase.getSupplierInvoiceList(supplierDetails.id)
        }
    }

    fun getTotalResult() {
        totalInvoiceListBill()
        totalInvoiceListDue()
        totalInvoiceListPaid()
        totalPurchaseInvoiceDue()
    }

    private fun totalPurchaseInvoiceDue() {
        supplierInvoiceTotalDue.value = purchaseInvoiceUseCase.totalInvoiceDue()
    }

    private fun totalInvoiceListBill() {
        supplierInvoiceListTotalBill.value = purchaseInvoiceUseCase.totalInvoiceBill()
    }

    private fun totalInvoiceListPaid() {
        supplierInvoiceListTotalPaid.value = purchaseInvoiceUseCase.totalInvoicePayment()
    }

    private fun totalInvoiceListDue() {
        supplierInvoiceListTotalDue.value = purchaseInvoiceUseCase.totalInvoiceDue()
    }

    //Product Select Work

    fun getProductName() : LiveData<List<String>> = productUseCase.getProductNames()

    fun getProductDetails() : LiveData<Product> = productUseCase.getProductByName(name)

    fun setProduct(list : ArrayList<SalesProductModel>){
        _product.value = list
    }

    fun getProductList() {
        _product.value = salesUseCase.getProductList()
    }

    fun updateProduct(position: Int, quantity: Double, totalPrice: Double, price: Double) {
        updateProductQuantity(position,quantity)
        updateProductTotalPrice(position,totalPrice)
        updateProductPrice(position,price)
        totalAmount()
        totalItem()
        totalQuantity()
    }

    private fun updateProductTotalPrice(position: Int, totalPrice: Double) {
        _product.value?.get(position)!!.totalPrice = salesUseCase.getProductTotalPrice(position,totalPrice)
    }

    fun updateProductQuantity(position: Int, quantity: Double) {
        _product.value?.get(position)!!.quantity = salesUseCase.getProductQuantity(
            position,
            quantity
        )
    }

    fun incrementQuantity(product: SalesProductModel , increment: Boolean) {
        salesUseCase.addProductToList(product, increment)
    }

    fun delete(product: SalesProductModel) {
        deleteProduct(product)
        deleteSales(product)
        totalAmount()
        totalItem()
        totalQuantity()
    }

    private fun deleteSales(product: SalesProductModel) {
        viewModelScope.launch {
            salesUseCase.deleteSalesItem(purchaseInvoiceDetails.value?.id?:0,product.id)
//            totalAmount()
            purchaseInvoiceDetails.value?.let {
                purchaseInvoiceUseCase.updatePurchaseInvoice(
                    it.totalAmount.minus(product.totalPrice),
                    it.dueAmount.plus(currentDue.value.toString().toDouble()-product.totalPrice),
                    it.partialPayment.plus(payment.toString().toDouble()),
                    it.id
                )
            }

        }
    }

    private fun deleteProduct(product: SalesProductModel) {
        salesUseCase.deleteProduct(product)
        _product.value?.remove(product)
    }

    fun addProduct(product: SalesProductModel, increment: Boolean)  = salesUseCase.addProductToList(product,increment)


    //Purchase Confirmation Work


    fun billFromEdit(){
        if(fromEditScreen){
            totalBill.value = salesUseCase.purchaseInvoiceNewAmount(purchaseInvoiceDetails.value)
        }else{
            totalAmount()
        }
    }

    fun calculateDue(payment : Double, discount :  Double) {
        currentDue.value = "${(totalPurchaseBill.minus(payment)).minus(discount)}"
        this.payment = payment
        this.discount = discount
    }


    fun onPurchaseClick(){
        salesUseCase.arrayOfProduct()
        _navigate.value = Event(true)
    }

    fun onProductSelectClick(){
        if(supplier.value != null){
            _navigate.value = Event(true)
        }else{
            _navigate.value = Event(false)
        }
    }


    // Store Data On Confirmation Click
    fun clickOnConfirm(data: Data?) {
        data?.let {
            viewModelScope.launch {
                if(!fromEditScreen){
                    insertPurchaseInvoice(data,currentDue.value?.toDouble()?:0.0)
                    insertPurchase(data)
                    insertPayment()
                    currentDue.value?.let {
                        updateSupplierBalance(supplier,supplier.value?.openingBalance?:0.0 - payment + it.toDouble() )
                    }

                }
//                else {
//                    updatePurchaseInvoice()
//                    insertPayment()
//                    insertOrUpdatePurchase()
//                    updateSupplierBalance(
//                        supplier,
//                        supplier.value?.openingBalance!! - payment + currentDue.value!!.toDouble()-purchaseInvoiceDetails.value!!.partialPayment-purchaseInvoiceDetails.value!!.dueAmount
//                    )
//                }

                }

        }


    }

//    private suspend fun insertOrUpdatePurchase() {
//        var i = 0
//        while (i < product.value!!.size) {
//            val exist = purchaseUseCase.dataExist(billId,product.value!![i].id)
//            if(exist){
//                purchaseUseCase.updatePurchase(
//                    product.value!![i].totalPrice,
//                    product.value!![i].quantity,
//                    billId
//                )
//            }else{
//                purchaseUseCase.insertPurchase(
//                    Purchase(
//                        0,
//                        product.value?.get(i)?.id!!,
//                        "0",
//                        Calendar.getInstance().time,
//                        Calendar.getInstance().time,
//                        product.value?.get(i)?.price!!,
//                        product.value?.get(i)?.quantity!!,
//                        0.0,
//                        supplier.value?.id!!,
//                        Calendar.getInstance().time,
//                        product.value?.get(i)?.quantity!!*product.value?.get(i)?.price!!,
//                        "0",
//                        purchaseInvoiceDetails.value!!.id,
//                        purchaseInvoiceDetails.value!!.billNo.toString(),
//                        Calendar.getInstance().time,
//                        Calendar.getInstance().time,
//                        Calendar.getInstance().time
//
//                    )
//                )
//            }
//
//
//            val product = productUseCase.getProductById(_product.value!![i].id)
//
//            salesUseCase.updateAlertQuantity(
//                _product.value!![i].id,
//                product.alertQuantity + _product.value!![i].quantity
//            )
//            i++
//        }
//    }

    private suspend fun insertPurchase(data : Data?) {
        data?.let {
            var i=0
            while (i<product.value!!.size){
                purchaseUseCase.insertPurchase(
                    Purchase(
                        0,
                        product.value?.get(i)?.id?:0,
                        "0",
                        Calendar.getInstance().time,
                        Calendar.getInstance().time,
                        product.value?.get(i)?.price!!,
                        product.value?.get(i)?.quantity!!,
                        0.0,
                        data.supplier_id,
                        Calendar.getInstance().time,
                        product.value?.get(i)?.price?:0.0 * product.value?.get(i)?.quantity!! ,
                        data.supplier_id.toString(),
                       data.purchase_invoice,
                        data.purchase_invoice.toString(),
                        Calendar.getInstance().time,
                        Calendar.getInstance().time,
                        Calendar.getInstance().time

                    )
                )

                val product = productUseCase.getProductById(_product.value!![i].id)

                salesUseCase.updateAlertQuantity(
                    _product.value!![i].id,
                    product.alertQuantity + _product.value!![i].quantity
                )
                i++
            }
        }

    }

    private suspend fun insertPurchaseInvoice(data: Data, due: Double): Long {
        return purchaseInvoiceUseCase.insertPurchaseInvoice(
            PurchaseInvoice(
                0,
                Calendar.getInstance().time,
                data.total_amount,
                0.0,
                totalPurchaseBill,
                due,
                payment,
                0,
                0,
                0,
                0,
                supplier.value?.id!!,
                0,
                data.purchase_invoice,
                0.0,
                0,
                0,
                0,
                Calendar.getInstance().time,
                Calendar.getInstance().time,
                Calendar.getInstance().time
            )
        )

    }

    private suspend fun updatePurchaseInvoice() {
        purchaseInvoiceUseCase.updatePurchaseInvoice(
            totalPurchaseBill,
            totalPurchaseBill - purchaseInvoiceDetails.value!!.partialPayment - payment,
            purchaseInvoiceDetails.value!!.partialPayment + payment,
            billId
        )
    }

    private suspend fun updateSupplierBalance(supplier: LiveData<Supplier>, balance : Double) {
        supplierUseCase.updateSupplierBalance(supplier.value!!.id,balance)
    }

    private suspend fun insertPayment(){
        paymentUseCase.insertPayment(
            Payment(
                0,
                0,
                0,
                supplier.value?.id!!,
                0,
                0,
                "0",
                Calendar.getInstance().time,
                Calendar.getInstance().time,
                totalPurchaseBill,
                discount,
                totalPurchaseBill-discount,
                "",
                Calendar.getInstance().time,
                Calendar.getInstance().time,
                Calendar.getInstance().time

            )
        )
    }

    fun clickInvoice(data : Data?){
        data?.let{
            billNo()
            viewModelScope.launch {
                if (product.value!!.isNotEmpty()) {
                    if (!fromEditScreen) {
                        insertPurchaseInvoice(data, totalBill.value!!)
                        insertPurchase(data)
                        updateSupplierBalance(supplier ,supplier.value?.openingBalance!! + totalBill.value!!)

                    } else {
                        purchaseInvoiceUseCase.updatePurchaseInvoice(
                            totalPurchaseBill,
                            totalPurchaseBill - purchaseInvoiceDetails.value!!.partialPayment,
                            purchaseInvoiceDetails.value!!.partialPayment,
                            data.purchase_invoice
                        )
                        var i = 0
                        while (i < product.value!!.size) {
                            val exist = purchaseUseCase.dataExist(billId,product.value!![i].id)
                            if(exist){
                                purchaseUseCase.updatePurchase(
                                    product.value!![i].totalPrice,
                                    product.value!![i].quantity,
                                    data.purchase_invoice
                                )
                            }else{
                                purchaseUseCase.insertPurchase(
                                    Purchase(
                                        data.id,
                                        product.value?.get(i)?.id!!,
                                        "0",
                                        Calendar.getInstance().time,
                                        Calendar.getInstance().time,
                                        product.value?.get(i)?.price!!,
                                        product.value?.get(i)?.quantity!!,
                                        0.0,
                                        supplier.value?.id!!,
                                        Calendar.getInstance().time,
                                        product.value?.get(i)?.quantity!!*product.value?.get(i)?.price!!,
                                        "0",
                                        purchaseInvoiceDetails.value!!.id,
                                        purchaseInvoiceDetails.value!!.billNo.toString(),
                                        Calendar.getInstance().time,
                                        Calendar.getInstance().time,
                                        Calendar.getInstance().time

                                    )
                                )
                            }


                            val product = productUseCase.getProductById(_product.value!![i].id)

                            salesUseCase.updateAlertQuantity(
                                _product.value!![i].id,
                                product.alertQuantity + _product.value!![i].quantity
                            )
                            i++
                        }
                        updateSupplierBalance(supplier,supplier.value?.openingBalance!! + totalBill.value!! - purchaseInvoiceDetails.value!!.partialPayment+purchaseInvoiceDetails.value!!.dueAmount)
                        _navigate.value = Event(true)
                    }

                } else {
                    _navigate.value = Event(false)
                }
            }
        }

    }

    fun billNo() = billNo.apply {
        billNo = Random.nextInt(100000,999999)
    }


    fun insertPurchase() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            if(!fromEditScreen){
                if(!confirmClick){
                    emit(Resource.success(data = apiUseCase.insertPurchase(
                        salesUseCase.quantityList(),
                        salesUseCase.totalProductPrice(),
                        salesUseCase.productUnitPrice(),
                        salesUseCase.productIdArray(),
                        supplier.value!!.id,
                        totalBill.value?:0.0,
                        payment,
                        currentDue.value?.toDouble()?:0.0,
                        discount,
                        false,
                        "Without Payment"

                    )))
                }else{
                    emit(Resource.success(data = apiUseCase.insertPurchase(
                        salesUseCase.quantityList(),
                        salesUseCase.totalProductPrice(),
                        salesUseCase.productUnitPrice(),
                        salesUseCase.productIdArray(),
                        supplier.value!!.id,
                        totalBill.value?:0.0,
                        payment,
                        currentDue.value?.toDouble()?:0.0,
                        discount,
                        true,
                        "with Payment"

                    )))
                }
            }else{
                if(!confirmClick){
                    emit(Resource.success(data = apiUseCase.updatePurchase(
                        apiInvoice.value?.data?.purchase?.supplier_id?:0,
                        apiInvoice.value?.data?.purchase?.purchase_invoice?:0,
                        salesUseCase.quantityList(),
                        salesUseCase.totalProductPrice(),
                        salesUseCase.productUnitPrice(),
                        salesUseCase.productIdArray(),
                        apiInvoice.value?.data?.purchase?.supplier_id?:0,
                        totalBill.value?:0.0,
                        payment,
                        totalBill.value?:0.0,
                        0.0,
                        false,
                        "Without Payment"

                    )))
                }else{
                    emit(Resource.success(data = apiUseCase.updatePurchase(
                        apiInvoice.value?.data?.purchase?.supplier_id?:0,
                        apiInvoice.value?.data?.purchase?.purchase_invoice?:0,
                        salesUseCase.quantityList(),
                        salesUseCase.totalProductPrice(),
                        salesUseCase.productUnitPrice(),
                        salesUseCase.productIdArray(),
                        apiInvoice.value?.data?.purchase?.supplier_id?:0,
                        totalBill.value?:0.0,
                        payment,
                        currentDue.value?.toDouble()?:0.0,
                        discount,
                        true,
                        "Without Payment"
                    )))
                }
            }



        } catch (exception: Exception) {
            Timber.d("$exception")
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun insertApiPayment() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = apiUseCase.insertPayment(
                        Payment(
                            0,
                            0,
                            0,
                            supplier.value?.id!!,
                            0,
                            0,
                            "0",
                            Calendar.getInstance().time,
                            Calendar.getInstance().time,
                            totalPurchaseBill,
                            discountTv.value?.toString()?.toDouble()?:0.0,
                            totalPurchaseBill.minus(discount),
                            description.value?:"Null",
                            Calendar.getInstance().time,
                            Calendar.getInstance().time,
                            Calendar.getInstance().time
                        )
                    )
                )
            )
        }catch (exception : Exception){
            emit(Resource.error(data = null,message = exception.toString()))
        }
    }

    fun arrayOfProduct() {
        salesUseCase.arrayOfProduct()
    }

    fun setSupplierPurchaseInfo(purchaseData: PurchaseData) {
        this.purchaseData.value = purchaseData
    }

    private fun updateProductPrice(position: Int, price: Double) = _product.value?.get(position)!!.price.apply {
        salesUseCase.getProductPrice(position,price)
    }


    fun supplierValidation() : Boolean{
        _errorMessage.value = when {
            companyName.value.isNullOrEmpty() -> {
                Event("Empty Company Name")
            }
            address.value.isNullOrEmpty() -> {
                Event("Enter Address")
            }
            contactPersonName.value.isNullOrEmpty() -> {
                Event("Enter Contact Person Name")
            }
            mobile.value.isNullOrEmpty() -> {
                Event("Enter Mobile")
            }
            ownerName.value.isNullOrEmpty() -> {
                Event("Enter Owner Name")
            }
            else -> {
                return true
            }
        }
        return false
    }


    fun createSupplier() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(data = apiUseCase.createSupplier(
                    Supplier(
                        0,
                        companyName.value?:"No Name",
                        ownerName.value?:"No Owner",
                        contactPersonName.value?:"No Contact Person",
                        "",
                        "",
                        "",
                        "",
                        mobile.value?:"null",
                        address.value?:"",
                        "",
                        "",
                        0.0,
                        1,
                        Calendar.getInstance().time,
                        Calendar.getInstance().time
                    )
                ))

            )
        } catch (exception: Exception) {
            Timber.d("$exception")
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun createLocalCustomer(data: com.example.shopmanagement.data.models.api.supplier.Data?) {
        data?.let {
            viewModelScope.launch {

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
                    "",
                    data.opening_balance.toDouble(),
                    1,
                    Calendar.getInstance().getTime(),
                    Calendar.getInstance().getTime()
                )

            }.also {
                supplierDetails = data.name
                getSupplierDetailsPurchase()
                getSupplierDetails(data.name)

            }

        }

    }

    fun getSupplierDetailsPurchase(): LiveData<Supplier> {
        val supplierDetails = supplierUseCase.getSupplierDetailsPurchase(supplierDetails)
        _supplier.value = supplierDetails.value
        return supplierDetails
    }

    fun setProductForEditInvoice(){
        if(apiInvoice.value != null){
            var i=0
            while(i<apiInvoice.value!!.data.purchases.size){

                salesUseCase.addProductToList(
                    SalesProductModel(
                        apiInvoice.value!!.data.purchases[i].product_id,
                        apiInvoice.value!!.data.purchases[i].name,
                        apiInvoice.value!!.data.purchases[i].quantity.toDouble(),
                        apiInvoice.value!!.data.purchases[i].purchase_price.toDouble(),
                        apiInvoice.value!!.data.purchases[i].total_amount.toDouble()
                    ),
                    false
                )
                i++
            }

        }
    }

    fun setApiInvoice(body: ApiPurchaseDetails?) {
        _apiInvoice.value = body
    }

    fun setEditFlag() {
        fromEditScreen = true
    }

    fun getEditFlag() = fromEditScreen

    fun clearData() {
        _supplier.value = null
        _product.value = arrayListOf()
        salesUseCase.setProductList()
        fromEditScreen = false
        _apiInvoice.value = null
        _product.value = null
    }


}
package com.example.shopmanagement.ui.sales

import android.content.Context
import androidx.lifecycle.*
import com.example.shopmanagement.data.models.*
import com.example.shopmanagement.data.models.api.invoice.ApiInvoice
import com.example.shopmanagement.data.models.api.invoice.Data
import com.example.shopmanagement.data.models.api.invoice.details.ApiSpecificInvoice
import com.example.shopmanagement.data.models.api.invoice.list.InvoiceData
import com.example.shopmanagement.domain.*
import com.example.shopmanagement.domain.api.ApiUseCase
import com.example.shopmanagement.ui.utils.Event
import com.example.shopmanagement.ui.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class SelectCustomerViewModel @Inject constructor(
    private val customerUseCase: CustomerUseCase,
    private val productUseCase: ProductUseCase,
    private val salesUseCase: SalesUseCase,
    private val invoiceUseCase: InvoiceUseCase,
    private val receiptUseCase: ReceiptUseCase,
    private val apiUseCase: ApiUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    var newCustomerName = MutableLiveData<String?>()
    var address = MutableLiveData<String>()
    var mobile = MutableLiveData<String>()

    var newProductSelect: Boolean = false
    val customerInvoiceListTotalBill = MutableLiveData<Double?>()
    val customerInvoiceListTotalDue = MutableLiveData<Double>()
    val customerInvoiceListTotalPaid = MutableLiveData<Double>()
    var invoiceDetails = MutableLiveData<InvoiceData>()
    var invoiceId : Long = 0
    var confirmClick = false

    private var fromDialogEdit : Boolean = false
    private val newAmount = MutableLiveData<Double?>()
    var billNumber = MutableLiveData<Int?>()
    var fromEditScreen : Boolean = false
    var salesProduct = MutableLiveData<Product>()
    var selectedInvoice = MutableLiveData<Invoice?>()
    var validCustomer = MutableLiveData<Boolean>()
    var validProduct = MutableLiveData<Boolean>()
    var customerName = MutableLiveData<String>()
    var currentCustomer = MutableLiveData<String>()
    var productName = MutableLiveData<String>()
    var payment = MutableLiveData<String>()
    var discount = MutableLiveData<String>()
    var currentDue = MutableLiveData<String>()
    var alertQuantity = MutableLiveData<Int>()

    val response = MutableLiveData<Resource<Response<ApiInvoice>>>()

    private val _apiInvoice = MutableLiveData<ApiSpecificInvoice>()
    val apiInvoice : LiveData<ApiSpecificInvoice>
            get() = _apiInvoice


    var totalBill = MutableLiveData<Double>()
    var totalItem = MutableLiveData<Int>()
    var totalQuantity = MutableLiveData<Double>()

    var name : String = ""
    var customer = ""

    var invoiceCustomerDetails = MutableLiveData<Customer?>()

    private var _selectedCustomer = MutableLiveData<Customer?>()
    val selectedCustomer : LiveData<Customer?>
        get() = _selectedCustomer

    private val _salesByInvoiceId = MutableLiveData<List<Sales>>()
    val salesByInvoiceId : LiveData<List<Sales>>
        get() = _salesByInvoiceId

    private val _navigate = MutableLiveData<Event<Boolean>>()
    val navigate: LiveData<Event<Boolean>>
        get() = _navigate


    private val _product = MutableLiveData<ArrayList<SalesProductModel>>()
    val product : LiveData<ArrayList<SalesProductModel>>
        get() = _product

    private val _invoiceList = MutableLiveData<ArrayList<Invoice>>()
    val invoiceList : LiveData<ArrayList<Invoice>>
        get() = _invoiceList

    private val _customerInvoiceList = MutableLiveData<List<Invoice>>()
    val customerInvoiceList : LiveData<List<Invoice>>
        get() = _customerInvoiceList

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>>
        get() = _errorMessage


    fun getCustomerName() : LiveData<List<String>>  = customerUseCase.getCustomerName()

    fun getProductName() : LiveData<List<String>> = productUseCase.getProductNames()

    fun getProductDetails() : LiveData<Product> = productUseCase.getProductByName(name)

    fun getCustomerDetails() : LiveData<Customer> = customerUseCase.getCustomerByName(customer)

    fun customerFieldValidation(): Boolean {
        _errorMessage.value = when {
            newCustomerName.value.isNullOrEmpty() -> {
                Event("Empty Name")
            }
            address.value.isNullOrEmpty() -> {
                Event("Enter Address")
            }
            mobile.value.isNullOrEmpty() -> {
                Event("Enter Mobile")
            }
            else -> {
                return true
            }
        }
        return false
    }


    fun getInvoiceList() {
        viewModelScope.launch {
            _invoiceList.value = invoiceUseCase.getInvoiceList() as ArrayList<Invoice>
        }
    }

    private suspend fun getSalesByInvoiceId(id: Int){

            _salesByInvoiceId.value = salesUseCase.getSalesByInvoiceId(id)
            var i=0
            while(i<salesByInvoiceId.value!!.size){
                salesProduct.value = productUseCase.getSalesProduct(salesByInvoiceId.value!![i].productID)

                    salesUseCase.addProductToList(
                        SalesProductModel(
                            salesProduct.value!!.id,
                            salesProduct.value!!.name,
                            salesByInvoiceId.value!![i].dellQuantity,
                            salesByInvoiceId.value!![i].unitPrice,
                            salesByInvoiceId.value!![i].totalAmount
                        ),
                        false
                    )

                i++
            }

    }


    fun getProductList() {
        _product.value = salesUseCase.getProductList()
    }

    fun addProduct(product: SalesProductModel, increment: Boolean){
        salesUseCase.addProductToList(product, increment)
    }

    fun updateProductQuantity(position: Int, quantity: Double){
        _product.value?.get(position)!!.quantity = salesUseCase.getProductQuantity(
            position,
            quantity
        )
    }

    fun updateProductTotalPrice(position: Int, totalPrice: Double){
        _product.value?.get(position)!!.totalPrice = salesUseCase.getProductTotalPrice(
            position,
            totalPrice
        )
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


    fun incrementQuantity(product: SalesProductModel, increment: Boolean) {
        salesUseCase.addProductToList(product, increment)
    }

    private fun deleteProduct(product: SalesProductModel) {
        salesUseCase.deleteProduct(product)
        _product.value?.remove(product)
    }

    fun calculateDue(payment: Double, discount: Double) {
        currentDue.value = "${(totalBill.value?.minus(payment))?.minus(discount)}"
        this.payment.value = payment.toString()
    }

    fun billNumber(): Int {
        billNumber.value = Random.nextInt(100000, 999999)
        return billNumber.value as Int
    }

    private suspend fun editInvoice(invoice: InvoiceData, customerId: Int){
        selectedInvoice.value = invoiceUseCase.getInvoiceById(invoice.id)
        newAmount.value = selectedInvoice.value?.totalAmount
        invoiceCustomerDetails.value =  customerUseCase.getCustomerById(customerId)
        customer = invoiceCustomerDetails.value?.customerName?:""

        invoiceCustomerDetails.value?.let {
            setSelectedCustomer(it)
        }


    }

    fun insertLocal(data: Data?) {
        billNumber()
        data?.let {
            viewModelScope.launch {
                if(!fromEditScreen){
//                    val id =  insertInvoiceLocal(totalBill.value!!,data)
                    invoiceUseCase.insertInvoiceToLocalDb(data, currentDue.value, payment.value)
                    invoiceId = data.id.toLong()
                    insertLocalReceipt()
                    insertSales(data.id.toLong())

                    totalBill.value?.let {
                        customerUseCase.updateBalance(
                            selectedCustomer.value?.id ?: 0,
                            selectedCustomer.value?.customerOpeningBalance ?: 0.0 + it
                        )
                    }

                }else{
                    salesUseCase.updateInvoice(
                        selectedInvoice.value!!,
                        currentDue.value.toString().toDouble(),
                        payment.value.toString().toDouble()
                    )
                    salesUseCase.updateSales(selectedInvoice.value!!)

                    customerUseCase.updateBalance(
                        selectedCustomer.value?.id ?: 0,
                        selectedCustomer.value!!.customerOpeningBalance + totalBill.value!! - selectedInvoice.value!!.partialPayment + selectedInvoice.value!!.dueAmount
                    )
                }

            }
        }

    }

    fun insertLocalReceipt(){

        viewModelScope.launch {
            if(!fromEditScreen){
//                insertInvoiceLocal(currentDue.value!!.toDouble(),)
               val id = 0L
                insertSales(id)
                customerBalanceAfterPayment()
                insertingReceipt(id)
                resetValues()
            }else{

                salesUseCase.updateInvoice(
                    selectedInvoice.value!!,
                    currentDue.value.toString().toDouble(),
                    payment.value.toString().toDouble()
                )

                insertingReceipt(invoiceCustomerDetails.value?.id!!.toLong())

                salesUseCase.updateSales(selectedInvoice.value!!)
                customerUseCase.updateBalance(
                    selectedCustomer.value?.id ?: 0,
                    selectedCustomer.value!!.customerOpeningBalance + totalBill.value!! + selectedInvoice.value!!.dueAmount
                )
            }

        }


        _navigate.value = Event(true)
    }

    private suspend fun insertingReceipt(id: Long) {
        receiptUseCase.insertReceipt(
            Receipt(
                0,
                0,
                id.toString().toInt(),
                "0",
                0,
                0,
                selectedCustomer.value!!.id,
                0,
                "0",
                Calendar.getInstance().time,
                Calendar.getInstance().time,
                Calendar.getInstance().time,
                totalBill.value!! - discount.value!!.toDouble(),
                0.0,
                0.0,
                totalBill.value!!,
                discount.value!!.toDouble(),
                totalBill.value!! - discount.value!!.toDouble(),
                "0",
                Calendar.getInstance().time,
                Calendar.getInstance().time,
                Calendar.getInstance().time

            )
        )
    }

    //Update Customer Balance After Payment
    private suspend fun customerBalanceAfterPayment() {
        customerUseCase.updateBalance(
            selectedCustomer.value!!.id,
            selectedCustomer.value!!.customerOpeningBalance + currentDue.value!!.toDouble() - payment.value!!.toDouble()
        )
    }

    private suspend fun insertSales(id: Long) {
        var i=0
        while (i<_product.value!!.size){

            salesUseCase.insertSales(
                Sales(
                    0,
                    _product.value!![i].id,
                    billNumber.value.toString(),
                    _product.value!![i].price,
                    _product.value!![i].quantity,
                    id.toInt(),
                    _product.value!![i].totalPrice,
                    Calendar.getInstance().time,
                    Calendar.getInstance().time,
                    Calendar.getInstance().time
                )
            )

            val product = productUseCase.getProductById(_product.value!![i].id)

            salesUseCase.updateAlertQuantity(
                _product.value!![i].id,
                product.alertQuantity - _product.value!![i].quantity
            )

            i++
        }
    }



    fun resetValues() {
        _selectedCustomer.value = null
        validCustomer.value = false
        validProduct.value = false
        customerName.value = ""
        productName.value = ""
        payment.value = "0"
        discount.value = "0"
        currentDue.value = "0"
        alertQuantity.value  = 0

        totalBill.value = 0.0
        totalItem.value = 0
        totalQuantity.value = 0.0

        name  = ""
        customer = ""

        salesUseCase.setBill()
        salesUseCase.setProductList()
        salesUseCase.setQuantity()
        invoiceUseCase.clearInvoiceList()
        clearTotal()
        clearCustomer()
        clearProductList()
    }

    fun deleteInvoice(invoice: InvoiceData, position: Int) {
        invoiceList.value?.removeAt(position)
        viewModelScope.launch {
            invoiceUseCase.deleteInvoice(invoice)
            salesUseCase.deleteSales(invoice.bill_no)
        }
    }

    private fun deleteSales(product: SalesProductModel) {

        if(fromEditScreen){
            viewModelScope.launch {
                salesUseCase.deleteSalesItem(selectedInvoice.value!!.id, product.id)
                invoiceUseCase.updateInvoice(
                    selectedInvoice.value?.totalAmount!!.minus(product.totalPrice),
                    selectedInvoice.value?.dueAmount!!.plus(
                        currentDue.value.toString().toDouble() - product.totalPrice
                    ),
                    selectedInvoice.value?.partialPayment!!.plus(
                        payment.value.toString().toDouble()
                    ),
                    selectedInvoice.value?.billNo!!
                )
            }
        }

    }

    fun setSelectedCustomer(customer: Customer){
        _selectedCustomer.value = customer
        viewModelScope.launch {
            _customerInvoiceList.value = invoiceUseCase.getCustomerInvoiceList(customer.id) as ArrayList<Invoice>
        }

    }


    fun billFromEdit(){
        if(fromEditScreen){
            totalBill.value = salesUseCase.invoiceNewAmount(selectedInvoice)

        }else{
            totalAmount()
        }
    }

//    fun invoiceUpdate(invoice: InvoiceData) {
//        fromDialogEdit = true
//        fromEditScreen = true
//        salesUseCase.product.clear()
//
//        viewModelScope.launch {
//            getSalesByInvoiceId(invoice.id)
//            editInvoice(invoice, invoice.customer_id)
//            totalQuantity()
//            getProductList()
//        }
//
//    }

    fun delete(product: SalesProductModel) {
        deleteProduct(product)
        deleteSales(product)
        totalAmount()
        totalItem()
        totalQuantity()
    }

//    fun invoiceEdit(invoice: InvoiceData) {
//        fromEditScreen = true
//        fromDialogEdit = true
//        salesUseCase.product.clear()
//        viewModelScope.launch {
//            getSalesByInvoiceId(invoice.id)
//            editInvoice(invoice, invoice.customer_id)
//
//            totalQuantity()
//            getProductList()
//        }
//
//    }

    private fun totalInvoiceListBill(){
        customerInvoiceListTotalBill.value = invoiceUseCase.totalInvoiceBill()
    }

    private fun totalInvoiceListDue(){
        customerInvoiceListTotalDue.value = invoiceUseCase.totalInvoiceDue()
    }

    private fun totalInvoiceListPaid(){
        customerInvoiceListTotalPaid.value = invoiceUseCase.totalInvoicePayment()
    }

    fun getTotalResult() {
        totalInvoiceListBill()
        totalInvoiceListDue()
        totalInvoiceListPaid()
    }

    private fun clearInvoiceTotalBill(){
        customerInvoiceListTotalBill.value = 0.0
    }

    private fun clearInvoiceTotalPaid(){
        customerInvoiceListTotalPaid.value = 0.0
    }

    fun clearProductList() = product.apply {
        product.value?.clear()
    }

    private fun clearCustomer() = _selectedCustomer.apply {
        _selectedCustomer.value = null
    }

    private fun clearTotal(){
        clearInvoiceTotalBill()
//        clearInvoiceTotalDue()
        clearInvoiceTotalPaid()
    }

//    fun showDetails(invoice: Invoice) {
//        fromEditScreen = false
//        fromDialogEdit = false
//        salesUseCase.product.clear()
//        viewModelScope.launch {
//            getSalesByInvoiceId(invoice.id)
//            editInvoice(invoice,invoice.customerId)
//            totalQuantity()
//            getProductList()
//        }
//    }

    // Data Clear from Select Customer Fragment For Creating New Invoice
    fun clearData() {
        fromEditScreen = false
        fromDialogEdit = false
        clearCustomer()
        clearProductList()
        clearTotal()
        salesUseCase.setProductList()
    }


    /**Insert Invoice**/
    fun insertInvoice() = liveData(Dispatchers.IO){

        emit(Resource.loading(data = null))
        try {
            emit(
                if (!fromDialogEdit) {
                    if (!confirmClick) {
                        Timber.d("Without Payment")
                        Resource.success(
                            data = apiUseCase.insertInvoice(
                                salesUseCase.quantityList(),
                                salesUseCase.totalProductPrice(),
                                salesUseCase.productUnitPrice(),
                                salesUseCase.productIdArray(),
                                selectedCustomer.value!!.id,
                                totalBill.value ?: 0.0,
                                0.0,
                                totalBill.value ?: 0.0,
                                0.0,
                                false,
                                "Without Payment"
                            )
                        )
                    } else {
                        Timber.d("With Payment")
                        Resource.success(
                            data = apiUseCase.insertInvoice(
                                salesUseCase.quantityList(),
                                salesUseCase.totalProductPrice(),
                                salesUseCase.productUnitPrice(),
                                salesUseCase.productIdArray(),
                                selectedCustomer.value!!.id,
                                totalBill.value ?: 0.0,
                                payment.value?.toDouble() ?: 0.0,
                                currentDue.value?.toDouble() ?: 0.0,
                                discount.value?.toDouble() ?: 0.0,
                                true,
                                "With Payment"
                            )
                        )
                    }
                } else {
                    if (!confirmClick) {
                        Timber.d("Without Payment")
                        Resource.success(
                            data = apiUseCase.updateInvoice(
                                apiInvoice.value?.data?.invoice?.id?:0,
                                salesUseCase.quantityList(),
                                salesUseCase.totalProductPrice(),
                                salesUseCase.productUnitPrice(),
                                salesUseCase.productIdArray(),
                                selectedCustomer.value!!.id,
                                totalBill.value ?: 0.0,
                                0.0,
                                totalBill.value ?: 0.0,
                                0.0,
                                false,
                                "Without Payment"
                            )
                        )
                    } else {
                        Timber.d("With Payment")
                        Resource.success(
                            data = apiUseCase.updateInvoice(
                                apiInvoice.value?.data?.invoice?.id?:0,
                                salesUseCase.quantityList(),
                                salesUseCase.totalProductPrice(),
                                salesUseCase.productUnitPrice(),
                                salesUseCase.productIdArray(),
                                selectedCustomer.value!!.id,
                                apiInvoice.value?.data?.invoice?.total_amount?.plus(totalBill.value ?: 0.0)
                                    ?: 0.0,
                                apiInvoice.value?.data?.invoice?.partial_payment?.plus(
                                    payment.value?.toDouble() ?: 0.0
                                ) ?: 0.0,
                                apiInvoice.value?.data?.invoice?.due_amount?.plus(
                                    currentDue.value?.toDouble() ?: 0.0
                                ) ?: 0.0,
                                discount.value?.toDouble() ?: 0.0,
                                true,
                                "With Payment"
                            )
                        )
                    }
                }

            )

        } catch (exception: Exception) {
            Timber.d("$exception")
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    /**Insert Receipt**/
    fun insertReceipt() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = receiptUseCase.insertApiReceipt(
                        Receipt(
                            0,
                            0,
                            invoiceId.toInt(),
                            "0",
                            0,
                            0,
                            selectedCustomer.value!!.id,
                            0,
                            "0",
                            Calendar.getInstance().time,
                            Calendar.getInstance().time,
                            Calendar.getInstance().time,
                            totalBill.value!! - discount.value!!.toDouble(),
                            0.0,
                            0.0,
                            totalBill.value ?: 0.0,
                            discount.value?.toDouble() ?: 0.0,
                            totalBill.value ?: 0.0 - discount.value?.toDouble()!!,
                            "0",
                            Calendar.getInstance().time,
                            Calendar.getInstance().time,
                            Calendar.getInstance().time
                        )

                    )
                )
            )
        } catch (exception: Exception) {
            Timber.d("$exception")
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }




    fun arrayOfProduct() {
        salesUseCase.arrayOfProduct()
    }

    fun setInvoiceDetails(invoice: InvoiceData) {
        invoiceDetails.value = invoice
    }

    fun updateProductPrice(position: Int, price: Double) = _product.value?.get(position)!!.price.apply {
        salesUseCase.getProductPrice(position, price)
    }

    fun createUser() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = apiUseCase.createCustomer(
                        Customer(
                            0,
                            newCustomerName.value ?: "NO Name",
                            address.value ?: "No Address",
                            mobile.value ?: "No Mobile",
                            "Unknown",
                            "unknown",
                            "unknown",
                            "unknown",
                            0.0,
                            "1"
                        )
                    )
                )
            )
        } catch (exception: Exception) {
            Timber.d("$exception")
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun createLocalCustomer(customerResponse: com.example.shopmanagement.data.models.api.customer.Data?){

        customerResponse?.let {

            viewModelScope.launch {
                customerUseCase.insertCustomer(
                    Customer(
                        customerResponse.id,
                        customerResponse.name,
                        customerResponse.address,
                        customerResponse.mobile,
                        "1",
                        customerResponse.nid,
                        customerResponse.vat_no,
                        customerResponse.email,
                        customerResponse.opening_balance,
                        customerResponse.receipt_type.toString()
                    )
                )

            }.also {
                customer = customerResponse.name
                currentCustomer.value = customerResponse.name
                _selectedCustomer.value = getCustomerDetails().value
                validCustomer.value = true

            }

        }

    }

    fun setProductForEditInvoice(){
        if(apiInvoice.value != null){
            var i=0
            while(i<apiInvoice.value!!.data.sales.size){

                    salesUseCase.addProductToList(
                        SalesProductModel(
                            apiInvoice.value!!.data.sales[i].product_id,
                            apiInvoice.value!!.data.sales[i].name,
                            apiInvoice.value!!.data.sales[i].dell_quantity.toDouble(),
                            apiInvoice.value!!.data.sales[i].unit_price.toDouble(),
                            apiInvoice.value!!.data.sales[i].total_amount.toDouble()
                        ),
                        false
                    )
                i++
            }

        }
    }

    fun setApiInvoice(body: ApiSpecificInvoice?) {
        _apiInvoice.value = body
    }

    fun setEditFlag() {
        fromDialogEdit = true
    }

    fun getEditFlag() = fromDialogEdit

    fun setApiCustomer() {
        if(apiInvoice != null){
            customer = apiInvoice.value?.data?.customer?.name?:""
            _selectedCustomer.value = getCustomerDetails().value
        }
    }




}
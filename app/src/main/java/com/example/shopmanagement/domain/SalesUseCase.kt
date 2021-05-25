package com.example.shopmanagement.domain

import androidx.lifecycle.MutableLiveData
import com.example.shopmanagement.data.models.Invoice
import com.example.shopmanagement.data.models.PurchaseInvoice
import com.example.shopmanagement.data.models.Sales
import com.example.shopmanagement.data.models.SalesProductModel
import com.example.shopmanagement.data.repositories.InvoiceRepository
import com.example.shopmanagement.data.repositories.ProductRepository
import com.example.shopmanagement.data.repositories.SalesRepository
import java.util.*
import javax.inject.Inject

class SalesUseCase @Inject constructor(
    private val salesRepository: SalesRepository,
    private val productRepository: ProductRepository,
    private val invoiceRepository: InvoiceRepository) {
    var product : ArrayList<SalesProductModel> = arrayListOf()

    var productIdList : ArrayList<Int> = arrayListOf()
    var dellQuantity : ArrayList<Double> = arrayListOf()
    var unitPriceList : ArrayList<Double> = arrayListOf()
    var totalPriceList : ArrayList<Double> = arrayListOf()

    var totalBill = 0.0
    var quantity = 0.0
    var updateDue = 0.0
    var updatePaid = 0.0



    fun addProductToList(product: SalesProductModel, increment: Boolean){
        this.product.find { it.id == product.id }?.apply {
            if(increment){
                quantity += 1.0
            }else{
                quantity = product.quantity
            }

        }?: this.product.add(product)
    }

    fun setQuantityFromDialog(product: SalesProductModel, qty: Double){
        this.product.find { it.id == product.id }?.apply {
            quantity = qty
        }?: this.product.add(product)
    }

    fun getProductList(): ArrayList<SalesProductModel> = product

    fun deleteProduct(product: SalesProductModel) {
        this.product.remove(product)
    }

    fun totalBill(): Double {
        var totalBill = 0.0

        product.map {
            totalBill += it.totalPrice

        }

        return totalBill
    }

    fun arrayOfProduct(){
        clearData()
        product.map {
            totalPriceList.add(it.totalPrice)
            productIdList.add(it.id)
            dellQuantity.add(it.quantity.toDouble())
            unitPriceList.add(it.price)
        }
    }

    private fun clearData() {
        totalPriceList.clear()
        productIdList.clear()
        dellQuantity.clear()
        unitPriceList.clear()
    }

    fun totalQuantity(): Double {
        var quantity = 0.0
        product.map {
            quantity += it.quantity


        }
        return quantity
    }

    fun getProductQuantity(position: Int, quantity: Double): Double {
        product[position].quantity = quantity
        return product[position].quantity
    }

    fun getProductTotalPrice(position: Int, totalPrice: Double): Double {
        product[position].totalPrice = totalPrice
        return product[position].totalPrice
    }

    fun setProductList() = product.clear()

    fun setBill() = totalBill.apply {
        totalBill = 0.0
    }

    fun setQuantity() = quantity.apply {
        quantity = 0.0
    }

    fun invoiceNewAmount(selectedInvoice: MutableLiveData<Invoice?>): Double? {
        var newAmount = 0.0
        selectedInvoice.value?.let {
            newAmount = totalBill() - it.totalAmount
        }
        return newAmount
    }

    suspend fun addSalesProductToDatabase(sales : Sales) = salesRepository.addSalesProductToDatabase(sales)

    suspend fun insertSales(sales: Sales) = salesRepository.insertSales(sales)

    suspend fun updateAlertQuantity(id: Int, quantity: Double) = productRepository.updateAlertQuantity(id,quantity)

    suspend fun getSalesByInvoiceId(id : Int) : List<Sales> = salesRepository.getSalesByInvoiceId(id)

    suspend fun updateSales(invoice: Invoice) {
        var i=0
        while (i<product.size){
            if(salesRepository.dataExist(invoice.id,product[i].id)){
                salesRepository.updateSales(
                    product[i].quantity,
                    product[i].totalPrice,
                    invoice.id,
                    product[i].id
                )
            }else{
                salesRepository.insertSales(
                    Sales(
                        0,
                        product[i].id,
                        invoice.billNo.toString(),
                        product[i].price,
                        product[i].quantity,
                        invoice.id,
                        product[i].totalPrice,
                        Calendar.getInstance().time,
                        Calendar.getInstance().time,
                        Calendar.getInstance().time
                    )
                )
            }
            i++
        }
    }

    suspend fun deleteSales(id: Int)  = salesRepository.deleteSales(id)

    suspend fun deleteSalesItem(invoiceId : Int, productId : Int) = salesRepository.deleteSalesItem(invoiceId,productId)

    suspend fun updateInvoice(invoice: Invoice, due : Double, payment : Double) {

        updateDue = totalBill()-invoice.partialPayment-payment
        updatePaid = invoice.partialPayment+payment
        totalBill = totalBill()

        invoiceRepository.updateInvoice(
            totalBill,
            updateDue,
            updatePaid,
            invoice.id
        )
    }

    fun purchaseInvoiceNewAmount(purchaseInvoice: PurchaseInvoice?): Double {
        var newAmount = 0.0
        purchaseInvoice?.let {
            newAmount = totalBill() - it.totalAmount
        }
        return newAmount
    }


    /**Invoice Calculation for post **/
    fun productIdArray() = productIdList.toList()
    fun quantityList() = dellQuantity.toList()
    fun productUnitPrice() = unitPriceList.toList()
    fun totalProductPrice() = totalPriceList.toList()

    fun getUpdateInvoiceBillInfo(invoice: Invoice, due : Double, payment : Double) {
        updateDue = totalBill()-invoice.partialPayment-payment
        updatePaid = invoice.partialPayment+payment
        totalBill = totalBill()
    }

    fun getProductPrice(position: Int, price: Double): Double {
        product[position].price = price
        return product[position].price
    }


}
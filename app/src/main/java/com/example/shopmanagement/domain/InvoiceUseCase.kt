package com.example.shopmanagement.domain

import androidx.lifecycle.MutableLiveData
import com.example.shopmanagement.data.api.ApiHelper
import com.example.shopmanagement.data.models.Invoice
import com.example.shopmanagement.data.models.api.invoice.Data
import com.example.shopmanagement.data.models.api.invoice.list.ApiInvoiceList
import com.example.shopmanagement.data.models.api.invoice.list.InvoiceData
import com.example.shopmanagement.data.repositories.InvoiceRepository
import retrofit2.Response
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class InvoiceUseCase @Inject constructor(
    private val invoiceRepository: InvoiceRepository,
    private val apiHelper: ApiHelper
) {

    private var invoiceListLocal : List<Invoice> = listOf()
    private var invoiceList : ArrayList<InvoiceData> = arrayListOf()
    private val list : MutableLiveData<ArrayList<InvoiceData>> by lazy {
        MutableLiveData<ArrayList<InvoiceData>>()
    }

    private var invoiceTotal = 0.0


    suspend fun insertInvoiceToDatabase(invoice : Invoice) = invoiceRepository.insertInvoiceToDatabase(invoice)

    suspend fun insertInvoice(invoice: Invoice):Long = invoiceRepository.insertInvoice(invoice)

    suspend fun invoiceId() : Int = invoiceRepository.invoiceId()

    suspend fun getInvoiceById(id :Int) : Invoice = invoiceRepository.getInvoiceById(id)

    suspend fun getInvoiceList() : List<Invoice> {
        invoiceListLocal = invoiceRepository.getInvoiceList()

        return invoiceListLocal
    }

    suspend fun deleteInvoice(invoice: InvoiceData) = invoiceRepository.deleteInvoice(invoice)

    suspend fun updateInvoice(
        totalAmount: Double,
        totalDue: Double,
        totalPaid: Double,
        id : Int
    ) = invoiceRepository.updateInvoice(totalAmount,totalDue,totalPaid,id)

    suspend fun getCustomerInvoiceList(id: Int): List<Invoice>? {
        invoiceListLocal = invoiceRepository.getCustomerInvoiceList(id)
        return  invoiceListLocal
    }

    fun totalInvoiceBill(): Double {
        var total = 0.0
        invoiceListLocal.map {
            total += it.totalAmount
        }

        return total
    }

    fun totalInvoiceDue(): Double {
        var total = 0.0
        invoiceListLocal.map {
            total += it.dueAmount
        }

        return total
    }

    fun totalInvoicePayment(): Double {
        var total = 0.0
        invoiceListLocal.map {
            total += it.partialPayment
        }

        return total
    }

    fun clearInvoiceList() = invoiceListLocal.apply {
        invoiceListLocal = listOf()
    }

    suspend fun insertInvoiceToLocalDb(data: Data, due : String?, payment : String?) {
        insertInvoice(
            Invoice(
                data.id,
                Calendar.getInstance().time,
                data.total_amount,
                data.vat_amount,
                data.total_amount,
                due?.toDouble()?:0.0,
                payment?.toDouble()?:0.0,
                data.payment_transaction_status,
                0,
                0,
                0,
                data.customer_id,
                0,
                data.bill_no,
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


    suspend fun searchInvoice(search: String, fromDate: String, toDate: String, billNo: String,page : Int) = apiHelper.searchInvoice(search,fromDate,toDate,billNo,page)

    suspend fun getUserInvoiceList(page : Int): Response<ApiInvoiceList> {
        val response = apiHelper.getUserInvoiceList(page)
        val list = response.body()?.data?.data

        if(list != null){
            invoiceList = (invoiceList + list) as ArrayList<InvoiceData>
        }


        return response
    }

    fun getTotal(): Double {
        invoiceList.map {
            invoiceTotal += it.total_amount
        }
        return invoiceTotal
    }

    /**Returns Total Amount Of Invoice amount column**/
    fun getInvoiceTotal(): Double {
        return invoiceTotal
    }

    fun deleteInvoiceFromList(invoice: InvoiceData){
        invoiceList.remove(invoice)
    }

    fun invoiceList() = invoiceList

    /**Delete Invoice From Web Server**/
    suspend fun deleteWebInvoice(invoice: InvoiceData) = apiHelper.deleteInvoice(invoice.id)
    fun setInvoiceUseCase(list: List<InvoiceData>) {
        invoiceList = ArrayList(list)
    }



}
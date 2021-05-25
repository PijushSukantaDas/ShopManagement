package com.example.shopmanagement.ui.sales.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shopmanagement.data.models.api.invoice.list.InvoiceData
import com.example.shopmanagement.domain.api.ApiInvoiceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InvoiceDetailsViewModel @Inject constructor(
    private val apiInvoiceUseCase: ApiInvoiceUseCase
) : ViewModel() {
    var invoiceDetails = MutableLiveData<InvoiceData>()

    fun setInvoiceDetails(invoiceData: InvoiceData){
        invoiceDetails.value = invoiceData
    }

    fun getInvoiceDetails(invoiceId: Int) = apiInvoiceUseCase.getSpecificInvoiceDetails(invoiceId)
}
package com.example.shopmanagement.ui.purchase.details

import androidx.lifecycle.ViewModel
import com.example.shopmanagement.domain.PurchaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PurchaseDetailsViewModel @Inject constructor(
    private val purchaseUseCase: PurchaseUseCase
) : ViewModel() {

    fun getPurchaseDetails(purchaseId: Int)  = purchaseUseCase.getSpecificPurchaseDetails(purchaseId)
}
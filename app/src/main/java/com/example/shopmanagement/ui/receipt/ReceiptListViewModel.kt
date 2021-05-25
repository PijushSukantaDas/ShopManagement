package com.example.shopmanagement.ui.receipt

import androidx.lifecycle.ViewModel
import com.example.shopmanagement.domain.ReceiptUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReceiptListViewModel @Inject constructor(
    private val receiptUseCase: ReceiptUseCase
) : ViewModel(){


}
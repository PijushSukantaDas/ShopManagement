package com.example.shopmanagement.ui.sales.payment

import androidx.lifecycle.ViewModel
import com.example.shopmanagement.domain.api.ApiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SalesPaymentViewModel @Inject constructor(
    private val apiUseCase: ApiUseCase
) : ViewModel(){

}
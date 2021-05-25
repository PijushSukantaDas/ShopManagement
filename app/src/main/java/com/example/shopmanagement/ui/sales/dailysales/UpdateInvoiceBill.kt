package com.example.shopmanagement.ui.sales.dailysales

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shopmanagement.BaseFragment
import com.example.shopmanagement.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateInvoiceBill : BaseFragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_invoice_bill, container, false)
    }

}
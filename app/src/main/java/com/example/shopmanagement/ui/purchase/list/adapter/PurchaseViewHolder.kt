package com.example.shopmanagement.ui.purchase.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shopmanagement.R
import com.example.shopmanagement.data.models.api.purchase.PurchaseData
import com.example.shopmanagement.databinding.PurchaseListLayoutBinding
import com.example.shopmanagement.ui.purchase.IPurchaseListener

class PurchaseViewHolder(private val binding: PurchaseListLayoutBinding,private val listener: IPurchaseListener) : RecyclerView.ViewHolder(binding.root){

    fun bind(purchaseData: PurchaseData, position: Int) {
        binding.invoice = purchaseData
        binding.position = "${position + 1}"
        binding.editBtn.setOnClickListener {
            listener.editInvoice(purchaseData)
        }

        binding.deleteBtn.setOnClickListener {
            listener.deleteInvoice(purchaseData,position)
        }


        binding.root.setOnClickListener {
            listener.showPurchaseDetails(purchaseData)
        }

    }



    companion object {
        fun create(parent: ViewGroup, listener: IPurchaseListener): PurchaseViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = DataBindingUtil.inflate<PurchaseListLayoutBinding>(
                layoutInflater,
                R.layout.purchase_list_layout,
                parent,
                false
            )
            return PurchaseViewHolder(binding,listener)
        }
    }
}
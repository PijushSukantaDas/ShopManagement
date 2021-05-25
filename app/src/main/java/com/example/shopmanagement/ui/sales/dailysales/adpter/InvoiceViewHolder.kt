package com.example.shopmanagement.ui.sales.dailysales.adpter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shopmanagement.R
import com.example.shopmanagement.data.models.api.invoice.list.InvoiceData
import com.example.shopmanagement.databinding.InvoiceLayoutBinding
import com.example.shopmanagement.ui.sales.dailysales.InvoiceListener

class InvoiceViewHolder(private val binding: InvoiceLayoutBinding, private val listener: InvoiceListener) : RecyclerView.ViewHolder(binding.root){
    fun bind(invoice : InvoiceData, position: Int) {
        binding.invoice = invoice
        binding.position = "${position+1}"
        binding.editBtn.setOnClickListener {
            listener.editInvoice(invoice)
        }


        binding.deleteBtn.setOnClickListener {
            listener.deleteInvoice(invoice,position)
        }

        binding.root.setOnClickListener {
            listener.showInvoiceDetails(invoice)
        }
    }

    companion object {
        fun create(parent: ViewGroup,listener: InvoiceListener): InvoiceViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = DataBindingUtil.inflate<InvoiceLayoutBinding>(layoutInflater, R.layout.invoice_layout,parent,false)
            return InvoiceViewHolder(binding,listener)
        }
    }

}
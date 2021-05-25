package com.example.shopmanagement.ui.sales.dailysales

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.shopmanagement.data.models.api.invoice.list.InvoiceData
import com.example.shopmanagement.ui.sales.dailysales.adpter.InvoiceViewHolder

class InvoiceAdapter(private val listener: InvoiceListener) : PagingDataAdapter<InvoiceData,InvoiceViewHolder>(REPO_COMPARATOR) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InvoiceViewHolder {
        return InvoiceViewHolder.create(parent,listener)
    }

    override fun onBindViewHolder(holder: InvoiceViewHolder, position: Int) {
        val invoice = getItem(position)
        if(invoice != null){
            holder.bind(invoice,position)
        }
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<InvoiceData>() {
            override fun areItemsTheSame(oldItem: InvoiceData, newItem: InvoiceData): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: InvoiceData, newItem: InvoiceData): Boolean =
                oldItem == newItem
        }
    }

}
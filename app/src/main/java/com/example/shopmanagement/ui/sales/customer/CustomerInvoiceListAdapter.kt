package com.example.shopmanagement.ui.sales.customer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shopmanagement.R
import com.example.shopmanagement.data.models.Invoice
import com.example.shopmanagement.databinding.CustomerInvoiceListLayoutBinding

class CustomerInvoiceListAdapter(private val list : List<Invoice>, private val listener: Listener) :RecyclerView.Adapter<CustomerInvoiceListAdapter.InvoiceViewHolder>() {

    class InvoiceViewHolder(
        private val binding: CustomerInvoiceListLayoutBinding,
        private val listener: Listener
    ) : RecyclerView.ViewHolder(binding.root){
        fun bind(invoice: Invoice, position: Int) {
            binding.invoice= invoice

            binding.root.setOnClickListener {
                listener.showDetails(invoice)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomerInvoiceListAdapter.InvoiceViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<CustomerInvoiceListLayoutBinding>(layoutInflater, R.layout.customer_invoice_list_layout,parent,false)
        return InvoiceViewHolder(binding,listener)
    }


    override fun onBindViewHolder(holder: InvoiceViewHolder, position: Int) {
        holder.bind(list[position],position)
    }

    override fun getItemCount(): Int = list.size
}
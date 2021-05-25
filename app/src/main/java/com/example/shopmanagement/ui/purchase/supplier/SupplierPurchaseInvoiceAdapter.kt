package com.example.shopmanagement.ui.purchase.supplier

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shopmanagement.R
import com.example.shopmanagement.data.models.PurchaseInvoice
import com.example.shopmanagement.databinding.SupplierInvoiceListLayoutBinding

class SupplierPurchaseInvoiceAdapter(private val list : List<PurchaseInvoice>,private val listener: SupplierPurchaseListener):RecyclerView.Adapter<SupplierPurchaseInvoiceAdapter.PurchaseInvoiceViewHolder>() {

    class PurchaseInvoiceViewHolder(private val binding: SupplierInvoiceListLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(purchaseInvoice: PurchaseInvoice, listener: SupplierPurchaseListener) {
            binding.invoice = purchaseInvoice
            binding.root.setOnClickListener {
                listener.onSupplierInvoiceClick(purchaseInvoice)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SupplierPurchaseInvoiceAdapter.PurchaseInvoiceViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<SupplierInvoiceListLayoutBinding>(layoutInflater, R.layout.supplier_invoice_list_layout,parent,false)
        return SupplierPurchaseInvoiceAdapter.PurchaseInvoiceViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SupplierPurchaseInvoiceAdapter.PurchaseInvoiceViewHolder,
        position: Int
    ) {
      holder.bind(list[position],listener)
    }

    override fun getItemCount(): Int = list.size

}
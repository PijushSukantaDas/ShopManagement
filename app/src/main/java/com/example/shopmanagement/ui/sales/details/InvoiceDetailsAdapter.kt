package com.example.shopmanagement.ui.sales.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shopmanagement.R
import com.example.shopmanagement.data.models.api.invoice.details.Sale
import com.example.shopmanagement.databinding.InvoiceDetailsProductLayoutBinding

class InvoiceDetailsAdapter(private val list : List<Sale>) : RecyclerView.Adapter<InvoiceDetailsAdapter.InvoiceViewHolder>() {

    class InvoiceViewHolder(private val binding: InvoiceDetailsProductLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(product: Sale, position: Int) {
            binding.id.text = product.id.toString()
            binding.productName.text = product.name
            binding.productPrice.text = product.unit_price.toString()
            binding.totalAmount.text = product.total_amount.toString()
            binding.quantity.text = product.dell_quantity.toString()
//            binding.id.text = "${position+1}"
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InvoiceDetailsAdapter.InvoiceViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<InvoiceDetailsProductLayoutBinding>(layoutInflater, R.layout.invoice_details_product_layout,parent,false)
        return InvoiceViewHolder(binding)
    }


    override fun onBindViewHolder(holder: InvoiceViewHolder, position: Int) {
        holder.bind(list[position],position)
    }

    override fun getItemCount(): Int = list.size
}
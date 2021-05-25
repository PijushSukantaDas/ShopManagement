package com.example.shopmanagement.ui.purchase.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shopmanagement.R
import com.example.shopmanagement.data.models.api.purchase.details.Purchases
import com.example.shopmanagement.databinding.InvoiceDetailsProductLayoutBinding

class PurchaseDetailsAdapter (private val list : List<Purchases>) : RecyclerView.Adapter<PurchaseDetailsAdapter.InvoiceViewHolder>() {

    class InvoiceViewHolder(private val binding: InvoiceDetailsProductLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(product: Purchases, position: Int) {
            binding.id.text = product.id.toString()
            binding.productName.text = product.name
            binding.productPrice.text = product.purchase_price.toString()
            binding.totalAmount.text = product.total_amount.toString()
            binding.quantity.text = product.quantity.toString()

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PurchaseDetailsAdapter.InvoiceViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<InvoiceDetailsProductLayoutBinding>(layoutInflater, R.layout.invoice_details_product_layout,parent,false)
        return InvoiceViewHolder(binding)
    }


    override fun onBindViewHolder(holder: InvoiceViewHolder, position: Int) {

        holder.bind(list[position],position)
    }

    override fun getItemCount(): Int = list.size
}

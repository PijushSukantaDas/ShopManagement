package com.example.shopmanagement.ui.product.details.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shopmanagement.R
import com.example.shopmanagement.data.models.api.product.details.Re
import com.example.shopmanagement.databinding.ProductDetailsLayoutBinding

class ProductDetailsViewHolder(private val binding: ProductDetailsLayoutBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(re: Re) {
        binding.product = re
        if(re.status == "sale"){
            binding.customerOrSupplierName.text = re.customer_name
            binding.idNo.text = "Sales Id : ${re.id}"
            binding.productSalesLayout.setBackgroundColor(Color.argb(84,32,63,255))
        }else{
            binding.customerOrSupplierName.text = re.supplier_name
            binding.idNo.text = "Purchase Id : ${re.id}"
            binding.productSalesLayout.setBackgroundColor(Color.argb(100,122,87,165))
        }

    }

    companion object {
        fun create(parent: ViewGroup): ProductDetailsViewHolder{
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = DataBindingUtil.inflate<ProductDetailsLayoutBinding>(layoutInflater, R.layout.product_details_layout,parent,false)
            return  ProductDetailsViewHolder(binding)
        }
    }
}
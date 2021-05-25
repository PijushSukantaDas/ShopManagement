package com.example.shopmanagement.ui.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shopmanagement.R
import com.example.shopmanagement.data.models.Product
import com.example.shopmanagement.databinding.ProductLayoutBinding

class ProductListAdapter(private val productList : List<Product>,private val iProductListener : IProductListener) : RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>(){
    
    class ProductViewHolder(private val binding: ProductLayoutBinding,private val listener: IProductListener) : RecyclerView.ViewHolder(binding.root){
        fun bind(product : Product){
            binding.product = product
            binding.root.setOnClickListener {
                listener.navigateToDetails(product)
            }

            binding.editProductBtn.setOnClickListener {
                listener.editProduct(product)
            }

            binding.deleteProductBtn.setOnClickListener {
                listener.deleteProduct(product)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductListAdapter.ProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ProductLayoutBinding>(layoutInflater, R.layout.product_layout,parent,false)
        return ProductViewHolder(binding,iProductListener)

    }

    override fun onBindViewHolder(holder: ProductListAdapter.ProductViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    override fun getItemCount(): Int = productList.size
}
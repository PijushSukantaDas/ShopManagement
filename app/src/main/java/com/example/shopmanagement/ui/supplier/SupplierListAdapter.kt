package com.example.shopmanagement.ui.supplier

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shopmanagement.R
import com.example.shopmanagement.data.models.Supplier
import com.example.shopmanagement.databinding.SupplierLayoutBinding

class SupplierListAdapter(var supplierList : List<Supplier>, private val listener : ISupplierListener) : RecyclerView.Adapter<SupplierListAdapter.SupplierListViewHolder>(){

    class SupplierListViewHolder(private val binding : SupplierLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(supplier: Supplier, listener: ISupplierListener) {
            binding.supplier = supplier
            binding.root.setOnClickListener {
                listener.navigateToSupplierDetails(supplier)
            }

            binding.editCustomer.setOnClickListener {
                listener.editSupplier(supplier)
            }

            binding.deleteCustomer.setOnClickListener {
                listener.deleteSupplier(supplier)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SupplierListAdapter.SupplierListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<SupplierLayoutBinding>(layoutInflater, R.layout.supplier_layout,parent,false)
        return SupplierListViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SupplierListAdapter.SupplierListViewHolder,
        position: Int
    ) {
        holder.bind(supplierList[position],listener)
    }

    override fun getItemCount(): Int = supplierList.size
}
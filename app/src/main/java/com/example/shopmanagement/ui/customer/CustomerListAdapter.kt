package com.example.shopmanagement.ui.customer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shopmanagement.R
import com.example.shopmanagement.data.models.Customer
import com.example.shopmanagement.databinding.CustomerLayoutBinding
import kotlinx.android.synthetic.main.customer_layout.view.*

class CustomerListAdapter(private val customerList : List<Customer>,private val iCustomerListener: ICustomerListener) : RecyclerView.Adapter<CustomerListAdapter.CustomerListViewHolder>() {

    class CustomerListViewHolder(private val binding: CustomerLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(customer: Customer, iCustomerListener: ICustomerListener){
            binding.customerName.setText(customer.customerName)
            binding.customerMobile.setText(customer.customerMobile)
            binding.customerAddress.setText(customer.customerAddress)


            binding.root.customerCardView.setOnClickListener {
                iCustomerListener.navigateToDetails(customer)
            }

            binding.editCustomer.setOnClickListener {
                iCustomerListener.editCustomer(customer)
            }

            binding.deleteCustomer.setOnClickListener {
                iCustomerListener.deleteCustomer(customer)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomerListAdapter.CustomerListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<CustomerLayoutBinding>(layoutInflater, R.layout.customer_layout,parent,false)
        return CustomerListViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: CustomerListAdapter.CustomerListViewHolder,
        position: Int
    ) {
        holder.bind(customerList[position],iCustomerListener)
    }

    override fun getItemCount(): Int = customerList.size
}

package com.example.shopmanagement.ui.customer.details

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shopmanagement.R
import com.example.shopmanagement.data.models.api.customer.transaction.Re
import com.example.shopmanagement.databinding.TransactionLayoutBinding

class CustomerTransactionAdapter(private val list: List<Re>) : RecyclerView.Adapter<CustomerTransactionAdapter.TransactionViewHolder>(){
    class TransactionViewHolder(private val binding: TransactionLayoutBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(re: Re, position: Int, list: List<Re>) {
            binding.transactionDate.text = re.date
            binding.totalAmount.text = re.amount.toString()
            binding.transaction = re
            binding.balance.text = re.balance.toString()
            when(re.status){
                "invoice"->{
                    binding.idNo.text = "Invoice No: ${re.receipt_id}"
                    binding.customerPaymentLayout.setBackgroundColor(Color.argb(100,32,63,255))
                }
                "receipt" -> {
                    binding.idNo.text = "Receipt No: ${re.receipt_id}"
                    binding.customerPaymentLayout.setBackgroundColor(Color.argb(84,104,253,11))
                }

            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomerTransactionAdapter.TransactionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<TransactionLayoutBinding>(layoutInflater, R.layout.transaction_layout,parent,false)
        return CustomerTransactionAdapter.TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: CustomerTransactionAdapter.TransactionViewHolder,
        position: Int
    ) {
        holder.bind(list[position],position,list)
    }

    override fun getItemCount() = list.size
}
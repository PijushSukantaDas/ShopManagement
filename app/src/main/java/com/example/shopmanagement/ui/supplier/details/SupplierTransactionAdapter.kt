package com.example.shopmanagement.ui.supplier.details

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

import com.example.shopmanagement.R
import com.example.shopmanagement.data.models.api.supplier.details.Re
import com.example.shopmanagement.databinding.TransactionLayoutBinding

class SupplierTransactionAdapter(private val list: List<Re>) : RecyclerView.Adapter<SupplierTransactionAdapter.TransactionViewHolder>() {
    class TransactionViewHolder(private val binding: TransactionLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(re: Re) {
            binding.transactionDate.text = re.date
            binding.idNo.text = "Id : ${re.id}"
            binding.totalAmount.text = re.amount.toString()
            binding.statusType.text = re.status
            binding.balance.text = re.balance.toString()

            if(re.status == "purchase"){
                binding.customerPaymentLayout.setBackgroundColor(Color.argb(100,75,158,255))
            }else{
                binding.customerPaymentLayout.setBackgroundColor(Color.argb(100,0,65,255))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<TransactionLayoutBinding>(layoutInflater, R.layout.transaction_layout,parent,false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

}
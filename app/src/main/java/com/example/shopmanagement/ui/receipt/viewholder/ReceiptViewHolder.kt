package com.example.shopmanagement.ui.receipt.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shopmanagement.R
import com.example.shopmanagement.data.models.api.receipt.DataList
import com.example.shopmanagement.databinding.ReceiptInfoLayoutBinding
import com.example.shopmanagement.ui.receipt.IReceiptListener


class ReceiptViewHolder(private var binding: ReceiptInfoLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(receipt : DataList, listener: IReceiptListener) {
        binding.customerName.text = receipt.customer_name
        binding.amount.text = receipt.amount.toString()
        binding.receiptId.text = "Receipt Id : ${receipt.id}"
        binding.date.text = receipt.receive_date

        binding.root.setOnClickListener {
            listener.onReceiptClick(receipt)
        }

        binding.deleteBtn.setOnClickListener {
            listener.deleteReceipt(receipt)
        }

        binding.editBtn.setOnClickListener {
            listener.toReceiptDetails(receipt)
        }
    }

    companion object {
        fun create(parent: ViewGroup, listener: IReceiptListener): ReceiptViewHolder{
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = DataBindingUtil.inflate<ReceiptInfoLayoutBinding>(layoutInflater, R.layout.receipt_info_layout,parent,false)
            return ReceiptViewHolder(binding)
        }
    }

}
package com.example.shopmanagement.ui.payment.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shopmanagement.R
import com.example.shopmanagement.data.models.api.payment.PaymentList
import com.example.shopmanagement.databinding.ReceiptInfoLayoutBinding
import com.example.shopmanagement.ui.payment.IPaymentListener

class PaymentViewHolder(private var binding: ReceiptInfoLayoutBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(payment: PaymentList, listener: IPaymentListener) {
            binding.customerName.text = payment.supplier_name
            binding.amount.text = payment.amount.toString()
            binding.receiptId.text = "Payment Id : ${payment.id}"
//            val formatDate = SimpleDateFormat("dd-MM-yyyy")
//            val date = formatDate.format(receipt.receive_date)
            binding.date.text = payment.payment_date

            binding.root.setOnClickListener {
                listener.onPaymentItemClick(payment)
            }

            binding.deleteBtn.setOnClickListener {
                listener.deletePayment(payment)
            }

            binding.editBtn.setOnClickListener {
                listener.toPaymentDetails(payment)
            }
        }


    companion object {
        fun create(parent: ViewGroup, listener: IPaymentListener): PaymentViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = DataBindingUtil.inflate<ReceiptInfoLayoutBinding>(layoutInflater, R.layout.receipt_info_layout,parent,false)
            return PaymentViewHolder(binding)
        }
    }

}
package com.example.shopmanagement.ui.payment

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.shopmanagement.data.models.api.payment.PaymentList
import com.example.shopmanagement.ui.payment.viewholder.PaymentViewHolder

class PaymentAdapter(private val  listener : IPaymentListener) : PagingDataAdapter<PaymentList,PaymentViewHolder>(REPO_COMPARATOR) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PaymentViewHolder{
        return PaymentViewHolder.create(parent,listener)
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        val payment = getItem(position)
        if(payment != null){
            holder.bind(payment,listener)
        }

    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<PaymentList>() {
            override fun areItemsTheSame(oldItem: PaymentList, newItem: PaymentList): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: PaymentList, newItem: PaymentList): Boolean =
                oldItem == newItem
        }
    }
}
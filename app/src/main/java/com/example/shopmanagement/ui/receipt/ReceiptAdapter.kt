package com.example.shopmanagement.ui.receipt

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.shopmanagement.data.models.api.receipt.DataList
import com.example.shopmanagement.ui.receipt.viewholder.ReceiptViewHolder

class ReceiptAdapter(private val listener : IReceiptListener) : PagingDataAdapter<DataList, ReceiptViewHolder>(
    REPO_COMPARATOR) {



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReceiptViewHolder {
        return ReceiptViewHolder.create(parent,listener)
    }

    override fun onBindViewHolder(holder: ReceiptViewHolder, position: Int) {
        val receipt = getItem(position)
        if(receipt != null){
            holder.bind(receipt,listener)
        }
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<DataList>() {
            override fun areItemsTheSame(oldItem: DataList, newItem: DataList): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: DataList, newItem: DataList): Boolean =
                oldItem == newItem
        }
    }
}
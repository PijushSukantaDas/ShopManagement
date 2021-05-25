package com.example.shopmanagement.ui.purchase

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.shopmanagement.data.models.api.purchase.PurchaseData
import com.example.shopmanagement.ui.purchase.list.adapter.PurchaseViewHolder

class PurchaseAdapter(
    private val listener: IPurchaseListener

) : PagingDataAdapter<PurchaseData,PurchaseViewHolder>(REPO_COMPARATOR) {


        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): PurchaseViewHolder {
            return PurchaseViewHolder.create(parent,listener)
        }

        override fun onBindViewHolder(holder: PurchaseViewHolder, position: Int) {
            val purchaseData = getItem(position)
            if(purchaseData != null){
                holder.bind(purchaseData,position)
            }

        }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<PurchaseData>() {
            override fun areItemsTheSame(oldItem: PurchaseData, newItem: PurchaseData): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: PurchaseData, newItem: PurchaseData): Boolean =
                oldItem == newItem
        }
    }

}
package com.example.shopmanagement.data.repositories.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.shopmanagement.data.models.api.purchase.PurchaseData
import com.example.shopmanagement.domain.PurchaseUseCase
import com.bumptech.glide.load.HttpException
import java.io.IOException
import javax.inject.Inject

class PurchaseSearchSource @Inject constructor(
    private val purchaseUseCase: PurchaseUseCase,
    private val mobile : String?,
    private val purchaseInvoice : String?,
    private val fromDate : String,
    private val toDate : String
) : PagingSource<Int,PurchaseData>() {

    private val initialPageIndex = 1

    override fun getRefreshKey(state: PagingState<Int, PurchaseData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PurchaseData> {
        val page = params.key ?: initialPageIndex
        return try {
            val response = purchaseUseCase.searchedWebPurchase(page,mobile?:"",fromDate,toDate,purchaseInvoice?:"")

            PagingSource.LoadResult.Page(
                response.body()?.data?.purchases?.data ?: listOf() , prevKey = if (page == initialPageIndex) null else page - 1,
                nextKey = if (response.body()?.data?.purchases?.data!!.isEmpty()) null else page + 1
            )
        } catch (exception: IOException) {
            return PagingSource.LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return PagingSource.LoadResult.Error(exception)
        }
    }
}
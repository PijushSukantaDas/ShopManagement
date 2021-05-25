package com.example.shopmanagement.data.repositories.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.shopmanagement.data.models.api.invoice.list.InvoiceData
import com.example.shopmanagement.domain.InvoiceUseCase
import com.bumptech.glide.load.HttpException
import java.io.IOException
import javax.inject.Inject

class InvoiceSearchSource @Inject constructor(private val invoiceUseCase: InvoiceUseCase,
                                              private val id : String,
                                              private val billNo : String,
                                              private val fromDate : String,
                                              private val toDate : String) : PagingSource<Int,InvoiceData>(){
    private val initialPageIndex = 1

    override fun getRefreshKey(state: PagingState<Int, InvoiceData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: PagingSource.LoadParams<Int>): PagingSource.LoadResult<Int, InvoiceData> {
        val page = params.key ?: initialPageIndex
        return try {
            val response = invoiceUseCase.searchInvoice(id,fromDate,toDate,billNo,page)

            PagingSource.LoadResult.Page(
                response.body()?.data?.data?: listOf() , prevKey = if (page == initialPageIndex) null else page - 1,
                nextKey = if (response.body()?.data?.data!!.isEmpty()) null else page + 1
            )
        } catch (exception: IOException) {
            return PagingSource.LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return PagingSource.LoadResult.Error(exception)
        }
    }
}
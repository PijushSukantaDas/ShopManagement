package com.example.shopmanagement.data.repositories.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.shopmanagement.data.models.api.invoice.list.InvoiceData
import com.example.shopmanagement.domain.InvoiceUseCase
import com.bumptech.glide.load.HttpException
import java.io.IOException
import javax.inject.Inject

class InvoicePagingSource @Inject constructor(private val invoiceUseCase: InvoiceUseCase) : PagingSource<Int,InvoiceData>(){
    private val initialPageIndex = 1

    override fun getRefreshKey(state: PagingState<Int, InvoiceData>): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, InvoiceData> {
        val page = params.key ?: initialPageIndex
        return try {

            val response = invoiceUseCase.getUserInvoiceList(page)

            LoadResult.Page(
                response.body()?.data?.data?: listOf() , prevKey = if (page == initialPageIndex) null else page - 1,
                nextKey = if (response.body()?.data?.data!!.isEmpty()) null else page + 1
            )

        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}
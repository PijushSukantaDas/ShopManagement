package com.example.shopmanagement.data.repositories.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.shopmanagement.data.models.api.payment.PaymentList
import com.example.shopmanagement.domain.api.ApiUseCase
import com.bumptech.glide.load.HttpException
import java.io.IOException
import javax.inject.Inject

class PaymentSource @Inject constructor(
    private val apiUseCase: ApiUseCase
) : PagingSource<Int,PaymentList>() {

    private val initialPageIndex = 1

    override fun getRefreshKey(state: PagingState<Int, PaymentList>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PaymentList> {
        val page = params.key ?: initialPageIndex
        return try {
            val response = apiUseCase.getUserPaymentList(page)

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
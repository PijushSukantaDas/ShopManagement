package com.example.shopmanagement.domain.api

import com.github.mikephil.charting.data.PieEntry
import com.example.shopmanagement.data.api.ApiHelper
import com.example.shopmanagement.data.models.api.dashboard.ApiDashBoard
import retrofit2.Response
import javax.inject.Inject

class ApiDashBoardUseCase @Inject constructor(
    private val apiHelper: ApiHelper
) {
    private var dashBoardData : ApiDashBoard? = null



    suspend fun getDashBoardData(): Response<ApiDashBoard> {
        apiHelper.getDashBoardData().body()?.let {
            dashBoardData = it
        }
        return apiHelper.getDashBoardData()
    }


    /**Returns Daily Chart Data**/
    fun pieChartDataDaily(): ArrayList<PieEntry> {
        val pieChartData : ArrayList<PieEntry> = arrayListOf()
        dashBoardData?.let {dashBoardData->
            pieChartData.add(PieEntry(dashBoardData.data.sale_today+0f,"Sale"))
            pieChartData.add(PieEntry(dashBoardData.data.deposit_today+0f,"Receipt"))
            pieChartData.add(PieEntry(dashBoardData.data.expense_today+0f,"Payment"))
            pieChartData.add(PieEntry(dashBoardData.data.purchase_today+0f,"Purchase"))
        }


        return pieChartData
    }

    /**Returns Monthly Chart Data**/
    fun pieChartDataMonthly(): ArrayList<PieEntry> {
        val pieChartData : ArrayList<PieEntry> = arrayListOf()

        dashBoardData?.let {dashBoardData->
            pieChartData.add(PieEntry(dashBoardData.data.sale_amount_this_month.toFloat(),"Sale"))
            pieChartData.add(PieEntry(dashBoardData.data.deposit_thisMonth.toFloat(),"Receipt"))
            pieChartData.add(PieEntry(dashBoardData.data.expense_thisMonth.toFloat(),"Payment"))
            pieChartData.add(PieEntry(dashBoardData.data.purchase_amount_this_month.toFloat(),"Purchase"))
        }


        return pieChartData
    }

    /**Returns Yearly Chart Data**/
    fun pieChartDataYearly(): ArrayList<PieEntry> {
        val pieChartData : ArrayList<PieEntry> = arrayListOf()
        dashBoardData?.let {dashBoardData->
            pieChartData.add(PieEntry(dashBoardData.data.sale_amount_this_year.toFloat(),"Sale"))
            pieChartData.add(PieEntry(dashBoardData.data.receipt_amount_this_year.toFloat(),"Receipt"))
            pieChartData.add(PieEntry(dashBoardData.data.expense_amount_this_year.toFloat(),"Payment"))
            pieChartData.add(PieEntry(dashBoardData.data.expense_amount_this_year.toFloat(),"Purchase"))
        }


        return pieChartData
    }


}
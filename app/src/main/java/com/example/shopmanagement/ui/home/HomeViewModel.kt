package com.example.shopmanagement.ui.home

import androidx.lifecycle.*
import com.github.mikephil.charting.data.PieEntry
import com.example.shopmanagement.domain.api.ApiDashBoardUseCase
import com.example.shopmanagement.domain.api.ApiUseCase
import com.example.shopmanagement.ui.utils.Event
import com.example.shopmanagement.ui.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiDashBoardUseCase: ApiDashBoardUseCase,
    private val apiUseCase: ApiUseCase
) : ViewModel() {

    var list : ArrayList<PieEntry> = arrayListOf()

    private val _navigate = MutableLiveData<Event<Boolean>>()
    val navigate: LiveData<Event<Boolean>>
        get() = _navigate

    fun getDashBoard() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = apiDashBoardUseCase.getDashBoardData()
                )
            )
        }catch (exception : Exception){
            emit(Resource.error(data = null,message = exception.toString()))
        }
    }

    fun pieChartDailyData() = apiDashBoardUseCase.pieChartDataDaily()

    fun pieChartMonthlyData() = apiDashBoardUseCase.pieChartDataMonthly()

    fun pieChartYearlyData() = apiDashBoardUseCase.pieChartDataYearly()

    fun getCustomer() = viewModelScope.launch {
        apiUseCase.getUserCustomer()
    }

    fun getSupplier() = viewModelScope.launch {

        apiUseCase.getUserSupplier()
    }

    fun getProduct() = viewModelScope.launch {
        apiUseCase.getUserProduct()
    }
}
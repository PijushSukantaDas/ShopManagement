package com.example.shopmanagement.ui.authentication

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.shopmanagement.data.models.api.authentication.License
import com.example.shopmanagement.data.models.api.category.ShopCategory
import com.example.shopmanagement.domain.api.ApiAuthenticationUseCase
import com.example.shopmanagement.ui.utils.Event
import com.example.shopmanagement.ui.utils.Preference
import com.example.shopmanagement.ui.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val apiAuthenticationUseCase: ApiAuthenticationUseCase,
    @ApplicationContext private val context : Context
) : ViewModel() {

    var shopName = MutableLiveData<String>()
    var mobileNumber = MutableLiveData<String>()
    var category = MutableLiveData<String>()
    var email = MutableLiveData<String>()
    var userName = MutableLiveData<String>()
    var categoryDetails = MutableLiveData<ShopCategory>()

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>>
        get() = _errorMessage

    fun getCategory() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = apiAuthenticationUseCase.getShopCategory()
                )
            )
        }catch (exception : Exception){
            emit(Resource.error(data = null,message = exception.toString()))
        }
    }

    fun authenticate(mobile: String?, userEmail: String?) = liveData(Dispatchers.IO) {
            emit(Resource.loading(data = null))
            try {
                emit(
                    Resource.success(
                        data = apiAuthenticationUseCase.authenticate(
                            shopName.value?:"",
                            mobile?:"",
                            category.value?:"",
                            userEmail?:"",
                            userName.value?:"",
                            expireDate(),
                            otp = false,
                            emailOtp = false,
                            status = 1
                        )
                    )
                )

            }catch (exception : Exception){
                emit(Resource.error(data = null,message = exception.toString()))
            }

    }

    fun createUser(
        license: License?,
        mobile: String?,
        email: String?
    ) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = apiAuthenticationUseCase.createUser(
                        license?.id.toString()?:"",
                        license?.mobile?:license?.email?:"",
                        license?.client_company_name?:"",
                        "",
                        categoryDetails.value?.id?.toString()?:"1"
                    )
                )
            )

        }catch (exception : Exception){
            emit(Resource.error(data = null,message = exception.toString()))
        }
    }

    fun fieldValidation(): Boolean {

        _errorMessage.value = when {

            shopName.value.isNullOrEmpty() -> {
                Event("Shop Name is Require")
            }
            category.value.isNullOrEmpty()->{
                Event("Category is Required")
            }
            userName.value.isNullOrEmpty()->{
                Event("Name is Required")
            }
            else -> {
                return true
            }
        }
        return false
    }

    private fun expireDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE,30)
        val sf = SimpleDateFormat("yyyy-MM-dd")

        Preference(context).addString("EXPIRE_DATE", sf.format(calendar.time))

        return sf.format(calendar.time)
    }

    fun checkExpire(expireDateString : String?): Boolean {

        return if(expireDateString != null){
            val sf = SimpleDateFormat("yyyy-MM-dd")
            val calendar = Calendar.getInstance()
            val date = sf.format(Date())
            calendar.time = sf.parse(date)
            val currentDate = calendar.time
            val expireDate = sf.parse(expireDateString)

            currentDate.before(expireDate)
        }else{
            false
        }


    }

    fun getCategoryName() = apiAuthenticationUseCase.categoryNameList()

    fun getCategoryDetails(categoryName : String?){
        categoryName?.let {
            categoryDetails.value = apiAuthenticationUseCase.getIdByName(categoryName)
            Preference(context).addInt("CATEGORY_ID",categoryDetails.value?.id?:0)
        }
    }

    fun dateDifference(expireDate : String?): Long? {
        if(expireDate != null){
            val expire = formatDate(expireDate)
            val today = currentDate()

            return TimeUnit.DAYS.convert((expire?.time?.minus(today?.time!!)!!), TimeUnit.MILLISECONDS)
        }
        return null
    }

    private fun formatDate(date : String): Date? {
        val sf = SimpleDateFormat("yyyy-MM-dd")
        return sf.parse(date)
    }

    private fun currentDate(): Date? {
        val sf = SimpleDateFormat("yyyy-MM-dd")
        val calendar = Calendar.getInstance()
        val date = sf.format(Date())
        return sf.parse(date)
    }
}
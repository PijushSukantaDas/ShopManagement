package com.example.shopmanagement.ui.authentication

import android.content.Context
import androidx.lifecycle.*
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
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class LicenseViewModel @Inject constructor(
    private val authenticationUseCase: ApiAuthenticationUseCase,
    @ApplicationContext private val context: Context,
    private val apiAuthenticationUseCase: ApiAuthenticationUseCase
) : ViewModel(){

    var mobileNumber = MutableLiveData<String>()
    var emailNumber = MutableLiveData<String>()
    var date : Int? = null

    private var _alarmOn = MutableLiveData<Boolean>()
    val isAlarmOn: LiveData<Boolean>
        get() = _alarmOn


    private val _elapsedTime = MutableLiveData<Long>()
    val elapsedTime: LiveData<Long>
        get() = _elapsedTime

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>>
        get() = _errorMessage



    fun bDAuthenticate(otp: Boolean, emailOtp: Boolean) = liveData(Dispatchers.IO) {
        if(mobileNumber.value.isNullOrEmpty() && emailNumber.value.isNullOrEmpty()){
            emit(
                Resource.error(
                    data = null,message = "Mobile Or Email is Empty"
                )
            )
            return@liveData
        }
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = authenticationUseCase.authenticate(
                        "",
                        mobileNumber.value,
                        "",
                        emailNumber.value,
                        "",
                        expireDate(),
                        otp,
                        emailOtp,
                        0
                    )
                )
            )

        }catch (exception : Exception){
            emit(Resource.error(data = null,message = exception.toString()))
        }

    }

    fun mobileValidation(): Boolean {

        _errorMessage.value = when {
            mobileNumber.value.isNullOrEmpty() && emailNumber.value.isNullOrEmpty() -> {
                Event("Mobile Number is Required")
            }
            !phoneNumberValidator(mobileNumber.value?:"") && emailNumber.value.isNullOrEmpty()->{
                Event("Enter a valid phone number")
            }
            else -> {
                return true
            }
        }
        return false
    }

    fun createUser(
        mobile: String?,
        licenseId: String?,
        email: String?,
        companyName: String?
    ) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val phone = if(mobile.isNullOrEmpty()){
                email
            }else{
                mobile
            }
            emit(
                Resource.success(
                    data = apiAuthenticationUseCase.createUser(
                        licenseId?:"",
                        phone?:"",
                        companyName?:"Postally2021",
                        "",
                        "1"
                    )
                )
            )

        }catch (exception : Exception){
            emit(Resource.error(data = null,message = exception.toString()))
        }
    }

    fun emailValidation(): Boolean {

        _errorMessage.value = when {

            emailNumber.value.isNullOrEmpty() && mobileNumber.value.isNullOrEmpty() -> {
                Event("Email Number is Required")
            }
            !isEmailValid(emailNumber.value?:"") && mobileNumber.value.isNullOrEmpty()->{
                Event("Email is Not Valid")
            }
            else -> {
                return true
            }
        }
        return false
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun expireDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE,30)
        val sf = SimpleDateFormat("yyyy-MM-dd")

        Preference(context).addString("EXPIRE_DATE", sf.format(calendar.time))

        return sf.format(calendar.time)
    }

    private fun phoneNumberValidator(number : String): Boolean {
        val format = Pattern.compile("^(?:\\+?88)?01[15-9]\\d{8}\$")
        return format.matcher(number).matches()
    }

    fun checkExpire(expireDateString : String?): Boolean {

        return if(expireDateString != null && expireDateString!=""){
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
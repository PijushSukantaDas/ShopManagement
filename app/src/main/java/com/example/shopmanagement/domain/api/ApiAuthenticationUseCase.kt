package com.example.shopmanagement.domain.api

import android.content.Context
import com.example.shopmanagement.data.api.ApiHelper
import com.example.shopmanagement.data.models.api.authentication.ApiAuth
import com.example.shopmanagement.data.models.api.category.ApiShopCategory
import com.example.shopmanagement.data.models.api.category.ShopCategory
import com.example.shopmanagement.ui.utils.ConstantKeys
import com.example.shopmanagement.ui.utils.Preference
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Response
import javax.inject.Inject
import kotlin.random.Random

class ApiAuthenticationUseCase @Inject constructor(
    private val apiHelper: ApiHelper,
    @ApplicationContext private val context : Context
){

    var categoryId = 0
    private var shopCategoryList : ArrayList<ShopCategory> = arrayListOf()
    private var categoryDropDown : ArrayList<String> = arrayListOf()

    suspend fun authenticate(
        shopName: String?,
        mobileNumber: String?,
        category: String?,
        email: String?,
        userName: String,
        expireDate: String,
        otp: Boolean,
        emailOtp: Boolean,
        status:Int
    ): Response<ApiAuth> = apiHelper.authenticate(

        mobile = mobileNumber,
        shopName = shopName?:"",
        address = "",
        email = email?:"",
        name = userName,
        expireDate = expireDate,
        message = otpMessage(),
        otp = otp,
        emailOtp = emailOtp,
        status = status
    )

    private fun otpMessage(): String {
        val otp = Random.nextInt(1000,10000)
        Preference(context).addString("OTP", otp.toString())
        return ConstantKeys.VALIDATION_MESSAGE + otp.toString()
    }

    suspend fun getShopCategory(): Response<ApiShopCategory> {
        shopCategoryList = ArrayList(apiHelper.getShopCategory().body()?.data?: listOf())
        return apiHelper.getShopCategory()
    }

    fun categoryNameList(): ArrayList<String> {
        shopCategoryList.map {
            categoryDropDown.plusAssign(it.category_name)
        }
        return categoryDropDown
    }

    fun getIdByName(categoryName : String): ShopCategory? {
        return shopCategoryList.find {
            it.category_name == categoryName
        }
    }

    suspend fun createUser(userId:String,mobile: String,companyName:String,address: String,categoryId:String) = apiHelper.createUser(
        userId,
        mobile,
        companyName,
        address,
        categoryId
    )
}
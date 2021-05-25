package com.example.shopmanagement.data.repositories

import com.example.shopmanagement.data.dao.IDailyAccountDao
import javax.inject.Inject

class DailyAccountRepository @Inject constructor(private val dailyAccountDao: IDailyAccountDao) {
}
package com.example.shopmanagement.data.repositories

import com.example.shopmanagement.data.dao.ICodesDao
import com.example.shopmanagement.data.models.Codes
import javax.inject.Inject

class CodesRepository @Inject constructor(private val codesDao: ICodesDao) {
    suspend fun insertCodes(codes : Codes) = codesDao.insertCodes(codes)
}
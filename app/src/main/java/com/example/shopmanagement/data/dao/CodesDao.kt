package com.example.shopmanagement.data.dao

import com.example.shopmanagement.data.models.Codes
import javax.inject.Inject

class CodesDao @Inject constructor() : ICodesDao{
    override suspend fun insertCodes(codes: Codes) {
        insertCodes(codes)
    }

}
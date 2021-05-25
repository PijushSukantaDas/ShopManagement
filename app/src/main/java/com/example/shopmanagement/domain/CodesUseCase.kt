package com.example.shopmanagement.domain

import com.example.shopmanagement.data.models.Codes
import com.example.shopmanagement.data.repositories.CodesRepository
import javax.inject.Inject

class CodesUseCase @Inject constructor(private val codesRepository: CodesRepository) {
    suspend fun insertCodes(codes : Codes) = codesRepository.insertCodes(codes)
}
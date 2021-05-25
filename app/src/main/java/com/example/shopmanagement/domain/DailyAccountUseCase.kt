package com.example.shopmanagement.domain

import com.example.shopmanagement.data.repositories.DailyAccountRepository
import javax.inject.Inject

class DailyAccountUseCase @Inject constructor(private val dailyAccountRepository: DailyAccountRepository) {
}
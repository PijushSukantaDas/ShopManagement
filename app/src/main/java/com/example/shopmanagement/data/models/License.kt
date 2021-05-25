package com.example.shopmanagement.data.models

data class License(
    val address: String,
    val branch_id: Int,
    val client_company_name: String,
    val company_id: Int,
    val created_at: String,
    val email: String,
    val expire_at: String,
    val id: Int,
    val license_key: String,
    val mobile: String,
    val name: String,
    val project_id: String,
    val status: Int,
    val times_activated_max: String,
    val updated_at: String,
    val url: String,
    val user_id: Int
)
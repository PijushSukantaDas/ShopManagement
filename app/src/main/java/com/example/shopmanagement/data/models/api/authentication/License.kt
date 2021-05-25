package com.example.shopmanagement.data.models.api.authentication

data class License(
    val id : Int,
    val branch_id: Int,
    val client_company_name: String,
    val company_id: Int,
    val email: String,
    val expire_at: String,
    val license_key: Any,
    val mobile: String,
    val name: String,
    val project_id: String,
    val status: Int,
    val times_activated_max: String,
    val url: String,
    val user_id: Int
)
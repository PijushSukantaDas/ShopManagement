package com.example.shopmanagement.ui.utils

fun phoneNumberValidator(number : String): String? {
    val format = "^(?:\\+?88)?01[13-9]\\d{8}\$"
    val result : String? = Regex(format).find(number)?.value
    return result


}

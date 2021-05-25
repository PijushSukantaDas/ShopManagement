package com.example.shopmanagement.ui.utils

import java.text.SimpleDateFormat
import java.util.*


object FormateDate {
    fun formatDateToString(date : Date): String {
        val format = "yyyy-MM-dd"
        val simpleDateFormat = SimpleDateFormat(format)

        return  simpleDateFormat.format(date)
    }

    fun formatStringToDate(date : String): Date {
        val format = "yyyy-MM-dd"
        val simpleDateFormat = SimpleDateFormat(format)
        val dateString = simpleDateFormat.format(date)
        val date = simpleDateFormat.parse(date)

        return  date
    }
}
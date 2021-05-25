package com.example.shopmanagement

import androidx.fragment.app.Fragment
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

open class BaseFragment : Fragment() {

    fun removeTimeFromDate(date : String): String {
        val input = date
        val inputFormatter: DateFormat =
            SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS")
        val date: Date = inputFormatter.parse(input)

        val outputFormatter: DateFormat = SimpleDateFormat("MM/dd/yyyy")
        val output: String = outputFormatter.format(date)

        return output
    }

    fun todayDate() = "${Calendar.getInstance().get(Calendar.YEAR)}-${SimpleDateFormat("MM").format(Calendar.getInstance().time)}-01"

    fun lastDate() = "${Calendar.getInstance().get(Calendar.YEAR)}-${SimpleDateFormat("MM").format(Calendar.getInstance().time)}-${Calendar.getInstance().getActualMaximum(Calendar.DATE)}"
}
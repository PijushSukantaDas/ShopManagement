package com.example.shopmanagement.ui.utils

import android.widget.Toast
import androidx.fragment.app.Fragment
import java.util.*

fun Fragment.showToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
}

fun Fragment.showDialog(title: String,body:String) {
    activity?.showDialog(title, body)
}

fun Fragment.dateFormat(date : Date): String? {
    return activity?.dateFormat(date)
}



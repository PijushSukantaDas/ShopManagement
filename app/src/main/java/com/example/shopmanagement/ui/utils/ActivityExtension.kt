package com.example.shopmanagement.ui.utils

import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import java.text.SimpleDateFormat
import java.util.*

fun FragmentActivity.showDialog(title: String, body:String) {
    val alertDialog = AlertDialog.Builder(this)
    alertDialog.setTitle(title)
    alertDialog.setMessage(body)

    alertDialog.setNegativeButton("ok",{ dialogInterface: DialogInterface, i: Int -> })
    alertDialog.show()

}

fun FragmentActivity.dateFormat(date : Date): String {
    val format = "dd-MM-yyyy"
    val simpleDateFormat = SimpleDateFormat(format)
    return simpleDateFormat.format(date)
}

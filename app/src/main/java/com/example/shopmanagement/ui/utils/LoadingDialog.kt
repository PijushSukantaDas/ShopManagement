package com.example.shopmanagement.ui.utils

import android.app.AlertDialog
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LoadingDialog @Inject constructor(@ApplicationContext private val context: Context) {

    val dialog: AlertDialog = dmax.dialog.SpotsDialog.Builder().setContext(context).build()

    fun showDialog() = dialog.show()

    fun dismissDialog() = dialog.dismiss()
}
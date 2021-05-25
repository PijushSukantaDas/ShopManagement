package com.example.shopmanagement.ui

import android.net.Uri
import android.os.Bundle
import com.example.shopmanagement.BaseFragment

abstract class BaseCreateFragment : BaseFragment() {
    lateinit var pickerObserver : PickerObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pickerObserver = PickerObserver(requireActivity().activityResultRegistry, getPickerCallback())
        lifecycle.addObserver(pickerObserver)
    }

    abstract fun getPickerCallback(): (Uri) -> Unit

    protected fun showImagePicker() {
        pickerObserver.selectImage()
    }
}
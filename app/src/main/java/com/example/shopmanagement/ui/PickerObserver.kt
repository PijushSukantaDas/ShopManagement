package com.example.shopmanagement.ui

import android.net.Uri
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class PickerObserver(private val registry : ActivityResultRegistry, private val callback: ((Uri) -> Unit)) : DefaultLifecycleObserver {
    lateinit var getContent : ActivityResultLauncher<String>

    override fun onCreate(owner: LifecycleOwner) {
        getContent = registry.register("key", owner, ActivityResultContracts.GetContent(), ActivityResultCallback<Uri> {
            callback(it)
        })
    }

    fun selectImage() {
        getContent.launch("image/*")
    }
}
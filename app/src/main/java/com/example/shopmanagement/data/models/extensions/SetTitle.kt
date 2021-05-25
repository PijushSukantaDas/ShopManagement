package com.example.shopmanagement.data.models.extensions

object SetTitle {

    private var title = ""

    fun setTitle(title: String){
        this.title = title
    }

    fun getTitle() : String = title
}
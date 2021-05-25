package com.example.shopmanagement.ui.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.shopmanagement.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreeen : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        return inflater.inflate(R.layout.fragment_splash_screeen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val background = object : Thread(){
            override fun run() {
                try{
                    sleep(0)

                }catch (e : Exception){
                    e.printStackTrace()
                }
            }

        }

        background.start()
    }

}


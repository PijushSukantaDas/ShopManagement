package com.example.shopmanagement.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.shopmanagement.R
import com.example.shopmanagement.databinding.ActivitySupplierBinding
import com.example.shopmanagement.ui.supplier.SupplierViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SupplierActivity : AppCompatActivity() {
    private val viewModel : SupplierViewModel by viewModels()
    lateinit var binding : ActivitySupplierBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = DataBindingUtil.setContentView<ActivitySupplierBinding>(this,R.layout.activity_supplier)
        setSupportActionBar(binding.myToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_button)
        binding.viewModel = viewModel

        bannerAdInit()
    }

    private fun bannerAdInit() {
        MobileAds.initialize(this) {}

        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
package com.example.shopmanagement.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.shopmanagement.R
import com.example.shopmanagement.databinding.FragmentDashBoardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashBoardFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding : FragmentDashBoardBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_dash_board,container,false)

        val toolbar = activity?.findViewById<TextView>(R.id.titleToolbar)
        toolbar?.setText("Dashboard")

        binding.goToProductBtn.setOnClickListener {

        }
        binding.goToCustomerBtn.setOnClickListener {

        }

        binding.supplier.setOnClickListener {

        }

        binding.goToPurchase.setOnClickListener {

        }

        binding.gotoSales.setOnClickListener {

        }

        return binding.root
    }

}
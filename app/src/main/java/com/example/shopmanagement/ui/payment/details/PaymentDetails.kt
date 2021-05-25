package com.example.shopmanagement.ui.payment.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.shopmanagement.BaseFragment
import com.example.shopmanagement.R
import com.example.shopmanagement.databinding.FragmentPaymentDetailsBinding
import com.example.shopmanagement.ui.payment.PaymentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_payment_show.*

@AndroidEntryPoint
class PaymentDetails : BaseFragment() {
    lateinit var binding : FragmentPaymentDetailsBinding
    private val viewModel : PaymentViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPaymentDetailsBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val toolbar = activity?.findViewById<TextView>(R.id.titleToolbar)
        toolbar?.setText("Payment Details")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.paymentCheckLayout.visibility = View.GONE

        binding.editBtn.setOnClickListener {
            viewModel.editPayment()
            viewModel.fromEditBtn = true
            findNavController().navigate(R.id.supplierPaymentFragment)
        }
    }

}
package com.example.shopmanagement.ui.receipt.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.shopmanagement.BaseFragment
import com.example.shopmanagement.R
import com.example.shopmanagement.databinding.FragmentReceiptDetailsBinding
import com.example.shopmanagement.ui.receipt.ReceiptViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_receipt_show.*

@AndroidEntryPoint
class ReceiptDetails : BaseFragment() {
    lateinit var binding : FragmentReceiptDetailsBinding
    private val viewModel : ReceiptViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReceiptDetailsBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.checkLayout.visibility = View.GONE

        binding.editBtn.setOnClickListener {
            viewModel.editReceipt()
            findNavController().navigate(R.id.action_receiptDetails_to_customerReceiptFragment)
        }
    }

}
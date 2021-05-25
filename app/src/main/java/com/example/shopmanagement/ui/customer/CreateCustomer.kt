package com.example.shopmanagement.ui.customer

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.shopmanagement.R
import com.example.shopmanagement.databinding.FragmentCreateCustomerBinding
import com.example.shopmanagement.ui.activity.CustomerActivity
import com.example.shopmanagement.ui.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_product_list.*

@AndroidEntryPoint
class CreateCustomer : Fragment() {

    lateinit var binding : FragmentCreateCustomerBinding
    val viewModel : CustomerViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.customerList)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateCustomerBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        if(viewModel.editCustomerFlag){
            binding.createCustomerBtn.text = "update customer"
            val toolbar = activity?.findViewById<TextView>(R.id.titleToolbar)
            toolbar?.setText("Update Customer")
        }else{
            val toolbar = activity?.findViewById<TextView>(R.id.titleToolbar)
            toolbar?.setText("Create Customer")
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val adapterGender = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.select_dialog_item,
            viewModel.getGender()
        )
        binding.customerGenderAutoFill.setAdapter(adapterGender)

        val adapterReceipt = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.select_dialog_item,
            viewModel.getReceiptType()
        )


        binding.customerGenderAutoFill.setOnItemClickListener { parent, view, position, id ->
            viewModel.onGenderItemClicked(position)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner, EventObserver {
            showToast(it)
        })

//        viewModel.navigate.observe(viewLifecycleOwner,EventObserver{
//            if(it){
//                findNavController().navigateUp()
//            }else{
//                showToast("False")
//            }
//        })

        viewModel.isCustomerEmpty.observe(viewLifecycleOwner, Observer {
            if(it){
                noDataTv.visibility = View.GONE
            }else{
                noDataTv.visibility = View.VISIBLE
            }
        })

        binding.createCustomerBtn.setOnClickListener {

                viewModel.createUser().observe(viewLifecycleOwner, Observer {
                    it?.let { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                LoadingDialog(requireContext()).dismissDialog()
                                when (it.data?.body()?.success) {

                                    200 -> {
                                            viewModel.createLocalCustomer(it.data.body()?.data!!)
                                            val intent = Intent(
                                                requireContext(),
                                                CustomerActivity::class.java
                                            )

                                            requireActivity().finishAffinity()
                                            startActivity(intent)

                                    }
                                    201 -> {
                                        showToast("Phone Number Already Used")
                                    }
                                    else -> {
                                        showToast("${it.data?.body()}")
                                    }
                                }
                            }
                            Status.ERROR -> {
                                LoadingDialog(requireContext()).dismissDialog()
                                showToast("Error")
                            }
                            Status.LOADING -> {
                                LoadingDialog(requireContext()).showDialog()
                            }
                        }
                    }
                })


        }
    }


}
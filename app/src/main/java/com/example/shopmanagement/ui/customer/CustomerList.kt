package com.example.shopmanagement.ui.customer

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.example.shopmanagement.R
import com.example.shopmanagement.data.models.Customer
import com.example.shopmanagement.databinding.FragmentCustomerListBinding
import com.example.shopmanagement.ui.activity.CustomerActivity
import com.example.shopmanagement.ui.activity.MainActivity
import com.example.shopmanagement.ui.utils.Status
import com.example.shopmanagement.ui.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import dmax.dialog.SpotsDialog

@AndroidEntryPoint
class CustomerList : Fragment(),ICustomerListener {
    lateinit var binding : FragmentCustomerListBinding
    lateinit var adapter : CustomerListAdapter
    val viewModel : CustomerListViewModel by viewModels()
    private val customerViewModel : CustomerViewModel by activityViewModels()
    lateinit var  loader : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(requireContext(),MainActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        loader = SpotsDialog.Builder().setContext(requireContext()).build()

        binding = FragmentCustomerListBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewMoel = viewModel

        val toolbar = activity?.findViewById<TextView>(R.id.titleToolbar)
        toolbar?.setText("Customer")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val customers = viewModel.customerList
        binding.customerList.layoutManager = LinearLayoutManager(requireContext())
        adapter = CustomerListAdapter(listOf(),this)
        binding.customerList.adapter = adapter

        customers.observe(viewLifecycleOwner, Observer {
            adapter = CustomerListAdapter(it,this)
            binding.customerList.adapter = adapter
            adapter.notifyDataSetChanged()

        })

        binding.createCustomerBtn.setOnClickListener {
            customerViewModel.editCustomerFlag = false
            customerViewModel.clearFieldData()
            findNavController().navigate(R.id.action_customerList_to_createCustomer)
        }

        viewModel.searchCustomerList.observe(viewLifecycleOwner, Observer {
            adapter = CustomerListAdapter(it,this)
            binding.customerList.adapter = adapter
            adapter.notifyDataSetChanged()
        })

        binding.searchBtn.setOnClickListener {
            if(viewModel.searchValidation()){
                viewModel.searchCustomer().observe(viewLifecycleOwner, Observer {
                    it?.let { resource ->
                        when(resource.status){
                            Status.SUCCESS->{
                                loader.dismiss()
                                val responseData = it.data?.body()?.data
                                responseData?.let {
                                    if(it.data.isEmpty()){
                                        adapter = CustomerListAdapter(listOf(),this)
                                        binding.customerList.adapter = adapter
                                        adapter.notifyDataSetChanged()
                                        showToast("Customer Not Found")
                                    }else{
                                        viewModel.searchFromLocal(responseData.data[0].mobile)
                                    }

                                }

                            }
                            Status.LOADING->{
                                loader.show()
                            }
                            Status.ERROR->{
                                loader.dismiss()
                                showToast("Error")
                            }
                        }
                    }
                })
            }
        }

        binding.dateFromTv.addTextChangedListener {
            if(it.isNullOrEmpty()){
                val intent = Intent(
                    requireContext(),
                    CustomerActivity::class.java
                )
                startActivity(intent)
                requireActivity().finish()
            }
        }

        binding.mobileFormTv.addTextChangedListener {
            if(it.isNullOrEmpty()){
                val intent = Intent(
                    requireContext(),
                    CustomerActivity::class.java
                )
                startActivity(intent)
                requireActivity().finish()
            }
        }


    }

    override fun navigateToDetails(customer: Customer) {
        customerViewModel.customerId = customer.id
        val direction = CustomerListDirections.actionCustomerListToCustomerDetails(customer)
        findNavController().navigate(direction)
    }

    override fun editCustomer(customer: Customer) {
        customerViewModel.editCustomerFlag = true
        customerViewModel.setEditCustomerField(customer)
        findNavController().navigate(R.id.action_customerList_to_createCustomer)
    }

    override fun deleteCustomer(customer: Customer) {
        val dialog = MaterialDialog(requireContext())
            .title(R.string.customer_delete_title)
            .message(R.string.customer_delete_message)

        dialog.show()

        dialog.negativeButton {
            dialog.dismiss()
        }

        dialog.positiveButton {
            viewModel.deleteCustomer(customer).observe(viewLifecycleOwner, Observer {
                it?.let {resource ->
                    when(resource.status){
                        Status.SUCCESS->{
                            loader.dismiss()
                            viewModel.deleteLocalCustomer(customer)
                            showToast("Customer Deleted")
                        }
                        Status.LOADING->{
                            loader.show()
                        }
                        Status.ERROR->{
                            loader.dismiss()
                            showToast("${it.message}")
                        }
                    }
                }
            })
            adapter.notifyDataSetChanged()
            dialog.dismiss()
        }

    }


}
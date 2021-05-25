package com.example.shopmanagement.ui.receipt

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.shopmanagement.BaseFragment
import com.example.shopmanagement.databinding.FragmentCustomerReceiptBinding
import com.example.shopmanagement.ui.activity.ReceiptActivity
import com.example.shopmanagement.ui.utils.LoadingDialog
import com.example.shopmanagement.ui.utils.Status
import com.example.shopmanagement.ui.utils.showToast
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class CustomerReceiptFragment : BaseFragment() {

    lateinit var binding : FragmentCustomerReceiptBinding
    private val viewModel : ReceiptViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCustomerReceiptBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val builder : MaterialDatePicker.Builder<*> = MaterialDatePicker.Builder.datePicker()
        val materialDatePick = builder.build()

        binding.datePicker.setOnClickListener{
            materialDatePick.show(
                requireActivity().supportFragmentManager,
                "Date Picker"
            )
        }

        materialDatePick.addOnPositiveButtonClickListener { selection ->
            val targetFormat = SimpleDateFormat("yyyy-MM-dd")
            val newDate: String = targetFormat.format(selection)
            binding.dateTv.text = newDate
        }

        var adapterCustomer = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.select_dialog_item,
            listOf()
        )

        if(viewModel.fromEdit){
            binding.searchCustomer.visibility = View.GONE
            binding.customer.visibility = View.GONE

            viewModel.customerDetails.observe(viewLifecycleOwner, Observer { customer->
                if(customer != null){
                    binding.customerTv.text = customer.customerName
                    binding.customerMobileTv.text = customer.customerMobile
                    binding.customerBalanceTv.text = customer.customerOpeningBalance.toString()
                    binding.dueAmountTv.text = customer.customerOpeningBalance.toString()
                }

            })
        }else{
            viewModel.getCustomerName().observe(this.viewLifecycleOwner, Observer {
                adapterCustomer = ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.select_dialog_item,
                    it
                )
                binding.receiptCustomerSelection.setAdapter(adapterCustomer)
            })


            binding.receiptCustomerSelection.setOnItemClickListener { parent, view, position, id ->
                val data = adapterCustomer.getItem(position)
                viewModel.getCustomer("$data")

                viewModel.customerDetails.observe(viewLifecycleOwner, Observer { customer->
                    if(customer != null){
                        binding.customerTv.text = customer.customerName
                        binding.customerMobileTv.text = customer.customerMobile
                        binding.customerBalanceTv.text = customer.customerOpeningBalance.toString()
                        binding.dueAmountTv.text = customer.customerOpeningBalance.toString()
                    }

                })
            }
        }




        binding.discountEdt.addTextChangedListener {
                if(it.isNullOrEmpty() && binding.amountEdt.text.toString().isNullOrEmpty()){
                    viewModel.calculateDue(0.0 , 0.0)
                }else if(it.isNullOrEmpty() && binding.amountEdt.text.toString().isNotEmpty()){
                    viewModel.calculateDue(binding.amountEdt.text.toString().toDouble() , 0.0)
                }else if(it.toString().isNotEmpty() && binding.amountEdt.text.toString().isNullOrEmpty()){
                    viewModel.calculateDue(0.0 , it.toString().toDouble())
                }else{
                    viewModel.calculateDue(binding.amountEdt.text.toString().toDouble() , it.toString().toDouble())
                }

        }

        binding.amountEdt.addTextChangedListener {

                if(it.isNullOrEmpty() && binding.discountEdt.text.toString().isEmpty()){
                    viewModel.calculateDue(0.0 , 0.0)
                }else if(it.isNullOrEmpty() && binding.discountEdt.text.toString().isNotEmpty()){
                    viewModel.calculateDue( 0.0,binding.discountEdt.text.toString().toDouble())
                }else if(it.toString().isNotEmpty() && binding.discountEdt.text.toString().isEmpty()){
                    viewModel.calculateDue(it.toString().toDouble(),0.0)
                }else{
                    viewModel.calculateDue(it.toString().toDouble(),binding.discountEdt.text.toString().toDouble())
                }

        }

        viewModel.afterDiscountAmount.observe(viewLifecycleOwner, Observer {
            binding.totalAfterDiscountTv.text = it.toString()
        })


        binding.receiptBtn.setOnClickListener {
            when {
                binding.dateTv.text.toString().isNullOrEmpty() -> {
                    showToast("Insert Date")
                }
                binding.amountEdt.text.toString().isNullOrEmpty() -> {
                    showToast("Insert Payment Amount")
                }
                else -> {
                    viewModel.insertOrUpdateApiReceipt().observe(viewLifecycleOwner, Observer {
                        it?.let{resource->
                            when(resource.status){

                                Status.LOADING->{
                                    LoadingDialog(requireContext()).showDialog()
                                }
                                Status.SUCCESS->{
                                    LoadingDialog(requireContext()).dismissDialog()
                                    when(it.data?.data?.body()?.success){
                                        200->{
                                            viewModel.insertOrUpdateReceipt()
                                            val intent = Intent(this.requireContext(), ReceiptActivity::class.java)
                                            startActivity(intent)
                                            requireActivity().finish()
                                        }

                                    }
                                }
                                Status.ERROR->{
                                    LoadingDialog(requireContext()).dismissDialog()
                                    showToast("Error")
                                }

                            }
                        }
                    })
                }
            }

        }


    }

}
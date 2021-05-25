package com.example.shopmanagement.ui.sales.payment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.shopmanagement.BaseFragment
import com.example.shopmanagement.databinding.FragmentCustomerBillBinding
import com.example.shopmanagement.ui.activity.SalesActivity
import com.example.shopmanagement.ui.sales.SelectCustomerViewModel
import com.example.shopmanagement.ui.utils.Status
import com.example.shopmanagement.ui.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.*

@AndroidEntryPoint
class CustomerBillFragment : BaseFragment() {
    lateinit var binding : FragmentCustomerBillBinding
    private val viewModel : SelectCustomerViewModel by activityViewModels()
    private val salesPaymentViewModel : SalesPaymentViewModel by viewModels()
    lateinit var  loader : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCustomerBillBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loader = SpotsDialog.Builder().setContext(requireContext()).build()

        viewModel.payment.value = "0"
        viewModel.discount.value = "0"
        viewModel.currentDue.value = "0"
        viewModel.customerName.value = ""
        viewModel.billFromEdit()
        viewModel.calculateDue( 0.0, 0.0)

        if(viewModel.getEditFlag()){
            viewModel.setApiCustomer()

            viewModel.getCustomerDetails().observe(this.viewLifecycleOwner, Observer {customer ->
                if(customer !=null){
                    viewModel.setSelectedCustomer(customer)
                }

            })
        }


        binding.paymentTv.addTextChangedListener {
            if(binding.paymentTv.text.toString().isEmpty()){
                viewModel.calculateDue(0.0, binding.discountTv.text.toString().toDouble())

            }else{
                viewModel.calculateDue(binding.paymentTv.text.toString().toDouble(), binding.discountTv.text.toString().toDouble())
            }

        }

        binding.discountTv.addTextChangedListener {
            if(binding.discountTv.text.toString().isEmpty()){
                viewModel.calculateDue(binding.paymentTv.text.toString().toDouble(),0.0)

            }else{
                viewModel.discount.value = binding.discountTv.text.toString()
                viewModel.calculateDue(binding.paymentTv.text.toString().toDouble(),binding.discountTv.text.toString().toDouble())
            }
        }

        viewModel.currentDue.observe(this.viewLifecycleOwner, Observer {
            binding.currentDueTv.setText(it)

        })

        viewModel.selectedCustomer.observe(viewLifecycleOwner, Observer {
            if(it != null){
                binding.customerNameTv.text = it.customerName
                binding.customerMobileTv.text = it.customerMobile
            }
        })




        viewModel.customerInvoiceListTotalDue.observe(this.viewLifecycleOwner, Observer {
            binding.oldDue.setText(it.toString())
        })

        binding.confirmBtn.setOnClickListener {
            viewModel.arrayOfProduct()
            viewModel.confirmClick = true
            viewModel.currentDue.value = binding.currentDueTv.text.toString()
            viewModel.payment.value = binding.paymentTv.text.toString()

            if(viewModel.payment.value?.toDouble()?:0.0 > 0.0 ){
                viewModel.insertInvoice().observe(viewLifecycleOwner, Observer {
                    it?.let {resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                loader.dismiss()
                                when(it.data?.body()?.success){
                                    200->{
//                                        viewModel.insertLocal(it.data.body()?.data)

                                        /**Inserting Receipt Information into Postally Web Server**/
                                        viewModel.insertReceipt()

                                        val intent = Intent(requireContext(), SalesActivity::class.java)
                                        startActivity(intent)
                                        requireActivity().finish()
                                    }
                                    else->{
                                        showToast("Error Inserting Invoice")
                                    }
                                }
                            }
                            Status.ERROR -> {
                                loader.dismiss()
                                showToast("Error")
                            }
                            Status.LOADING -> {
                                loader.show()
                            }
                        }

                    }
                })


            }else{
                showToast("Enter Payment Amount")
            }

        }
    }





}
package com.example.shopmanagement.ui.customer.details

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopmanagement.BaseFragment
import com.example.shopmanagement.R
import com.example.shopmanagement.databinding.FragmentCustomerDetailsBinding
import com.example.shopmanagement.ui.customer.CustomerViewModel
import com.example.shopmanagement.ui.utils.Status
import com.example.shopmanagement.ui.utils.showToast
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import dmax.dialog.SpotsDialog
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class CustomerDetails : BaseFragment(){
    lateinit var binding : FragmentCustomerDetailsBinding
    private val args : CustomerDetailsArgs by navArgs()
    lateinit var adapter : CustomerTransactionAdapter
    private val viewModel : CustomerDetailsViewModel by viewModels()
    private val customerViewModel : CustomerViewModel by activityViewModels()
    lateinit var  loader : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCustomerDetailsBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.customer = args.cutomer
        binding.viewModel = viewModel

        val toolbar = activity?.findViewById<TextView>(R.id.titleToolbar)
        toolbar?.setText("${args.cutomer?.customerName} Details")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        datePickerSetup()
        loader = SpotsDialog.Builder().setContext(requireContext()).build()


        binding.customerDetailsRv.layoutManager = LinearLayoutManager(requireContext())
        adapter = CustomerTransactionAdapter(listOf())
        binding.customerDetailsRv.adapter = adapter

        val fistDay = todayDate()
        binding.dateFromTv.text = fistDay
        val lastDay = lastDate()
        binding.dateToTv.text = lastDay

        viewModel.getCustomerDetails(args.cutomer?.id?:0).observe(viewLifecycleOwner , Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        loader.dismiss()

                        val balanceList = viewModel.getBalanceList()
                        val responseData = it.data?.body()?.data

                        adapter = CustomerTransactionAdapter(balanceList)
                        binding.customerDetailsRv.adapter = adapter
                        adapter.notifyDataSetChanged()

                        val total = viewModel.apiCustomerBalance().toString()
                        binding.balance.text = total
                        binding.subTotal.text = total
                        binding.totalPayment.text = "Pay. : ${viewModel.getApiCustomerTotalPayment()}"
                        binding.invoiceAmount.text = "Inv. : ${viewModel.apiCustomerTotalInvoice().toString()}"
                        binding.totalRtv.text = "Rtv. : ${it.data?.body()?.data?.lastYearsReturn}"
                        binding.dateInvoice.text = binding.dateToTv.text.toString()
                        binding.datePayment.text = binding.dateToTv.text.toString()
                        binding.dateRtv.text = binding.dateToTv.text.toString()

                        responseData?.let {
                            binding.customerInvoiceTv.text = responseData.lastYearsTotalInvoice.toString()
                            binding.customerPaymentTv.text = responseData.lastYearsDebitPayment.toString()
                            binding.customerRtv.text = responseData.lastYearsReturn.toString()

                        }


                    }
                    Status.ERROR -> {
                        loader.dismiss()
                        showToast(it.message.toString())
                    }
                    Status.LOADING -> {
                        loader.show()
                    }
                }
            }

        })

        binding.searchBtn.setOnClickListener {
            viewModel.getDateSearchReceiptList(args.cutomer?.id?:0,binding.dateFromTv.text.toString(),binding.dateToTv.text.toString()).observe(
                viewLifecycleOwner, Observer {
                    it?.let {resource->
                        when(resource.status){
                            Status.SUCCESS->{
                                val body = it.data?.body()
                                body?.let {body->
                                    when(body.success){
                                        200->{
                                            loader.dismiss()

                                            viewModel.setBalanceList(body.data.res)

                                            val balanceList = viewModel.getBalanceList()

                                            adapter = CustomerTransactionAdapter(balanceList)
                                            binding.customerDetailsRv.adapter = adapter
                                            adapter.notifyDataSetChanged()



                                        }
                                        else->{
                                            loader.dismiss()
                                        }
                                    }

                                }
                            }
                            Status.LOADING->{
                                loader.show()
                            }
                            Status.ERROR->{
                                loader.dismiss()
                            }
                        }

                    }
                }
            )
        }

    }

    private fun datePickerSetup() {
        val fromBuilder : MaterialDatePicker.Builder<*> = MaterialDatePicker.Builder.datePicker()
        val toBuilder : MaterialDatePicker.Builder<*> = MaterialDatePicker.Builder.datePicker()

        val fromDatePick = fromBuilder.build()
        val toDatePick = toBuilder.build()

        binding.fromDateIcon.setOnClickListener{
            fromDatePick.show(
                requireActivity().supportFragmentManager,
                "From Date Picker"
            )
        }

        binding.toDatePicker.setOnClickListener{
            toDatePick.show(
                requireActivity().supportFragmentManager,
                "To Date Picker"
            )
        }

        fromDatePick.addOnPositiveButtonClickListener { selection ->
            val targetFormat = SimpleDateFormat("yyyy-MM-dd")
            val newDate: String = targetFormat.format(selection)
            binding.dateFromTv.text = newDate
        }

        toDatePick.addOnPositiveButtonClickListener { selection ->
            val targetFormat = SimpleDateFormat("yyyy-MM-dd")
            val newDate: String = targetFormat.format(selection)
            binding.dateToTv.text = newDate
            binding.dateInvoice.text = binding.dateToTv.text.toString()
            binding.datePayment.text = binding.dateToTv.text.toString()
            binding.dateRtv.text = binding.dateToTv.text.toString()
        }
    }

}
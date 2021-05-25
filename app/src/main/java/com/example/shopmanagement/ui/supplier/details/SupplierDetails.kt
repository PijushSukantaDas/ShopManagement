package com.example.shopmanagement.ui.supplier.details

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopmanagement.BaseFragment
import com.example.shopmanagement.R
import com.example.shopmanagement.databinding.FragmentSupplierDetailsBinding
import com.example.shopmanagement.ui.utils.Status
import com.example.shopmanagement.ui.utils.showToast
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import dmax.dialog.SpotsDialog
import java.text.SimpleDateFormat

@AndroidEntryPoint
class SupplierDetails : BaseFragment() {

    lateinit var binding : FragmentSupplierDetailsBinding
    private val args : SupplierDetailsArgs by navArgs()
    private val viewModel : DetailsViewModel by viewModels()
    lateinit var adapter : SupplierTransactionAdapter
    lateinit var  loader : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loader = SpotsDialog.Builder().setContext(requireContext()).build()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSupplierDetailsBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.supplier = args.supplier

        val toolbar = activity?.findViewById<TextView>(R.id.titleToolbar)
        toolbar?.setText("${args.supplier?.name} Details")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.supplierDetailsRv.layoutManager = LinearLayoutManager(requireContext())
        adapter = SupplierTransactionAdapter(listOf())
        binding.supplierDetailsRv.adapter  = adapter

        datePickerSetup()

        binding.dateFromTv.text = todayDate()
        binding.dateToTv.text = lastDate()

        binding.datePurchase.text = lastDate()
        binding.datePayment.text = lastDate()
        binding.datePurchaseRtv.text = lastDate()

        viewModel.getSupplierDetails(args.supplier?.id?:0).observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        when (it.data?.body()?.success) {
                            200 -> {
                                loader.dismiss()
                                val balance = viewModel.getSupplierBalanceList()
                                adapter = SupplierTransactionAdapter(balance)
                                binding.supplierDetailsRv.adapter = adapter
                                adapter.notifyDataSetChanged()

                                val total = viewModel.getBalance().toString()
                                val responseData = it.data.body()?.data
                                binding.balance.text = total
                                binding.subTotal.text = total
                                binding.totalPayment.text = "Pay : ${viewModel.getSupplierPayment()}"
                                binding.invoiceAmount.text = "Inv : ${viewModel.getSupplierPurchase()}"
                                binding.totalRtv.text = "Rtv : ${it.data.body()?.data?.lastYearsPurchaseReturnTotal}"

                                responseData?.let {
                                    binding.purchaseAmountTv.text = (responseData.lastYearsDebit + responseData.lastYearsDiscount).toString()
                                    binding.paymentAmountTv.text = responseData.lastYearsDebit.toString()
                                    binding.purchaseReturnTv.text = responseData.lastYearsPurchaseReturnTotal.toString()
                                }

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
                        loader.dismiss()
                        showToast("${it}")
                    }
                    Status.LOADING -> {
                        loader.show()
                    }
                }
            }
        })

        binding.searchBtn.setOnClickListener {
            viewModel.searchPurchase(args.supplier?.id?:0, binding.dateFromTv.text.toString(),binding.dateToTv.text.toString()).observe(
                viewLifecycleOwner, Observer {
                    it?.let { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                when (it.data?.body()?.success) {
                                    200 -> {
                                        loader.dismiss()
                                        val balance = viewModel.getSupplierBalanceList()
                                        adapter = SupplierTransactionAdapter(balance)
                                        binding.supplierDetailsRv.adapter = adapter
                                        adapter.notifyDataSetChanged()

                                        val total = viewModel.getBalance().toString()
                                        val responseData = it.data.body()?.data
                                        binding.balance.text = total
                                        binding.subTotal.text = total
                                        binding.totalPayment.text = "Pay : ${viewModel.getSupplierPayment()}"
                                        binding.invoiceAmount.text = "Inv : ${viewModel.getSupplierPurchase()}"
                                        binding.totalRtv.text = "Rtv : ${it.data.body()?.data?.lastYearsPurchaseReturnTotal}"

                                        responseData?.let {
                                            binding.purchaseAmountTv.text = (responseData.lastYearsDebit + responseData.lastYearsDiscount).toString()
                                            binding.paymentAmountTv.text = responseData.lastYearsDebit.toString()
                                            binding.purchaseReturnTv.text = responseData.lastYearsPurchaseReturnTotal.toString()
                                        }

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
                                loader.dismiss()
                                showToast("${it}")
                            }
                            Status.LOADING -> {
                                loader.show()
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


            binding.datePurchase.text = binding.dateToTv.text.toString()
            binding.datePayment.text = binding.dateToTv.text.toString()
            binding.datePurchaseRtv.text = binding.dateToTv.text.toString()
        }
    }

}
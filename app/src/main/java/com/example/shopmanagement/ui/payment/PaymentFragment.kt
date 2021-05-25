package com.example.shopmanagement.ui.payment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.example.shopmanagement.BaseFragment
import com.example.shopmanagement.R
import com.example.shopmanagement.data.models.api.payment.PaymentList
import com.example.shopmanagement.databinding.DialogPaymentShowBinding
import com.example.shopmanagement.databinding.FragmentPaymentBinding
import com.example.shopmanagement.ui.activity.MainActivity
import com.example.shopmanagement.ui.activity.PaymentActivity
import com.example.shopmanagement.ui.utils.EventObserver
import com.example.shopmanagement.ui.utils.Status
import com.example.shopmanagement.ui.utils.showToast
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.dialog_payment_show.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@AndroidEntryPoint
class PaymentFragment : BaseFragment(),IPaymentListener {
    lateinit var binding : FragmentPaymentBinding
    private val viewModel : PaymentViewModel by activityViewModels()
    lateinit var adapter : PaymentAdapter
    lateinit var dialog : MaterialDialog
    lateinit var  loader : AlertDialog
    private var searchJob : Job? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(requireContext(), MainActivity::class.java)
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
        binding = FragmentPaymentBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val toolbar = activity?.findViewById<TextView>(R.id.titleToolbar)
        toolbar?.setText("Payment")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loader = SpotsDialog.Builder().setContext(requireContext()).build()
        datePickerSetup()

        binding.dateFromTv.text = todayDate()
        binding.dateToTv.text = lastDate()

        viewModel.fromEditBtn = false
        viewModel.getListOfPayment()

        initAdapter()
        setList()

        binding.searchBtn.setOnClickListener {
            searchPayment()
        }

        viewModel.totalAmount.observe(viewLifecycleOwner, Observer {
            binding.totalAmountTv.text = it
        })

//        viewModel.getApiPaymentList().observe(viewLifecycleOwner, Observer {
//            it?.let { resource ->
//                when (resource.status) {
//                    Status.SUCCESS -> {
//                        loader.dismiss()
//                        when (it.data?.body()?.success) {
//                            200 -> {
//                                adapter = PaymentAdapter(viewModel.getPaymentList(),this)
//                                binding.paymentRv.adapter = adapter
//                                adapter.notifyDataSetChanged()
//                                viewModel.totalAmount()
//
//                                val list = it.data.body()?.data?.data
//
//                            }
//                            201 -> {
//                                showToast("Phone Number Already Used")
//                            }
//                            else -> {
//                                showToast("${it.data?.body()}")
//                            }
//                        }
//                    }
//                    Status.ERROR -> {
//                        loader.dismiss()
//                        showToast("$it")
//                    }
//                    Status.LOADING -> {
//                        loader.show()
//                    }
//                }
//            }
//        })

        viewModel.totalAmount.observe(viewLifecycleOwner, Observer {
            binding.totalAmountTv.text = it
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, EventObserver {
            showToast(it)
        })

        binding.paymentBtn.setOnClickListener {
            viewModel.clearEditPaymentData()
            findNavController().navigate(R.id.action_paymentFragment_to_supplierPaymentFragment)
        }

//        binding.searchBtn.setOnClickListener {
//            if(viewModel.searchValidation()){
//                viewModel.getSupplierByName(binding.nameFormTv.text.toString())
//                viewModel.supplierDetails.observe(viewLifecycleOwner, Observer {
//
//                    viewModel.searchPayment(it.id.toString(),binding.dateFromTv.text.toString(),binding.dateToTv.text.toString()).observe(viewLifecycleOwner,
//                        Observer {
//                            it?.let {resource->
//                                when(resource.status){
//                                    Status.SUCCESS->{
//                                        val body = it.data?.body()
//                                        body?.let {body->
//                                            when(body.success){
//                                                200->{
//                                                    loader.dismiss()
//                                                    viewModel.setPaymentList(body.data.data)
//                                                    adapter = PaymentAdapter(viewModel.getPaymentList(),this)
//                                                    binding.paymentRv.adapter = adapter
//                                                    adapter.notifyDataSetChanged()
//                                                    viewModel.totalAmount()
//                                                }
//                                                else->{
//                                                    showToast(body.msg)
//                                                }
//                                            }
//                                        }
//                                    }
//                                    Status.ERROR->{
//                                        loader.dismiss()
//                                        showToast(it.message.toString())
//                                    }
//                                    Status.LOADING->{
//                                        loader.show()
//                                    }
//                                }
//
//                            }
//                        })
//                })
//            }else{
//                showToast("Supplier id is Required")
//            }
//
//
//        }


        binding.nameFormTv.addTextChangedListener {
            if(it.isNullOrEmpty()){
                val intent = Intent(this.requireContext(), PaymentActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }


    }

    private fun searchPayment() {
        val mobile = binding.nameFormTv.text?.trim().toString()
        val fromDate = binding.dateFromTv.text?.trim().toString()
        val toDate = binding.dateToTv.text?.trim().toString()
        var id = ""

        if(mobile.isNotEmpty()){
            viewModel.getSupplier()
            viewModel.supplierDetails.observe(viewLifecycleOwner, Observer {
                if(it !=null){
                  id = it.id.toString()
                }else{
                    showToast("Please Enter Valid Number")
                }
            })
        }

        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchPayment(id,fromDate,toDate).collect {
                it.let { adapter.submitData(it) }
            }
        }
    }

    private fun initAdapter() {
        binding.paymentRv.layoutManager = LinearLayoutManager(requireContext())
        adapter = PaymentAdapter(this)
        binding.paymentRv.adapter = adapter
    }

    private fun setList(){
        lifecycleScope.launch {
            viewModel.paymentList().collect {
                it.let { adapter.submitData(it) }
            }
        }
    }

    /**Date Picker**/
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
        }
    }

    override fun onPaymentItemClick(payment : PaymentList) {
        viewModel.onPaymentItemClick(payment)

        dialog = MaterialDialog(this.requireContext())

        val dialogBinding  = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.dialog_payment_show,
            null,
            false
        ) as DialogPaymentShowBinding

        dialogBinding.viewModel = viewModel
        dialog.setContentView(dialogBinding.root)

        dialog.paymentCheckLayout.visibility = View.GONE
        dialog.show()
        dialogBinding.cancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.editBtn.setOnClickListener {
            viewModel.setSupplier(payment)
            viewModel.fromEditBtn = true
            findNavController().navigate(R.id.supplierPaymentFragment)
            dialog.dismiss()
        }
    }

    override fun deletePayment(payment: PaymentList) {
        val dialog = MaterialDialog(requireContext())
            .title(R.string.payment_delete_title)
            .message(R.string.payment_delete_message)
        dialog.show()

        dialog.negativeButton {
            dialog.dismiss()
        }


        dialog.positiveButton {
            viewModel.deletePayment(payment).observe(viewLifecycleOwner, Observer {
                it?.let {resource->
                    when(resource.status){

                        Status.SUCCESS->{
                            loader.dismiss()
                            when(it.data?.body()?.success){
                                200->{
                                    viewModel.deleteFromLocal(payment)
                                    adapter.notifyDataSetChanged()
                                    viewModel.totalAmount()
                                }
                                else->{
                                    showToast(it.message.toString())
                                }
                            }
                        }
                        Status.LOADING->{
                            loader.show()
                        }
                        Status.ERROR->{
                            loader.dismiss()
                            showToast("Not Deleted")
                        }
                    }
                }
            })

            dialog.dismiss()
        }
    }

    override fun toPaymentDetails(payment : PaymentList) {
        viewModel.onPaymentItemClick(payment)
        findNavController().navigate(R.id.action_paymentFragment_to_paymentDetails)
    }

}
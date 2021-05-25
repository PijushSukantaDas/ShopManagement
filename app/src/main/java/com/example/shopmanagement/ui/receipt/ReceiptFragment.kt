package com.example.shopmanagement.ui.receipt

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
import com.example.shopmanagement.data.models.api.receipt.DataList
import com.example.shopmanagement.databinding.DialogReceiptShowBinding
import com.example.shopmanagement.databinding.FragmentReciptBinding
import com.example.shopmanagement.ui.activity.MainActivity
import com.example.shopmanagement.ui.activity.ReceiptActivity
import com.example.shopmanagement.ui.utils.EventObserver
import com.example.shopmanagement.ui.utils.LoadingDialog
import com.example.shopmanagement.ui.utils.Status
import com.example.shopmanagement.ui.utils.showToast
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import dmax.dialog.SpotsDialog
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@AndroidEntryPoint
class ReceiptFragment : BaseFragment(),IReceiptListener {

    lateinit var binding : FragmentReciptBinding
    private val receiptActivityViewModel : ReceiptViewModel by activityViewModels()
    lateinit var adapter : ReceiptAdapter
    lateinit var dialog : MaterialDialog
    lateinit var  loader : AlertDialog
    private var searchJob: Job? = null


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
        binding = FragmentReciptBinding.inflate(inflater,container,false)
        binding.viewModel = receiptActivityViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val toolbar = activity?.findViewById<TextView>(R.id.titleToolbar)
        toolbar?.setText("Receipt")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        receiptActivityViewModel.fromEdit = false
        receiptActivityViewModel.getListOfReceipt()
        loader = SpotsDialog.Builder().setContext(requireContext()).build()
        datePickerSetup()

        binding.dateFromTv.text = todayDate()
        binding.dateToTv.text = lastDate()

        initAdapter()
        setReceiptList()

        receiptActivityViewModel.totalAmount.observe(viewLifecycleOwner, Observer {
            binding.totalAmountTv.text = it
        })

        binding.searchBtn.setOnClickListener {
            searchReceipt()
        }

        binding.receiptBtn.setOnClickListener {
            receiptActivityViewModel.clearEditReceiptData()
            findNavController().navigate(R.id.action_receiptFragment_to_customerReceiptFragment)
        }

        receiptActivityViewModel.totalAmount.observe(viewLifecycleOwner, Observer {
            binding.totalAmountTv.text = it
        })

        receiptActivityViewModel.errorMessage.observe(viewLifecycleOwner, EventObserver {
            showToast(it)
        })


        receiptActivityViewModel.searchReceiptList.observe(viewLifecycleOwner, Observer {
            adapter = ReceiptAdapter(this)
            binding.receiptRv.adapter = adapter
            adapter.notifyDataSetChanged()
            receiptActivityViewModel.totalAmount()
        })


        binding.nameFormTv.addTextChangedListener {
            if(it.isNullOrEmpty()){
                val intent = Intent(requireContext(), ReceiptActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }

    }

    private fun searchReceipt() {

        val mobile = binding.nameFormTv.text?.trim().toString()
        val fromDate = binding.dateFromTv.text?.trim().toString()
        val toDate = binding.dateToTv.text.trim().toString()
        var id = ""
        if(mobile.isNotEmpty()) {
            receiptActivityViewModel.getCustomer(mobile)
            receiptActivityViewModel.customerDetails.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    id = it.id.toString()
                } else {
                    showToast("Please Enter a Valid Number")
                }
            })
        }
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            receiptActivityViewModel.searchReceipt(id,fromDate,toDate).collect {
                it.let { adapter.submitData(it) }
            }
        }
    }

    private fun setReceiptList(){
        lifecycleScope.launch {
            receiptActivityViewModel.receiptList().collect {
                it.let { adapter.submitData(it) }
            }
        }
    }

    private fun initAdapter() {
        binding.receiptRv.layoutManager = LinearLayoutManager(requireContext())
        adapter = ReceiptAdapter(this)
        binding.receiptRv.adapter = adapter
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

    override fun onReceiptClick(receipt: DataList) {
        dialog = MaterialDialog(this.requireContext())

        val dialogBinding  = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.dialog_receipt_show,
            null,
            false
        ) as  DialogReceiptShowBinding

        dialogBinding.viewModel = receiptActivityViewModel
        dialog.setContentView(dialogBinding.root)

        receiptActivityViewModel.onReceiptItemClick(receipt)
        receiptActivityViewModel.getCustomerServerDetails(receipt.id).observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when(resource.status){
                    Status.SUCCESS->{
                        dialogBinding.customerBalance.text = it.data?.body()?.data?.info?.due_amount?.toString()?:"0"
                    }
                    Status.LOADING->{

                    }
                    Status.ERROR->{

                    }
                }
            }
        })


        dialog.show()

        dialogBinding.cancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.editBtn.setOnClickListener {
            receiptActivityViewModel.fromEdit = true
            receiptActivityViewModel.setReceipt(receipt)
            findNavController().navigate(R.id.customerReceiptFragment)
            dialog.dismiss()
        }
    }

    override fun deleteReceipt(receipt: DataList) {
        val dialog = MaterialDialog(requireContext())
            .title(R.string.receipt_delete_title)
            .message(R.string.receipt_delete_message)
        dialog.show()

        dialog.negativeButton {
            dialog.dismiss()
        }

        dialog.positiveButton {

            receiptActivityViewModel.deleteReceipt(receipt).observe(viewLifecycleOwner, Observer {
                it?.let {resource->
                    when(resource.status){

                        Status.SUCCESS->{
                            LoadingDialog(requireContext()).dismissDialog()
                            when(it.data?.body()?.success){
                                200->{
                                    receiptActivityViewModel.deleteFormLocalList(receipt)
                                    adapter.notifyDataSetChanged()
                                    receiptActivityViewModel.totalAmount()
                                }
                                else->{
                                    showToast(it.message.toString())
                                }
                            }
                        }
                        Status.LOADING->{
                            LoadingDialog(requireContext()).showDialog()
                        }
                        Status.ERROR->{
                            LoadingDialog(requireContext()).dismissDialog()
                            showToast("Not Deleted")
                        }
                    }
                }
            })

            dialog.dismiss()
        }

    }

    override fun toReceiptDetails(receipt: DataList) {
        receiptActivityViewModel.setReceipt(receipt)
        findNavController().navigate(R.id.action_receiptFragment_to_receiptDetails)
    }


}
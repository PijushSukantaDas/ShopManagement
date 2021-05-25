package com.example.shopmanagement.ui.payment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.shopmanagement.BaseFragment
import com.example.shopmanagement.databinding.FragmentSupplierPaymentBinding
import com.example.shopmanagement.ui.activity.PaymentActivity
import com.example.shopmanagement.ui.purchase.PurchaseViewModel
import com.example.shopmanagement.ui.utils.Status
import com.example.shopmanagement.ui.utils.showToast
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import dmax.dialog.SpotsDialog
import java.text.SimpleDateFormat


@AndroidEntryPoint
class SupplierPaymentFragment : BaseFragment() {

    lateinit var binding : FragmentSupplierPaymentBinding
    private val viewModel : PaymentViewModel by activityViewModels()
    private val purchaseViewModel : PurchaseViewModel by viewModels()
    lateinit var  loader : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSupplierPaymentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loader = SpotsDialog.Builder().setContext(requireContext()).build()



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

        if(viewModel.fromEditBtn){
            binding.searchLayout.visibility = View.GONE
            binding.supplier.visibility = View.GONE

            viewModel.supplierDetails.observe(viewLifecycleOwner, Observer { supplier ->
                binding.companyNameTv.text = supplier.name
                binding.contactPersonTv.text = supplier.contactPerson
                binding.contactPersonNumber.text = supplier.mobile
                binding.ownerNameTv.text = supplier.ownerName
            })
        }else{
            viewModel.getSupplier().observe(this.viewLifecycleOwner, Observer {
                adapterCustomer = ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.select_dialog_item,
                    it
                )
                binding.paymentSupplierSelection.setAdapter(adapterCustomer)
            })


        }


        binding.paymentSupplierSelection.setOnItemClickListener { parent, view, position, id ->
            val data = adapterCustomer.getItem(position)
            viewModel.getSupplierByName("$data")
            binding.paymentSupplierSelection.setText("")

            viewModel.supplierDetails.observe(viewLifecycleOwner, Observer { supplier ->
                binding.companyNameTv.text = supplier.name
                binding.contactPersonTv.text = supplier.contactPerson
                binding.contactPersonNumber.text = supplier.mobile
                binding.ownerNameTv.text = supplier.ownerName
            })
        }





        binding.discountEdt.addTextChangedListener {
            if(it.isNullOrEmpty() && binding.amountEdt.text.toString().isNullOrEmpty()){
                viewModel.calculateDue(0.0, 0.0)
            }else if(it.isNullOrEmpty() && binding.amountEdt.text.toString().isNotEmpty()){
                viewModel.calculateDue(binding.amountEdt.text.toString().toDouble(), 0.0)
            }else if(it.toString().isNotEmpty() && binding.amountEdt.text.toString().isNullOrEmpty()){
                viewModel.calculateDue(0.0, it.toString().toDouble())
            }else{
                viewModel.calculateDue(
                    binding.amountEdt.text.toString().toDouble(),
                    it.toString().toDouble()
                )
            }

        }

        binding.amountEdt.addTextChangedListener {

            if(it.isNullOrEmpty() && binding.discountEdt.text.toString().isEmpty()){
                viewModel.calculateDue(0.0, 0.0)
            }else if(it.isNullOrEmpty() && binding.discountEdt.text.toString().isNotEmpty()){
                viewModel.calculateDue(0.0, binding.discountEdt.text.toString().toDouble())
            }else if(it.toString().isNotEmpty() && binding.discountEdt.text.toString().isEmpty()){
                viewModel.calculateDue(it.toString().toDouble(), 0.0)
            }else{
                viewModel.calculateDue(
                    it.toString().toDouble(),
                    binding.discountEdt.text.toString().toDouble()
                )
            }

        }

        viewModel.afterDiscountAmount.observe(viewLifecycleOwner, Observer {
            binding.totalAfterDiscountTv.text = it.toString()
        })

        binding.paymentBtn.setOnClickListener {
            if(binding.dateTv.text.toString().isNullOrEmpty()){
                showToast("Insert Date")
            }else if(binding.amountEdt.text.toString().isNullOrEmpty()){
                showToast("Insert Payment Amount")
            }else{
                viewModel.insertApiPayment(binding.dateTv.text.toString()).observe(viewLifecycleOwner, Observer {
                    it?.let { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                loader.dismiss()
                                showToast("Payment Inserted")
                                val intent = Intent(this.requireContext(), PaymentActivity::class.java)
                                startActivity(intent)
                                requireActivity().finish()
                            }
                            Status.ERROR -> {
                                loader.dismiss()
                                showToast("Payment Error")
                            }
                            Status.LOADING -> {
                                loader.show()
                            }
                        }

                    }
                })
            }

        }
    }


}
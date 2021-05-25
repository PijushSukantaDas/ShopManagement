package com.example.shopmanagement.ui.purchase.confirm

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.shopmanagement.BaseFragment
import com.example.shopmanagement.databinding.FragmentPurchaseConfirmBinding
import com.example.shopmanagement.ui.activity.PurchaseActivity
import com.example.shopmanagement.ui.purchase.PurchaseViewModel
import com.example.shopmanagement.ui.utils.Status
import com.example.shopmanagement.ui.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import dmax.dialog.SpotsDialog

@AndroidEntryPoint
class PurchaseConfirmFragment : BaseFragment() {
    lateinit var binding : FragmentPurchaseConfirmBinding
    private val viewModel : PurchaseViewModel by activityViewModels()
    lateinit var  loader : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.currentDue.value = viewModel.totalPurchaseBill.toString()
        viewModel.billNo()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPurchaseConfirmBinding.inflate(inflater,container,false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loader = SpotsDialog.Builder().setContext(requireContext()).build()

        viewModel.payment = 0.0
        viewModel.discount = 0.0
        viewModel.currentDue.value = "0"
        viewModel.customerName.value = ""
        viewModel.billFromEdit()

        viewModel.supplier.observe(this.viewLifecycleOwner, Observer {
            binding.companyName.text = it.name
            binding.contactPersonName.text = it.contactPerson
            binding.mobileNo.text = it.mobile
        })


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
                viewModel.discount = binding.discountTv.text.toString().toDouble()
                viewModel.calculateDue(binding.paymentTv.text.toString().toDouble(),binding.discountTv.text.toString().toDouble())
            }
        }

        viewModel.currentDue.observe(this.viewLifecycleOwner, Observer {
            binding.currentDueTv.setText(it)
        })


        binding.confirmBtn.setOnClickListener {
            viewModel.confirmClick = true
            viewModel.arrayOfProduct()
            if(viewModel.payment>0.0){
                viewModel.insertPurchase().observe(viewLifecycleOwner, Observer {
                    it?.let {resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                loader.dismiss()
                                viewModel.clickOnConfirm(it.data?.body()?.data)

                                /**Inserting Payment Information to Web Server**/
                                viewModel.insertApiPayment()

                                /**Transition to Purchase Home**/
                                val intent = Intent(requireContext(),PurchaseActivity::class.java)
                                startActivity(intent)
                                requireActivity().finish()
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

        viewModel.supplierInvoiceListTotalDue.observe(this.viewLifecycleOwner, Observer {
            binding.oldDue.setText(it.toString())
        })
    }

}
package com.example.shopmanagement.ui.purchase.supplier

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.shopmanagement.BaseFragment
import com.example.shopmanagement.R
import com.example.shopmanagement.data.models.PurchaseInvoice
import com.example.shopmanagement.databinding.*
import com.example.shopmanagement.ui.activity.PurchaseActivity
import com.example.shopmanagement.ui.purchase.PurchaseViewModel
import com.example.shopmanagement.ui.sales.details.InvoiceDetailsAdapter
import com.example.shopmanagement.ui.utils.EventObserver
import com.example.shopmanagement.ui.utils.Status
import com.example.shopmanagement.ui.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.invoice_dialog_show.*

@AndroidEntryPoint
class SupplierSelectFragment : BaseFragment(), SupplierPurchaseListener {
    lateinit var binding : FragmentSupplierSelectBinding
    private val viewModel : PurchaseViewModel by activityViewModels()
    lateinit var adapter : SupplierPurchaseInvoiceAdapter

    private lateinit var dialog: MaterialDialog
    lateinit var dialogAdapter : InvoiceDetailsAdapter
    lateinit var  loader : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loader = SpotsDialog.Builder().setContext(requireContext()).build()
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(requireContext(), PurchaseActivity::class.java))
                requireActivity().finish()
            }
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSupplierSelectBinding.inflate(inflater,container,false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.productSelectionBtn.setOnClickListener {
//            viewModel.onProductSelectClick()
//            viewModel.navigate.observe(this.viewLifecycleOwner, EventObserver {
//                if (it) {
//
//                } else {
//                    showToast("No Supplier Selected!")
//                }
//            })
            if(binding.companyName.text.isNotEmpty()){
                findNavController().navigate(R.id.action_supplierSelectFragment_to_productSelectFragment)
            }else{
                showToast("Select a Supplier")
            }
        }


        var adapterCustomer = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.select_dialog_item,
            listOf()
        )

        viewModel.getSupplierName().observe(this.viewLifecycleOwner, Observer {
            adapterCustomer = ArrayAdapter<String>(
                requireContext(),
                android.R.layout.select_dialog_item,
                it
            )
            binding.selectSupplierField.setAdapter(adapterCustomer)
        })

        binding.selectSupplierField.setOnItemClickListener { parent, view, position, id ->
            val data = adapterCustomer.getItem(position)
            binding.selectSupplierField.setText("")
            viewModel.getSupplierDetails("$data")
            viewModel.supplierDetails = "$data"
            setSupplierInfo()
            binding.supplierSelectRv.layoutManager = LinearLayoutManager(requireContext())

            viewModel.supplierInvoiceList.observe(this.viewLifecycleOwner, Observer {
                adapter = SupplierPurchaseInvoiceAdapter(it,this)
                binding.supplierSelectRv.adapter = adapter
                adapter.notifyDataSetChanged()

                viewModel.getTotalResult()
            })

            viewModel.supplierInvoiceListTotalBill.observe(this.viewLifecycleOwner, Observer {
                binding.totalCustomerInvoice.text = it.toString()
            })

            viewModel.supplierInvoiceListTotalPaid.observe(this.viewLifecycleOwner, Observer {
                binding.totalCustomerPaid.text = it.toString()
            })

            viewModel.supplierInvoiceListTotalDue.observe(this.viewLifecycleOwner, Observer {
                binding.totalCustomerDue.text = it.toString()
            })

        }

        binding.createSupplierBtn.setOnClickListener {
            createSupplier()
        }
    }

    private fun setSupplierInfo() {
        viewModel.getSupplierDetailsPurchase().observe(viewLifecycleOwner, Observer {
            if(it != null){
                binding.companyName.text = it.name
                binding.contactPersonTv.text = it.contactPerson
                binding.mobileTv.text = it.mobile
            }
        })
    }

    private fun createSupplier() {

        dialog = MaterialDialog(this.requireContext())
            .customView(R.layout.create_supplier_from_purchase)


        val dialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.create_supplier_from_purchase,
            null,
            false
        ) as CreateSupplierFromPurchaseBinding

        dialogBinding.viewModel = viewModel

        dialog.setContentView(dialogBinding.root)

        dialog.cancelOnTouchOutside(false)

        dialog.show()

        dialogBinding.createCustomer.setOnClickListener {
            if(viewModel.supplierValidation()){
                viewModel.createSupplier().observe(viewLifecycleOwner, Observer {
                    it?.let { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                loader.dismiss()
                                if(it.data?.body()?.success == 200){
                                    viewModel.createLocalCustomer(it.data?.body()?.data)
                                    setSupplierInfo()
                                    dialog.dismiss()
                                }else{
                                    showToast("${it.data?.body()?.msg}")
                                }

                            }
                            Status.ERROR -> {
                                dialog.dismiss()
                                loader.dismiss()
                                showToast("Error")
                            }
                            Status.LOADING -> {
                                loader.dismiss()
                            }
                        }
                    }
                })
            }else{
                viewModel.errorMessage.observe(viewLifecycleOwner,EventObserver{
                    showToast(it)
                })
            }

        }

        dialogBinding.cancelDialog.setOnClickListener {
            dialog.dismiss()
        }


    }

    override fun onSupplierInvoiceClick(invoice : PurchaseInvoice) {
        viewModel.clearData()
        dialogAdapter = InvoiceDetailsAdapter(listOf())

        dialog = MaterialDialog(requireContext())

        val dialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.supplier_invoice_show,
            null,
            false
        ) as SupplierInvoiceShowBinding

        dialog.setContentView(dialogBinding.root)

//        viewModel.purchaseInvoiceEdit(invoice)
        dialogAdapter.notifyDataSetChanged()

        dialogBinding.viewModel = viewModel

        dialogBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        dialogBinding.recyclerView.adapter = dialogAdapter

//        viewModel.product.observe(this.viewLifecycleOwner, Observer {
//            dialogAdapter = InvoiceDetailsAdapter(it)
//            dialogBinding.recyclerView.adapter = dialogAdapter
//            dialogAdapter.notifyDataSetChanged()
//            dialogBinding.purchaseTotalItem.setText(it.size.toString())
//        })

        viewModel.totalQuantity.observe(this.viewLifecycleOwner, Observer {
            dialogBinding.purchaseTotalQuantity.setText(it.toString())
        })

        viewModel.purchaseInvoiceDetails.observe(this.viewLifecycleOwner, Observer {
            it?.let {
                dialogBinding.purchaseTotalBill.setText(it.totalAmount.toString())
                dialogBinding.purchaseDue.setText(it.dueAmount.toString())
                dialogBinding.purchasePartialPaid.setText(it.partialPayment.toString())
                dialogBinding.purchaseDate.text = it.createdAt.toString()
                dialogBinding.purchaseBillNo.text = it.billNo.toString()
            }

        })

        viewModel.supplier.observe(this.viewLifecycleOwner, Observer {
            it?.let {
                dialogBinding.supplierName.text = it.contactPerson
                dialogBinding.supplierMobile.text = it.mobile
            }

        })

        viewModel.supplierInvoiceListTotalDue.observe(this.viewLifecycleOwner, Observer {
            dialogBinding.purchaseOldDue.setText(it.toString())
        })

        dialog.closeDialog.setOnClickListener {
            viewModel.product.value?.clear()
            dialog.dismiss()
        }

        dialog.cancelOnTouchOutside(false)

        dialog.show()
    }

}
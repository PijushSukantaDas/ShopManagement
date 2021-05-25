package com.example.shopmanagement.ui.sales.customer

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
import com.example.shopmanagement.data.models.Invoice
import com.example.shopmanagement.databinding.CustomerCreateFromSaleBinding
import com.example.shopmanagement.databinding.CustomerInvoiceShowBinding
import com.example.shopmanagement.databinding.FragmentSelectCustomerBinding
import com.example.shopmanagement.ui.activity.SalesActivity
import com.example.shopmanagement.ui.sales.SelectCustomerViewModel
import com.example.shopmanagement.ui.sales.details.InvoiceDetailsAdapter
import com.example.shopmanagement.ui.utils.EventObserver
import com.example.shopmanagement.ui.utils.Status
import com.example.shopmanagement.ui.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.invoice_dialog_show.*

@AndroidEntryPoint
class SelectCustomerFragment : BaseFragment(),Listener{
    lateinit var binding : FragmentSelectCustomerBinding
    val viewModel : SelectCustomerViewModel by activityViewModels()
    lateinit var adapter : CustomerInvoiceListAdapter
    private lateinit var dialog: MaterialDialog
    lateinit var dialogAdapter : InvoiceDetailsAdapter
    private val createCustomer = "Create Customer"
    lateinit var  loader : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loader = SpotsDialog.Builder().setContext(requireContext()).build()
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.clearData()
                startActivity(Intent(requireContext(), SalesActivity::class.java))
                requireActivity().finish()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        viewModel.validCustomer.value = false
        binding = FragmentSelectCustomerBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.clearData()

        var adapterCustomer = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.select_dialog_item,
            listOf()
        )

        binding.customerSelectRv.layoutManager = LinearLayoutManager(requireContext())
        adapter = CustomerInvoiceListAdapter(listOf(),this)
        binding.customerSelectRv.adapter = adapter


        viewModel.getCustomerName().observe(this.viewLifecycleOwner, Observer {

            adapterCustomer = ArrayAdapter<String>(
                requireContext(),
                android.R.layout.select_dialog_item,
                it
            )
            binding.selectCustomerField.setAdapter(adapterCustomer)
//            showToast(it.toString())
        })

        binding.productSelectionBtn.setOnClickListener {
            if(viewModel.validCustomer.value == true &&  viewModel.currentCustomer.value != null){
                viewModel.newProductSelect = true
                findNavController().navigate(R.id.action_selectCustomerFragment_to_productSelectionFragment)
            }else{
                showToast(getString(R.string.customer_valid))
            }

        }



        binding.selectCustomerField.setOnItemClickListener { parent, view, position, id ->
            val data = adapterCustomer.getItem(position)
                viewModel.validCustomer.value = true
                viewModel.customer = "$data"
                viewModel.currentCustomer.value = "$data"

                getCustomerDetails()

                viewModel.customerInvoiceList.observe(this.viewLifecycleOwner, Observer {
                    adapter = CustomerInvoiceListAdapter(it, this)
                    binding.customerSelectRv.adapter = adapter
                    adapter.notifyDataSetChanged()

                    viewModel.getTotalResult()
                })

                viewModel.customerInvoiceListTotalBill.observe(this.viewLifecycleOwner, Observer {
                    binding.totalCustomerInvoice.text = it.toString()
                })

                viewModel.customerInvoiceListTotalPaid.observe(this.viewLifecycleOwner, Observer {
                    binding.totalCustomerPaid.text = it.toString()
                })

                viewModel.customerInvoiceListTotalDue.observe(this.viewLifecycleOwner, Observer {
                    binding.totalCustomerDue.text = it.toString()
                })
                binding.selectCustomerField.setText("")
//            showToast(data.toString())

        }

        binding.createCustomer.setOnClickListener {
            createNewCustomer()
        }

    }

    private fun getCustomerDetails() {
        viewModel.getCustomerDetails().observe(this.viewLifecycleOwner, Observer {
            if(it!=null){
                binding.customer = it
                viewModel.setSelectedCustomer(it)
            }

        })
    }

    private fun createNewCustomer() {
        dialog = MaterialDialog(this.requireContext())
            .customView(R.layout.customer_create_from_sale)


        val dialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.customer_create_from_sale,
            null,
            false
        ) as CustomerCreateFromSaleBinding

        dialogBinding.viewModel = viewModel

        dialog.setContentView(dialogBinding.root)

        dialog.cancelOnTouchOutside(false)

        dialog.show()

        dialogBinding.createCustomer.setOnClickListener {

            if(viewModel.customerFieldValidation()){
                viewModel.createUser().observe(viewLifecycleOwner, Observer {
                    it?.let { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                loader.dismiss()
                                when (it.data?.body()?.success) {

                                    200 -> {
                                        viewModel.createLocalCustomer(it.data.body()?.data!!)
                                        getCustomerDetails()
                                        dialog.dismiss()

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
                                showToast("Error")
                            }
                            Status.LOADING -> {
                                loader.show()
                            }
                        }
                    }
                })
            }else{
                viewModel.errorMessage.observe(viewLifecycleOwner, EventObserver{
                    showToast(it)
                })
            }
        }

        dialogBinding.cancelDialog.setOnClickListener {
            dialog.dismiss()
        }
    }

    override fun showDetails(invoice: Invoice) {
//        viewModel.showDetails(invoice)
        adapter.notifyDataSetChanged()
        dialog = MaterialDialog(this.requireContext())
            .customView(R.layout.customer_invoice_show)


        val dialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.customer_invoice_show,
            null,
            false
        ) as CustomerInvoiceShowBinding

        dialog.setContentView(dialogBinding.root)

        dialogBinding.viewModel =viewModel

        dialog.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.product.observe(this.viewLifecycleOwner, Observer {
            dialogAdapter = InvoiceDetailsAdapter(listOf())
            dialogBinding.recyclerView.adapter = dialogAdapter
            dialog.invoiceTotalItem.setText(it.size.toString())
            dialogAdapter.notifyDataSetChanged()
        })

        viewModel.totalQuantity.observe(this.viewLifecycleOwner, Observer {
            dialog.totalQuantity.setText(it.toString())
        })

        viewModel.selectedInvoice.observe(this.viewLifecycleOwner, Observer {
            it?.let {
                dialog.totalBill.setText(it.totalAmount.toString())
                dialog.invoieDue.setText(it.dueAmount.toString())
                dialog.invoicePartialPaid.setText(it.partialPayment.toString())
                dialog.invoiceDate.text = it.createdAt.toString()
            }

        })

        viewModel.invoiceCustomerDetails.observe(this.viewLifecycleOwner, Observer {
            it?.let {
                dialog.customerName.text = it.customerName
                dialog.customerMobile.text = it.customerMobile
            }

        })


        dialog.closeDialog.setOnClickListener {
            viewModel.product.value?.clear()
            dialog.dismiss()
        }

        dialog.cancelOnTouchOutside(false)

        dialog.show()
    }

}
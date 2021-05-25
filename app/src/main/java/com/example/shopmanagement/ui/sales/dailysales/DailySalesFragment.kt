package com.example.shopmanagement.ui.sales.dailysales

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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog

import com.example.shopmanagement.BaseFragment
import com.example.shopmanagement.R
import com.example.shopmanagement.data.models.Invoice
import com.example.shopmanagement.data.models.api.invoice.list.InvoiceData
import com.example.shopmanagement.databinding.FragmentDailySalesBinding
import com.example.shopmanagement.databinding.InvoiceDialogShowBinding
import com.example.shopmanagement.ui.activity.MainActivity
import com.example.shopmanagement.ui.activity.SalesActivity
import com.example.shopmanagement.ui.sales.SelectCustomerViewModel
import com.example.shopmanagement.ui.sales.details.InvoiceDetailsAdapter
import com.example.shopmanagement.ui.utils.EventObserver
import com.example.shopmanagement.ui.utils.Status
import com.example.shopmanagement.ui.utils.showToast
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.invoice_dialog_show.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat

@AndroidEntryPoint
class DailySalesFragment : BaseFragment(),InvoiceListener {
    lateinit var bindings: FragmentDailySalesBinding
    private val salesViewModel : SelectCustomerViewModel by activityViewModels()
    private val viewModel : SalesHistoryViewModel by viewModels()
    lateinit var adapter : InvoiceAdapter
    private lateinit var dialog: MaterialDialog
    lateinit var dialogAdapter : InvoiceDetailsAdapter
    lateinit var  loader : AlertDialog
    private var searchJob: Job? = null
    private var total = 0.0

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
        bindings = FragmentDailySalesBinding.inflate(inflater,container,false)
        bindings.lifecycleOwner = viewLifecycleOwner
        bindings.viewModel = salesViewModel

        val toolbar = activity?.findViewById<TextView>(R.id.titleToolbar)
        toolbar?.setText("Sales")

        return bindings.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loader = SpotsDialog.Builder().setContext(requireContext()).build()
        datePickerSetup()

        salesViewModel.resetValues()
        salesViewModel.getInvoiceList()

        initAdapter()

        setList()


//        viewModel.total.observe(viewLifecycleOwner, Observer {
//            if(it!=null){
//                bindings.totalInvoiceAmount.text = it.toString()
//            }
//        })

        bindings.dateFromTv.text = todayDate()
        bindings.dateToTv.text = lastDate()

        salesViewModel.fromEditScreen = false


        bindings.salesBtn.setOnClickListener {
            salesViewModel.fromEditScreen = false
            findNavController().navigate(R.id.action_dailySalesFragment_to_selectCustomerFragment)
        }

        bindings.searchBtn.setOnClickListener {
            searchInvoice()
        }


        bindings.mobileFormTv.addTextChangedListener {
            if(it.isNullOrEmpty()){
                if(bindings.billFormTv.text?.toString().isNullOrEmpty()){
                    val intent = Intent(requireContext(), SalesActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }

            }
        }

        bindings.billFormTv.addTextChangedListener {
            if(it.isNullOrEmpty()){
                if(bindings.mobileFormTv.text?.toString().isNullOrEmpty()) {
                    val intent = Intent(requireContext(), SalesActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
        }

        /**Field Validation Message**/
        viewModel.errorMessage.observe(viewLifecycleOwner, EventObserver {
            showToast(it)
        })
    }

    private fun initAdapter() {
        bindings.invoiceRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = InvoiceAdapter(this)
        bindings.invoiceRecyclerView.adapter = adapter
    }

    private fun setList() = lifecycleScope.launch {
        viewModel.invoiceList().collect { data ->
            data.let {
                adapter.submitData(it)
            }
        }

    }.also {
        showToast("Second")
    }

    private fun searchInvoice(){
        val mobile = bindings.mobileFormTv.text?.trim().toString()
        val billNo = bindings.billFormTv.text?.trim().toString()
        val fromDate = bindings.dateFromTv.text?.trim().toString()
        val toDate = bindings.dateToTv.text?.trim().toString()
        var id = ""

        searchJob?.cancel()
        if(mobile.isNotEmpty()){
            viewModel.getCustomer(mobile)
            viewModel.customer.observe(viewLifecycleOwner, Observer {
                if(it!=null){
                    id = it.id.toString()
                }else{
                    showToast("Customer Not found")
                }

            })
        }


        searchJob = lifecycleScope.launch {
            viewModel.searchList(id,billNo,fromDate,toDate).collect {
                it.let {
                    adapter.submitData(it)
                }
            }
        }
    }

    /**Date Picker**/
    private fun datePickerSetup() {
        val fromBuilder : MaterialDatePicker.Builder<*> = MaterialDatePicker.Builder.datePicker()
        val toBuilder : MaterialDatePicker.Builder<*> = MaterialDatePicker.Builder.datePicker()

        val fromDatePick = fromBuilder.build()
        val toDatePick = toBuilder.build()

        bindings.fromDateIcon.setOnClickListener{
            fromDatePick.show(
                requireActivity().supportFragmentManager,
                "From Date Picker"
            )
        }

        bindings.toDatePicker.setOnClickListener{
            toDatePick.show(
                requireActivity().supportFragmentManager,
                "To Date Picker"
            )
        }

        fromDatePick.addOnPositiveButtonClickListener { selection ->
            val targetFormat = SimpleDateFormat("yyyy-MM-dd")
            val newDate: String = targetFormat.format(selection)
            bindings.dateFromTv.text = newDate
        }

        toDatePick.addOnPositiveButtonClickListener { selection ->
            val targetFormat = SimpleDateFormat("yyyy-MM-dd")
            val newDate: String = targetFormat.format(selection)
            bindings.dateToTv.text = newDate
        }
    }

    override fun editInvoice(invoice : InvoiceData) {
//        salesViewModel.invoiceUpdate(invoice)
        salesViewModel.setInvoiceDetails(invoice)
        salesViewModel.newProductSelect = false
        var id = invoice.id
        val invoiceDetailsDirection = DailySalesFragmentDirections.actionDailySalesFragmentToInvoiceDetailsFragment(id)
        findNavController().navigate(invoiceDetailsDirection)
    }

    override fun deleteInvoice(invoice: InvoiceData, position: Int) {
        val dialog = MaterialDialog(requireContext())
            .title(R.string.invoice_delete_title)
            .message(R.string.invoice_delete_message)
        dialog.show()

        dialog.negativeButton {
            dialog.dismiss()
        }

        dialog.positiveButton {
            viewModel.deleteInvoice(invoice).observe(viewLifecycleOwner, Observer {
                it?.let {resource->
                    when(resource.status){

                        Status.SUCCESS->{
                            loader.dismiss()
                            when(it.data?.body()?.success){

                                200->{
                                    viewModel.deleteInvoiceFromList(invoice)
                                    adapter.notifyDataSetChanged()
                                    adapter.notifyItemChanged(position)
                                    viewModel.getInvoiceTotal()
                                    setList()

                                    salesViewModel.deleteInvoice(invoice,position)
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

    override fun showInformation(invoice: Invoice) {
//        viewModel.invoiceEdit(invoice)
        adapter.notifyDataSetChanged()

        dialog = MaterialDialog(this.requireContext())


        val dialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.invoice_dialog_show,
            null,
            false
        ) as InvoiceDialogShowBinding

        dialog.setContentView(dialogBinding.root)


        dialog.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        salesViewModel.getProductList()

//        viewModel.product.observe(this.viewLifecycleOwner, Observer {
//            dialogAdapter = InvoiceDetailsAdapter(it)
//            dialogBinding.recyclerView.adapter = dialogAdapter
//            dialog.invoiceTotalItem.setText(it.size.toString())
//            dialogAdapter.notifyDataSetChanged()
//        })

        salesViewModel.totalQuantity.observe(this.viewLifecycleOwner, Observer {
            dialog.totalQuantity.setText(it.toString())
        })

        salesViewModel.selectedInvoice.observe(this.viewLifecycleOwner, Observer {
            it?.let {
                dialog.totalBill.setText(it.totalAmount.toString())
                dialog.invoieDue.setText(it.dueAmount.toString())
                dialog.invoicePartialPaid.setText(it.partialPayment.toString())
                dialog.invoiceDate.text = it.createdAt.toString()
                dialog.billNo.text = it.billNo.toString()
            }

        })

        salesViewModel.invoiceCustomerDetails.observe(this.viewLifecycleOwner, Observer {
            it?.let {
                dialog.customerName.text = it.customerName
                dialog.customerMobile.text = it.customerMobile
            }

        })

        salesViewModel.customerInvoiceListTotalDue.observe(this.viewLifecycleOwner, Observer {
            dialogBinding.oldDue.setText(it.toString())
        })


        dialog.toEditProduct.setOnClickListener {
            findNavController().navigate(R.id.productSelectionFragment)
            dialog.dismiss()
        }

        dialog.closeDialog.setOnClickListener {
            salesViewModel.clearProductList()
            adapter.notifyDataSetChanged()
            dialog.dismiss()
        }

        dialog.cancelOnTouchOutside(false)

        dialog.show()

    }

    override fun showInvoiceDetails(invoice: InvoiceData) {

        adapter.notifyDataSetChanged()
        dialog = MaterialDialog(this.requireContext())


        val dialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.invoice_dialog_show,
            null,
            false
        ) as InvoiceDialogShowBinding


        dialog.setContentView(dialogBinding.root)
        dialogBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        dialogBinding.viewModel = viewModel
        viewModel.getSpecificInvoiceDetails(invoice.id).observe(viewLifecycleOwner,
            Observer {
                it?.let {resource ->
                    when(resource.status){
                        Status.SUCCESS->{
                            loader.dismiss()
                            when(it.data?.body()?.success){
                                200->{

                                    dialogAdapter = InvoiceDetailsAdapter(it.data.body()?.data?.sales?: listOf())
                                    dialogBinding.recyclerView.adapter = dialogAdapter
                                    dialogAdapter.notifyDataSetChanged()

                                    salesViewModel.setApiInvoice(it.data.body())
                                    salesViewModel.setApiCustomer()
                                    salesViewModel.setProductForEditInvoice()

                                    dialogBinding.customerName.text = it.data.body()?.data?.customer?.name
                                    dialogBinding.customerMobile.text = it.data.body()?.data?.customer?.mobile
                                    dialogBinding.billNo.text = it.data.body()?.data?.invoice?.id.toString()
                                    dialogBinding.invoiceDate.text = it.data.body()?.data?.invoice?.date.toString()
                                    dialogBinding.oldDue.setText(it.data.body()?.data?.invoice?.due_amount.toString())
                                    dialogBinding.totalBill.setText(it.data.body()?.data?.invoice?.total_amount.toString())
                                    dialogBinding.invoiceTotalItem.setText(it.data.body()?.data?.no_of_product.toString())
                                    dialogBinding.totalQuantity.setText(it.data.body()?.data?.no_of_quantity.toString())
                                    dialogBinding.invoicePartialPaid.setText(it.data.body()?.data?.invoice?.partial_payment.toString())

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
            })

        dialogBinding.toEditProduct.setOnClickListener {
            try{
//                salesViewModel.invoiceUpdate(invoice)
//                salesViewModel.invoiceEdit(invoice)
                salesViewModel.setEditFlag()
                salesViewModel.setApiCustomer()
                findNavController().navigate(R.id.productSelectionFragment)
                dialog.dismiss()
            }catch (exception : Exception){
                showToast(exception.toString())
            }

        }

        dialog.closeDialog.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }


}
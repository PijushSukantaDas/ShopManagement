package com.example.shopmanagement.ui.purchase.list

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
import com.google.android.material.datepicker.MaterialDatePicker
import com.example.shopmanagement.BaseFragment
import com.example.shopmanagement.R
import com.example.shopmanagement.data.models.PurchaseInvoice
import com.example.shopmanagement.data.models.api.purchase.PurchaseData
import com.example.shopmanagement.databinding.FragmentPurchaseBinding
import com.example.shopmanagement.databinding.PurchaseInvoiceDialogShowBinding
import com.example.shopmanagement.ui.activity.MainActivity
import com.example.shopmanagement.ui.activity.PurchaseActivity
import com.example.shopmanagement.ui.purchase.IPurchaseListener
import com.example.shopmanagement.ui.purchase.PurchaseAdapter
import com.example.shopmanagement.ui.purchase.PurchaseViewModel
import com.example.shopmanagement.ui.utils.EventObserver
import com.example.shopmanagement.ui.utils.Status
import com.example.shopmanagement.ui.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import dmax.dialog.SpotsDialog
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat


@AndroidEntryPoint
class PurchaseFragment : BaseFragment(), IPurchaseListener {

    lateinit var binding : FragmentPurchaseBinding
    private val purchaseViewModel : PurchaseViewModel by activityViewModels()
    private val viewModel : PurchaseHistoryViewModel by viewModels()
    lateinit var adapter : PurchaseAdapter
    lateinit var  loader : AlertDialog
    private var searchJob: Job? = null

    private lateinit var dialog: MaterialDialog
    lateinit var dialogAdapter : PurchaseDetailsAdapter

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
        binding = FragmentPurchaseBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = purchaseViewModel
        binding.purchaseHistoryViewModel = viewModel

        val toolbar = activity?.findViewById<TextView>(R.id.titleToolbar)
        toolbar?.setText("Purchase")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loader = SpotsDialog.Builder().setContext(requireContext()).build()
        purchaseViewModel.clearData()

        purchaseViewModel.getPurchaseInvoiceList()

        initAdapter()
        setPurchaseList()
        datePickerSetup()

        binding.dateFromTv.text = todayDate()
        binding.dateToTv.text = lastDate()


        purchaseViewModel.navigate.observe(this.viewLifecycleOwner, EventObserver {
            if(it){
                purchaseViewModel.fromEditScreen = false
                findNavController().navigate(R.id.action_purchaseFragment_to_supplierSelectFragment)
            }

        })

        viewModel.totalPurchase.observe(viewLifecycleOwner, Observer {
            binding.totalAmountTv.text = it
        })

        binding.searchBtn.setOnClickListener {
            searchPurchase()
        }


        binding.mobileFormTv.addTextChangedListener {
            if(it.isNullOrEmpty()){
                if(binding.billFormTv.text?.toString().isNullOrEmpty()){
                    val intent = Intent(requireContext(), PurchaseActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }

            }
        }

        binding.billFormTv.addTextChangedListener {
            if(it.isNullOrEmpty()){
                if(binding.mobileFormTv.text?.toString().isNullOrEmpty()) {
                    val intent = Intent(requireContext(), PurchaseActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
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

    private fun setPurchaseList() {
        lifecycleScope.launch {
            viewModel.purchaseList().collect {
                it.let { adapter.submitData(it) }
            }
        }
    }

    private fun searchPurchase(){
        searchJob?.cancel()
        val mobile = binding.mobileFormTv.text?.trim().toString()
        val purchaseInvoiceNo = binding.billFormTv.text?.trim().toString()
        val fromDate = binding.dateFromTv.text?.trim().toString()
        val toDate = binding.dateToTv.text?.trim().toString()
        var id = ""

        if(mobile.isNotEmpty()){
            viewModel.getSupplier(mobile)
            viewModel.supplier.observe(viewLifecycleOwner, Observer {
                id = it.id.toString()
            })
        }

        searchJob = lifecycleScope.launch {
            viewModel.searchList(id,purchaseInvoiceNo,fromDate,toDate).collect {
                it.let { adapter.submitData(it) }
            }
        }
    }

    private fun initAdapter() {
        binding.purchaseListRv.layoutManager = LinearLayoutManager(requireContext())
        adapter = PurchaseAdapter(this)
        binding.purchaseListRv.adapter = adapter
    }


    override fun editInvoice(purchaseData : PurchaseData) {
        purchaseViewModel.purchaseInvoiceEdit(purchaseData)

        purchaseViewModel.setSupplierPurchaseInfo(purchaseData)

        val purchaseDetailsDirection = PurchaseFragmentDirections.actionPurchaseFragmentToPurchaseDetailsFragment(purchaseData.purchase_invoice)
        findNavController().navigate(purchaseDetailsDirection)
    }

    override fun deleteInvoice(invoice: PurchaseData, position: Int) {
        val dialog = MaterialDialog(requireContext())
            .title(R.string.purchase_delete_title)
            .message(R.string.purchase_delete_message)
        dialog.show()

        dialog.negativeButton {
            dialog.dismiss()
        }

        dialog.positiveButton {
            viewModel.deletePurchase(invoice).observe(viewLifecycleOwner, Observer {
                it?.let {resource->
                    when(resource.status){

                        Status.SUCCESS->{
                            loader.dismiss()

                            when(it.data?.body()?.success){
                                200->{
                                    viewModel.deleteFromLocalPurchaseList(invoice)
                                    adapter.notifyDataSetChanged()
                                    adapter.notifyItemChanged(position)
                                    viewModel.totalPurchase()
                                    setPurchaseList()
                                    purchaseViewModel.deletePurchaseInvoice(invoice,position)

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


    override fun showInformation(invoice: PurchaseInvoice) {
        purchaseViewModel.clearData()
//        dialogAdapter = InvoiceDetailsAdapter(listOf())

        dialog = MaterialDialog(requireContext())

        val dialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.purchase_invoice_dialog_show,
            null,
            false
        ) as PurchaseInvoiceDialogShowBinding

        dialog.setContentView(dialogBinding.root)

//        viewModel.purchaseInvoiceEdit(invoice)
        dialogAdapter.notifyDataSetChanged()

//        dialogBinding.viewModel = viewModel

        dialogBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        dialogBinding.recyclerView.adapter = dialogAdapter


        purchaseViewModel.totalQuantity.observe(this.viewLifecycleOwner, Observer {
            dialogBinding.purchaseTotalQuantity.setText(it.toString())
        })

        purchaseViewModel.purchaseInvoiceDetails.observe(this.viewLifecycleOwner, Observer {
            it?.let {
                dialogBinding.purchaseTotalBill.setText(it.totalAmount.toString())
                dialogBinding.purchaseDate.text = it.createdAt.toString()
                dialogBinding.purchaseBillNo.text = it.billNo.toString()
            }

        })

        purchaseViewModel.supplier.observe(this.viewLifecycleOwner, Observer {
            it?.let {
                dialogBinding.supplierName.text = it.contactPerson
            }

        })



        dialog.toEditProduct.setOnClickListener {
            purchaseViewModel.fromEditScreen = true
            findNavController().navigate(R.id.productSelectFragment)
            dialog.dismiss()
        }

        dialog.closeDialog.setOnClickListener {
            purchaseViewModel.clearData()
            dialog.dismiss()
        }

        dialog.cancelOnTouchOutside(false)

        dialog.show()

    }


    /**Click On Purchase and Showing Data From Api Call of Specific Purchase Details**/
    override fun showPurchaseDetails(purchaseData: PurchaseData) {
        dialog = MaterialDialog(this.requireContext())
        purchaseViewModel.purchaseInvoiceEdit(purchaseData)

        dialog = MaterialDialog(requireContext())

        val dialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.purchase_invoice_dialog_show,
            null,
            false
        ) as PurchaseInvoiceDialogShowBinding

        dialog.setContentView(dialogBinding.root)
        dialogBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        dialogBinding.viewModel = viewModel

        viewModel.getSpecificPurchaseDetails(purchaseData.purchase_invoice).observe(viewLifecycleOwner,
            Observer {
                it?.let {resource ->
                    when(resource.status){
                        Status.SUCCESS->{
                            loader.dismiss()
                            when(it.data?.body()?.success){
                                200->{

                                    dialogAdapter = PurchaseDetailsAdapter(it.data.body()?.data?.purchases?: listOf())
                                    dialogBinding.recyclerView.adapter = dialogAdapter
                                    dialogAdapter.notifyDataSetChanged()

                                    purchaseViewModel.setApiInvoice(it.data.body())

                                    dialogBinding.supplierName.text = purchaseData.supplier_name
                                    dialogBinding.purchaseBillNo.text = purchaseData.purchase_invoice.toString()
                                    dialogBinding.purchaseDate.text = purchaseData.purchase_date
                                    dialogBinding.purchaseTotalQuantity.setText(purchaseData.qty.toString())
                                    dialogBinding.purchaseTotalProduct.setText(purchaseData.product_count.toString())
                                    dialogBinding.purchaseTotalBill.setText(purchaseData.total.toString())

                                    dialog.show()
                                }
                                else->{
                                    showToast(it.data?.body()?.msg?:"")
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
            purchaseViewModel.setEditFlag()
            findNavController().navigate(R.id.productSelectFragment)
            dialog.dismiss()
        }

        dialog.closeDialog.setOnClickListener {
            purchaseViewModel.clearData()
            dialog.dismiss()
        }

        dialog.show()
    }


}
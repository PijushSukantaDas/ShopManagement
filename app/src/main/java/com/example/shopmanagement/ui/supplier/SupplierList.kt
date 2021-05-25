package com.example.shopmanagement.ui.supplier

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.example.shopmanagement.R
import com.example.shopmanagement.data.models.Supplier
import com.example.shopmanagement.databinding.FragmentSupplierListBinding
import com.example.shopmanagement.ui.activity.MainActivity
import com.example.shopmanagement.ui.activity.SupplierActivity
import com.example.shopmanagement.ui.utils.EventObserver
import com.example.shopmanagement.ui.utils.Status
import com.example.shopmanagement.ui.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import dmax.dialog.SpotsDialog

@AndroidEntryPoint
class SupplierList : Fragment(),ISupplierListener {

    lateinit var binding : FragmentSupplierListBinding
    private val viewModel : SupplierListViewModel by viewModels()
    private val supplierViewModel : SupplierViewModel by activityViewModels()
    lateinit var adapter : SupplierListAdapter
    lateinit var  loader : AlertDialog

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
        binding = FragmentSupplierListBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val toolbar = activity?.findViewById<TextView>(R.id.titleToolbar)
        toolbar?.setText("Suppliers")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loader = SpotsDialog.Builder().setContext(requireContext()).build()

        binding.createSupplierBtn.setOnClickListener {
            supplierViewModel.editSupplierFlag = false
            supplierViewModel.clearSupplierData()
            findNavController().navigate(R.id.action_supplierList_to_createSupplier)
        }
        val supplier = viewModel.supplierList
        binding.supplierListRv.layoutManager = LinearLayoutManager(requireContext())

        adapter = SupplierListAdapter(listOf(),this)
        binding.supplierListRv.adapter = adapter

        supplier.observe(viewLifecycleOwner, Observer {
            if(it != null){
                adapter.supplierList = it
                adapter.notifyDataSetChanged()
            }

        })

        viewModel.errorMessage.observe(viewLifecycleOwner, EventObserver {
            showToast(it)
        })

        binding.searchBtn.setOnClickListener {
            if(viewModel.searchValidation()){
                viewModel.searchSupplier().observe(viewLifecycleOwner, Observer {
                    it?.let {resource->
                        when(resource.status){
                            Status.SUCCESS->{
                                val body = it.data?.body()
                                body?.let {
                                    when(body.success){
                                        200->{
                                            loader.dismiss()
                                            if(body.data.data.isEmpty()){
                                                adapter.supplierList = listOf()
                                                adapter.notifyDataSetChanged()
                                                showToast("Supplier Not Found")
                                            }else{
                                                viewModel.searchSupplierFromLocal(body.data.data[0].id)
                                            }

                                        }
                                        else->{
                                            showToast("Supplier Not Found")
                                        }
                                    }
                                }
                            }
                            Status.LOADING->{
                                loader.show()
                            }
                            Status.ERROR->{
                                loader.dismiss()
                                showToast(it.message.toString())
                            }
                        }

                    }
                })
            }
        }

        viewModel.searchedSupplierList.observe(viewLifecycleOwner, Observer {
            adapter = SupplierListAdapter(it,this)
            binding.supplierListRv.adapter = adapter
            adapter.notifyDataSetChanged()
        })

        binding.dateFromTv.addTextChangedListener {
            if(it.isNullOrEmpty()){
                val intent = Intent(
                    requireContext(),
                    SupplierActivity::class.java
                )
                startActivity(intent)
                requireActivity().finish()
            }
        }

        binding.mobileFormTv.addTextChangedListener {
            if(it.isNullOrEmpty()){
                val intent = Intent(
                    requireContext(),
                    SupplierActivity::class.java
                )
                startActivity(intent)
                requireActivity().finish()
            }
        }

    }


    override fun navigateToSupplierDetails(supplier: Supplier) {
        val directions = SupplierListDirections.actionSupplierListToSupplierDetails(supplier)
        findNavController().navigate(directions)
    }

    override fun editSupplier(supplier: Supplier) {
        supplierViewModel.editSupplierFlag = true
        supplierViewModel.setSupplierInfo(supplier)
        findNavController().navigate(R.id.action_supplierList_to_createSupplier)
    }

    override fun deleteSupplier(supplier: Supplier) {
        val dialog = MaterialDialog(requireContext())
            .title(R.string.supplier_delete_title)
            .message(R.string.supplier_delete_message)

        dialog.show()

        dialog.negativeButton {
            dialog.dismiss()
        }

        dialog.positiveButton {
            viewModel.deleteSupplier(supplier).observe(viewLifecycleOwner, Observer {
                it?.let {resource ->
                    when(resource.status){
                        Status.SUCCESS->{
                            viewModel.deleteLocalSupplier(supplier)
                            showToast("Supplier Deleted")
                        }
                        Status.LOADING->{
                            showToast("Loading")
                        }
                        Status.ERROR->{
                            showToast("${it.message}")
                        }
                    }
                }
            })

            adapter.notifyDataSetChanged()
            dialog.dismiss()
        }
    }

}
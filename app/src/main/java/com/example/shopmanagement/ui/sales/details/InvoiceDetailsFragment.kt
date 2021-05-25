package com.example.shopmanagement.ui.sales.details

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopmanagement.BaseFragment
import com.example.shopmanagement.databinding.FragmentInvoiceDetailsBinding
import com.example.shopmanagement.ui.sales.SelectCustomerViewModel
import com.example.shopmanagement.ui.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import dmax.dialog.SpotsDialog

@AndroidEntryPoint
class InvoiceDetailsFragment : BaseFragment() {

    lateinit var binding : FragmentInvoiceDetailsBinding
    private val viewModel : InvoiceDetailsViewModel by viewModels()
    private val salesViewModel : SelectCustomerViewModel by activityViewModels()
    lateinit var adapter : InvoiceDetailsAdapter
    lateinit var  loader : AlertDialog
    private val args : InvoiceDetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentInvoiceDetailsBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loader = SpotsDialog.Builder().setContext(requireContext()).build()

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = InvoiceDetailsAdapter(listOf())
        binding.recyclerView.adapter = adapter


        salesViewModel.product.observe(this.viewLifecycleOwner, Observer {
                adapter = InvoiceDetailsAdapter(listOf())
                binding.recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
        })


        viewModel.getInvoiceDetails(args.invoiceId).observe(viewLifecycleOwner,Observer{
            it?.let {resource ->
                when(resource.status){
                    Status.LOADING->{
                        loader.show()
                    }
                    Status.SUCCESS->{
                        loader.dismiss()
                        val responseBody = it.data?.body()
                        responseBody?.let {invoice->
                            adapter = InvoiceDetailsAdapter(it.data.body()?.data?.sales?: listOf())
                            binding.recyclerView.adapter = adapter
                            adapter.notifyDataSetChanged()
                            binding.invoice = invoice
                            binding.invoiceDue.setText("${invoice.data.invoice.total_amount-invoice.data.invoice.partial_payment}")
                        }
                    }
                    Status.ERROR->{
                        loader.dismiss()
                    }
                }
            }
        })


//        binding.editProductBtn.setOnClickListener {
//            val invoice = salesViewModel.invoiceDetails.value
//            invoice?.let {
//                salesViewModel.invoiceUpdate(it)
//                salesViewModel.invoiceEdit(it)
//            }
//            findNavController().navigate(R.id.action_invoiceDetailsFragment_to_productSelectionFragment)
//        }

    }






}
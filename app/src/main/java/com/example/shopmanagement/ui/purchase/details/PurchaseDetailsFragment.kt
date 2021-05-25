package com.example.shopmanagement.ui.purchase.details


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
import com.example.shopmanagement.databinding.FragmentPurchaseDetailsBinding
import com.example.shopmanagement.ui.purchase.PurchaseViewModel
import com.example.shopmanagement.ui.purchase.list.PurchaseDetailsAdapter
import com.example.shopmanagement.ui.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import dmax.dialog.SpotsDialog

@AndroidEntryPoint
class PurchaseDetailsFragment : BaseFragment() {
    lateinit var binding : FragmentPurchaseDetailsBinding
    private val viewModel : PurchaseDetailsViewModel by viewModels()
    private val purchaseViewModel : PurchaseViewModel by activityViewModels()
    private val args : PurchaseDetailsFragmentArgs by navArgs()
    lateinit var  loader : AlertDialog

    lateinit var adapter : PurchaseDetailsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPurchaseDetailsBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loader = SpotsDialog.Builder().setContext(requireContext()).build()

        val purchaseData = purchaseViewModel.purchaseData.value

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = PurchaseDetailsAdapter(listOf())
        binding.recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

        viewModel.getPurchaseDetails(args.purchaseId).observe(viewLifecycleOwner, Observer {
            it?.let {resource ->
                when(resource.status){
                    Status.SUCCESS->{
                        loader.dismiss()
                        val responseData = it.data?.body()
                        adapter = PurchaseDetailsAdapter(responseData?.data?.purchases?: listOf())
                        binding.recyclerView.adapter = adapter
                        adapter.notifyDataSetChanged()

                        responseData?.let {purchase->
                            binding.purchase = purchase
                            purchaseData?.let {
                                binding.supplierName.text = purchaseData.supplier_name

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

    }

}
package com.example.shopmanagement.ui.product.details

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopmanagement.BaseFragment
import com.example.shopmanagement.R
import com.example.shopmanagement.databinding.FragmentProductDetailsBinding
import com.example.shopmanagement.ui.product.ProductViewModel
import com.example.shopmanagement.ui.utils.Status
import com.example.shopmanagement.ui.utils.showToast
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import dmax.dialog.SpotsDialog
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ProductDetails : BaseFragment(){

    private val viewModel : ProductDetailsViewModel by viewModels()
    private val productViewModel : ProductViewModel by activityViewModels()
    private val args : ProductDetailsArgs by navArgs()
    lateinit var binding: FragmentProductDetailsBinding
    lateinit var adapter : ProductDetailsAdapter
    lateinit var  loader : AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.product = args.product

        val toolbar = activity?.findViewById<TextView>(R.id.titleToolbar)
        toolbar?.setText("Product Details")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.productId = args.product?.id ?: 0
        loader = SpotsDialog.Builder().setContext(requireContext()).build()
        datePickerSetup()

        binding.productSalesRv.layoutManager = LinearLayoutManager(requireContext())
        adapter = ProductDetailsAdapter(listOf())
        binding.productSalesRv.adapter = adapter

        viewModel.getProductDetails(args.product?.id ?: 0).observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        loader.dismiss()
                        val body = it.data?.body()
                        when (body?.success) {
                            200 -> {

                                adapter = ProductDetailsAdapter(
                                    it.data.body()?.data?.res ?: listOf()
                                )
                                binding.productSalesRv.adapter = adapter
                                adapter.notifyDataSetChanged()

                                binding.productName.text = args.product?.name
                                binding.productPrice.text = args.product?.sellingPrice.toString()
                                binding.purchaseQuantity.text =
                                    viewModel.getTotalPurchaseQuantity().toString()
                                binding.salesQuantity.text =
                                    it.data.body()?.data?.total_sale_quantity

                                binding.purchaseQuantityTillDate.text =
                                    it.data.body()?.data?.lastYearsTotalQuantity.toString()
                                binding.purchaseTotalTillDate.text =
                                    it.data.body()?.data?.lastYearsPurchaseTotal.toString()
                                binding.purchaseRtvQuantityTillDate.text =
                                    it.data.body()?.data?.lastYearsPurchaseReturnQty.toString()
                                binding.purchaseRtvTotalTillDate.text =
                                    it.data.body()?.data?.lastYearsPurchaseReturnTotal.toString()
                                binding.saleQuantityTillDate.text =
                                    it.data.body()?.data?.lastYearsTotalSaleQuantity.toString()
                                binding.saleTotalTillDate.text =
                                    it.data.body()?.data?.lastYearsSaleTotal.toString()
                                binding.saleRtvQuantityTillDate.text =
                                    it.data.body()?.data?.lastYearsSaleReturnTotalQty.toString()
                                binding.saleRtvTotalTillDate.text =
                                    it.data.body()?.data?.lastYearsSaleReturnTotal.toString()


                                body.let { body ->
                                    val date = body.data.res[body.data.res.size-1].date.substring(0,10)
                                    binding.dateFromTv.text = body.data.res[0].date.substring(0,10)
                                    binding.dateToTv.text = body.data.res[body.data.res.size-1].date.substring(0,10)
                                    binding.datePurchase.text = date
                                    binding.datePurchaseReturn.text = date
                                    binding.dateSale.text = date
                                    binding.dateSaleRtv.text = date
                                }


                                val currentQuantity = viewModel.getTotalQuantity().toString()
                                binding.totalSalesPrice.text = currentQuantity
                                binding.currentQuantity.text = currentQuantity
                            }
                            201 -> {
                                loader.dismiss()
                                showToast("Phone Number Already Used")
                            }
                            else -> {
                                loader.dismiss()
                                showToast("${it.data?.body()}")
                            }
                        }
                    }
                    Status.ERROR -> {
                        loader.dismiss()
                        showToast("${it.message}")
                    }
                    Status.LOADING -> {
                        loader.show()
                    }
                }
            }
        })

        binding.searchBtn.setOnClickListener {
            viewModel.productDateSearch(args.product?.id?:0,binding.dateFromTv.text.toString(),binding.dateToTv.text.toString())
                .observe(viewLifecycleOwner, Observer {
                    it?.let { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                loader.dismiss()
                                val body = it.data?.body()
                                when (body?.success) {
                                    200 -> {

                                        adapter = ProductDetailsAdapter(
                                            it.data.body()?.data?.res ?: listOf()
                                        )
                                        binding.productSalesRv.adapter = adapter
                                        adapter.notifyDataSetChanged()

                                        binding.productName.text = args.product?.name
                                        binding.productPrice.text = args.product?.sellingPrice.toString()
                                        binding.purchaseQuantity.text =
                                            viewModel.getTotalPurchaseQuantity().toString()
                                        binding.salesQuantity.text =
                                            it.data.body()?.data?.total_sale_quantity

                                        binding.purchaseQuantityTillDate.text =
                                            it.data.body()?.data?.lastYearsTotalQuantity.toString()
                                        binding.purchaseTotalTillDate.text =
                                            it.data.body()?.data?.lastYearsPurchaseTotal.toString()
                                        binding.purchaseRtvQuantityTillDate.text =
                                            it.data.body()?.data?.lastYearsPurchaseReturnQty.toString()
                                        binding.purchaseRtvTotalTillDate.text =
                                            it.data.body()?.data?.lastYearsPurchaseReturnTotal.toString()
                                        binding.saleQuantityTillDate.text =
                                            it.data.body()?.data?.lastYearsTotalSaleQuantity.toString()
                                        binding.saleTotalTillDate.text =
                                            it.data.body()?.data?.lastYearsSaleTotal.toString()
                                        binding.saleRtvQuantityTillDate.text =
                                            it.data.body()?.data?.lastYearsSaleReturnTotalQty.toString()
                                        binding.saleRtvTotalTillDate.text =
                                            it.data.body()?.data?.lastYearsSaleReturnTotal.toString()


                                        body.let { body ->
                                            val date = body.data.res[body.data.res.size-1].date.substring(0,10)
                                            binding.dateFromTv.text = body.data.res[0].date.substring(0,10)
                                            binding.dateToTv.text = body.data.res[body.data.res.size-1].date.substring(0,10)
                                            binding.datePurchase.text = date
                                            binding.datePurchaseReturn.text = date
                                            binding.dateSale.text = date
                                            binding.dateSaleRtv.text = date
                                        }


                                        val currentQuantity = viewModel.getTotalQuantity().toString()
                                        binding.totalSalesPrice.text = currentQuantity
                                        binding.currentQuantity.text = currentQuantity
                                    }
                                    201 -> {
                                        loader.dismiss()
                                        showToast("Phone Number Already Used")
                                    }
                                    else -> {
                                        loader.dismiss()
                                        showToast("${it.data?.body()}")
                                    }
                                }
                            }
                            Status.ERROR -> {
                                loader.dismiss()
                                showToast("${it.message}")
                            }
                            Status.LOADING -> {
                                loader.show()
                            }
                        }
                    }
                })
        }

    }

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

}
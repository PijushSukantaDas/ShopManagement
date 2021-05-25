package com.example.shopmanagement.ui.product

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.example.shopmanagement.BaseFragment
import com.example.shopmanagement.R
import com.example.shopmanagement.data.models.Product
import com.example.shopmanagement.databinding.FragmentProductListBinding
import com.example.shopmanagement.ui.activity.MainActivity
import com.example.shopmanagement.ui.activity.ProductActivity
import com.example.shopmanagement.ui.utils.Status
import com.example.shopmanagement.ui.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import dmax.dialog.SpotsDialog

@AndroidEntryPoint
class ProductListFragment : BaseFragment(),IProductListener {
    private val viewModel: ProductListViewModel by viewModels()
    private val productViewModel : ProductViewModel by activityViewModels()
    lateinit var binding: FragmentProductListBinding
    lateinit var adapter: ProductListAdapter
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
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_product_list, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val toolbar = activity?.findViewById<TextView>(R.id.titleToolbar)
        toolbar?.setText("Products")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loader = SpotsDialog.Builder().setContext(requireContext()).build()

        binding.productListRv.layoutManager = LinearLayoutManager(requireContext())

        adapter = ProductListAdapter(listOf(),this)
        binding.productListRv.adapter = adapter


        binding.productCreateBtn.setOnClickListener {
            productViewModel.clearEditProductData()
            productViewModel.alertQuantityLiveData.value = "0"
            productViewModel.editProductFlag = false
            findNavController().navigate(R.id.action_productListFragment_to_createProductFragment)
        }

        viewModel.productList.observe(viewLifecycleOwner, Observer {

            adapter = ProductListAdapter(it,this)
            binding.productListRv.adapter = adapter
            adapter.notifyDataSetChanged()
        })

        binding.searchBtn.setOnClickListener {
            if(binding.productField.text.toString().isNotEmpty()){
                viewModel.searchApiProduct(binding.productField.text.toString()).observe(viewLifecycleOwner, Observer {
                    it?.let {response->
                        when(response.status){
                            Status.SUCCESS->{
                                loader.dismiss()
                                val body = it.data?.body()
                                body?.let {body->
                                    when(body.success){
                                        200->{
                                            if(body.data.data.isEmpty()){
                                                adapter = ProductListAdapter(listOf() ,this)
                                                binding.productListRv.adapter = adapter
                                                adapter.notifyDataSetChanged()
                                                showToast("Product Not Found")
                                            }else{
                                                viewModel.searchProduct(body.data.data[0].id)

                                                viewModel.list.observe(viewLifecycleOwner, Observer {list ->

                                                    adapter = ProductListAdapter(list ,this)
                                                    binding.productListRv.adapter = adapter
                                                    adapter.notifyDataSetChanged()

                                                })
                                            }

                                        }
                                        else->{

                                        }
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
            }else{
                showToast("Search Filed is Empty")
            }

        }

        binding.productField.addTextChangedListener {
            if(it.isNullOrEmpty()){
                val intent = Intent(
                    requireContext(),
                    ProductActivity::class.java
                )
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }

    override fun navigateToDetails(product: Product) {
        val direction = ProductListFragmentDirections.actionProductListFragmentToProductDetails(product)
        findNavController().navigate(direction)

    }

    override fun editProduct(product: Product) {
        productViewModel.editProductFlag = true
        productViewModel.editProductInfo(product)
        findNavController().navigate(R.id.action_productListFragment_to_createProductFragment)
    }

    override fun deleteProduct(product: Product) {
        val dialog = MaterialDialog(requireContext())
            .title(R.string.customer_delete_title)
            .message(R.string.customer_delete_message)

        dialog.show()

        dialog.negativeButton {
            dialog.dismiss()
        }
        dialog.positiveButton {
            viewModel.deleteProduct(product).observe(viewLifecycleOwner, Observer {
                it?.let {resource ->
                    when(resource.status){
                        Status.SUCCESS->{
                            loader.dismiss()
                            val body = it.data?.body()
                            body?.let {body->
                                when(body.success){
                                    200->{
                                        viewModel.deleteLocalProduct(body.data.id)
                                        adapter.notifyDataSetChanged()
                                        showToast("Product Deleted")
                                    }
                                    else->{
                                        showToast(body.msg)
                                    }
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
}
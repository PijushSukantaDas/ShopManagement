package com.example.shopmanagement.ui.supplier

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.shopmanagement.R
import com.example.shopmanagement.databinding.FragmentCreateSupplierBinding
import com.example.shopmanagement.ui.BaseCreateFragment
import com.example.shopmanagement.ui.activity.SupplierActivity
import com.example.shopmanagement.ui.utils.*
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class CreateSupplier : BaseCreateFragment() {

    lateinit var binding : FragmentCreateSupplierBinding
    val viewModel : SupplierViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateSupplierBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        if(viewModel.editSupplierFlag){
            binding.createSupplierBtn.text = "update supplier"
            val toolbar = activity?.findViewById<TextView>(R.id.titleToolbar)
            toolbar?.setText("Update Supplier")
        }else{
            val toolbar = activity?.findViewById<TextView>(R.id.titleToolbar)
            toolbar?.setText("Create Supplier")
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.errorMessage.observe(viewLifecycleOwner,EventObserver{
            showToast(it)
        })

        viewModel.navigate.observe(viewLifecycleOwner, Observer {
            findNavController().navigateUp()
        })

        viewModel.imagePickerEvent.observe(viewLifecycleOwner, EventObserver {
            showImagePicker()
        })


        /**On Supplier Create Button**/
        binding.createSupplierBtn.setOnClickListener {

            viewModel.createSupplier().observe(viewLifecycleOwner, Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            LoadingDialog(requireContext()).dismissDialog()
                            if(it.data?.body()?.success == 200){
                                viewModel.createLocalCustomer(it.data.body()?.data)
                                val intent = Intent(requireContext(),SupplierActivity::class.java)
                                startActivity(intent)
                                requireActivity().finish()
                            }else{
                                showToast("${it.data?.body()?.msg}")
                            }

                        }
                        Status.ERROR -> {
                            LoadingDialog(requireContext()).dismissDialog()
                            showToast("Error")
                        }
                        Status.LOADING -> {
                            LoadingDialog(requireContext()).showDialog()
                        }
                    }
                }
            })

        }

    }

    override fun getPickerCallback(): (Uri) -> Unit {
        return {
            Timber.d("===> selected image: $it")
            binding.imageView2.loadImage(it)
            viewModel.imageUri = it
        }
    }

}
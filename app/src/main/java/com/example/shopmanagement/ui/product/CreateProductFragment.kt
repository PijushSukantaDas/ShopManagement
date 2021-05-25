package com.example.shopmanagement.ui.product

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import com.example.shopmanagement.R
import com.example.shopmanagement.databinding.FragmentCreateProductBinding
import com.example.shopmanagement.databinding.ScannerDialogBinding
import com.example.shopmanagement.ui.BaseCreateFragment
import com.example.shopmanagement.ui.activity.ProductActivity
import com.example.shopmanagement.ui.utils.*
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import dagger.hilt.android.AndroidEntryPoint
import dmax.dialog.SpotsDialog
import timber.log.Timber

private const val CAMERA_REQUEST_CODE = 101

@AndroidEntryPoint
class CreateProductFragment : BaseCreateFragment() {
    val viewModel : ProductViewModel by activityViewModels()
    lateinit var binding: FragmentCreateProductBinding
    private lateinit var dialog: MaterialDialog
    lateinit var codeScanner : CodeScanner
    lateinit var  loader : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateProductBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val toolbar = activity?.findViewById<TextView>(R.id.titleToolbar)
        toolbar?.setText("Create Product")

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loader = SpotsDialog.Builder().setContext(requireContext()).build()

        dialog = MaterialDialog(this.requireContext())

        setupPermission()
        scanner()

        if(viewModel.editProductFlag){
            binding.produtCreateOrUpdateBtn.text = "Update Product"
        }

        viewModel.errorMessage.observe(viewLifecycleOwner, EventObserver {
            showToast(it)
        })

        viewModel.imagePickerEvent.observe(viewLifecycleOwner, EventObserver {
            showImagePicker()
        })

        viewModel.navigate.observe(viewLifecycleOwner, EventObserver {
            if (it) {
                val intent = Intent(this.requireContext(), ProductActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        })


        binding.produtCreateOrUpdateBtn.setOnClickListener {

            viewModel.createProduct().observe(viewLifecycleOwner, Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader.dismiss()
                            if(it.data?.body()?.success == 200){
                                viewModel.createLocalProduct(it.data.body()?.data)
                                val intent = Intent(requireContext(), ProductActivity::class.java)
                                startActivity(intent)
                                requireActivity().finish()
                            }else{
                                showToast("""${it.data?.body()?.msg}"""+
                                """${it.data?.body()?.success}""")
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

        binding.scannerBtn.setOnClickListener {
            dialog.show()
            dialog.negativeButton {
                dialog.dismiss()
            }
        }

    }

    private fun scanner() {
        val dialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.scanner_dialog,
            null,
            false
        ) as ScannerDialogBinding

        dialog.setContentView(dialogBinding.root)

        codeScanner = CodeScanner(requireContext(),dialogBinding.scannerView)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                activity?.runOnUiThread {
                    binding.code.setText(it.text)
                    dialog.dismiss()
                }

            }

            errorCallback = ErrorCallback {
                Log.i("Main","Camera Initialization Error : ${it.message}")
            }
        }

        dialogBinding.scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun getPickerCallback(): (Uri) -> Unit {
        return {
            setImage(it)
        }
    }

    private fun setImage(it: Uri) {
        Timber.d("===> selected image: $it")
        binding.chooseFileTv.loadImage(it)
        viewModel.imageUri = it
    }

    private fun setupPermission(){
        val permission = ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.CAMERA
        )
        if(permission != PackageManager.PERMISSION_GRANTED){
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE)
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            CAMERA_REQUEST_CODE -> {
                if(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    showToast("You Need the Camera Permission to able to use the app")
                }else{

                }
            }
        }

    }

}
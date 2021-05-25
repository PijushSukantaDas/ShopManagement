package com.example.shopmanagement.ui.purchase.product

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.example.shopmanagement.BaseFragment
import com.example.shopmanagement.R
import com.example.shopmanagement.data.models.SalesProductModel
import com.example.shopmanagement.databinding.FragmentProductSelectBinding
import com.example.shopmanagement.databinding.ProductSelectedLayoutBinding
import com.example.shopmanagement.databinding.ProductWeightLayoutBinding
import com.example.shopmanagement.databinding.ScannerDialogBinding
import com.example.shopmanagement.ui.activity.PurchaseActivity
import com.example.shopmanagement.ui.purchase.PurchaseViewModel
import com.example.shopmanagement.ui.sales.product.AdapterSelectedProduct
import com.example.shopmanagement.ui.sales.product.IQuantityChangeListener
import com.example.shopmanagement.ui.utils.EventObserver
import com.example.shopmanagement.ui.utils.LoadingDialog
import com.example.shopmanagement.ui.utils.Status
import com.example.shopmanagement.ui.utils.showToast
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import dagger.hilt.android.AndroidEntryPoint

private const val CAMERA_REQUEST_CODE = 101

@AndroidEntryPoint
class ProductSelectFragment : BaseFragment(),IQuantityChangeListener {
    lateinit var binding : FragmentProductSelectBinding
    private val viewModel : PurchaseViewModel by activityViewModels()
    private lateinit var dialog: MaterialDialog
    lateinit var codeScanner : CodeScanner

    lateinit var adapter : AdapterSelectedProduct

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.totalBill.value = 0.0
        viewModel.totalItem.value = 0
        viewModel.totalQuantity.value = 0.0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProductSelectBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog = MaterialDialog(this.requireContext())

        binding.selectedProductRv.layoutManager = LinearLayoutManager(requireContext())
        adapter = AdapterSelectedProduct(arrayListOf(),this)
        binding.selectedProductRv.adapter = adapter

        setupPermission()
        scanner()

        binding.scannerBtn.setOnClickListener {
            scanner()
            dialog.show()
        }

        var adapterProduct = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.select_dialog_item,
            listOf()
        )

        if(viewModel.getEditFlag()){
            viewModel.apiInvoice.observe(viewLifecycleOwner, Observer {
                if(it != null){
                    viewModel.setProductForEditInvoice()
                    viewModel.getProductList()
                }else{
                    showToast("Emply Invoice")
                }
            })
        }

        viewModel.getProductName().observe(this.viewLifecycleOwner, Observer {
            adapterProduct = ArrayAdapter<String>(
                requireContext(),
                android.R.layout.select_dialog_item,
                it
            )

            binding.selectProductField.setAdapter(adapterProduct)
        })

        binding.selectProductField.setOnItemClickListener { parent, view, position, id ->
            val data = adapterProduct.getItem(position)
            viewModel.name = "$data"
            viewModel.validProduct = true
            binding.selectProductField.setText("")
            weightDialog()
        }

        //Setting data to adapter
        viewModel.product.observe(this.viewLifecycleOwner, Observer { list ->
            if(list != null){
                adapter = AdapterSelectedProduct(list,this)
                binding.selectedProductRv.adapter = adapter
                adapter.notifyDataSetChanged()
            }
            binding.shopBtn.setOnClickListener {
                if(list.isNotEmpty()){
                    viewModel.setProduct(list)
                    viewModel.arrayOfProduct()
                    findNavController().navigate(R.id.action_productSelectFragment_to_purchaseConfirmFragment)
                }else{
                    showToast("No Product Selected")
                }
            }
        })


        //Total Bill
        viewModel.totalBill.observe(this.viewLifecycleOwner, Observer {
            viewModel.totalPurchaseBill = it
            binding.totalBill.setText(it.toString())
        })

        //Total Item
        viewModel.totalItem.observe(this.viewLifecycleOwner, Observer {
            binding.totalItem.setText(it.toString())
        })

        //Total Quantity
        viewModel.totalQuantity.observe(this.viewLifecycleOwner, Observer {
            binding.totalQuantity.setText(it.toString())
        })

        viewModel.navigate.observe(viewLifecycleOwner,EventObserver{
            if(it){
                val intent = Intent(requireContext(), PurchaseActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }else{
                showToast("Something Went Wrong")
            }

        })

        binding.invoiceBtn.setOnClickListener {
            viewModel.arrayOfProduct()
            viewModel.confirmClick = false
            viewModel.insertPurchase().observe(viewLifecycleOwner, Observer {
                it?.let {resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            LoadingDialog(requireContext()).dismissDialog()
                            viewModel.clickInvoice(it.data?.body()?.data)
                            val intent = Intent(requireActivity() , PurchaseActivity::class.java)
                            startActivity(intent)
                            showToast("created")
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

    /**Weight Selection Process**/
    private fun weightDialog() {
        var qty = 1.0

        val dialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.product_weight_layout,
            null,
            false
        ) as ProductWeightLayoutBinding

        dialog.setContentView(dialogBinding.root)

        dialogBinding.cancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        var touchedOutside = dialog.cancelOnTouchOutside

        if(touchedOutside){
            addProduct(1.0, false)
        }


        /**Quantity Click**/
        dialogBinding.one.setOnClickListener {
            qty = 1.0
            addProduct(qty,false)
            dialog.dismiss()
        }

        dialogBinding.two.setOnClickListener {
            qty = 2.0
            addProduct(qty, false)
            dialog.dismiss()
        }
        dialogBinding.three.setOnClickListener {
            qty = 3.0
            addProduct(qty, false)
            dialog.dismiss()
        }
        dialogBinding.five.setOnClickListener {
            qty = 5.0
            addProduct(qty, false)
            dialog.dismiss()
        }

        dialogBinding.ten.setOnClickListener {
            qty = 10.0
            addProduct(qty, false)
            dialog.dismiss()
        }

        dialogBinding.twenty.setOnClickListener {
            qty = 20.0
            addProduct(qty, false)
            dialog.dismiss()
        }

        dialogBinding.thirty.setOnClickListener {
            qty = 30.0
            addProduct(qty, false)
            dialog.dismiss()
        }

        dialogBinding.fifty.setOnClickListener {
            qty = 50.0
            addProduct(qty, false)
            dialog.dismiss()
        }

        dialogBinding.hundred.setOnClickListener {
            qty = 100.0
            addProduct(qty, false)
            dialog.dismiss()
        }

        /**Grams click**/

        dialogBinding.fiftyGm.setOnClickListener {
            qty = .05
            addProduct(qty, false)
            dialog.dismiss()
        }

        dialogBinding.hundredGm.setOnClickListener {
            qty = .100
            addProduct(qty, false)
            dialog.dismiss()
        }

        dialogBinding.onwFiftyGm.setOnClickListener {
            qty = .150
            addProduct(qty, false)
            dialog.dismiss()
        }

        dialogBinding.twoHundredGm.setOnClickListener {
            qty = .200
            addProduct(qty, false)
            dialog.dismiss()
        }

        dialogBinding.threeHundredGm.setOnClickListener {
            qty = .300
            addProduct(qty, false)
            dialog.dismiss()
        }

        dialogBinding.twoFiftyGm.setOnClickListener {
            qty = .250
            addProduct(qty, false)
            dialog.dismiss()
        }

        dialogBinding.fiveHundredGm.setOnClickListener {
            qty = .500
            addProduct(qty, false)
            dialog.dismiss()
        }

        dialogBinding.sevenFiftyGm.setOnClickListener {
            qty = .750
            addProduct(qty, false)
            dialog.dismiss()
        }
        dialogBinding.eightHundred.setOnClickListener {
            qty = .800
            addProduct(qty, false)
            dialog.dismiss()
        }


        /**In Kg**/
        dialogBinding.oneKg.setOnClickListener {
            qty = 1.0
            addProduct(qty, false)
            dialog.dismiss()
        }

        dialogBinding.twoKg.setOnClickListener {
            qty = 2.0
            addProduct(qty, false)
            dialog.dismiss()
        }
        dialogBinding.threeKg.setOnClickListener {
            qty = 3.0
            addProduct(qty, false)
            dialog.dismiss()
        }
        dialogBinding.fiveKg.setOnClickListener {
            qty = 5.0
            addProduct(qty, false)
            dialog.dismiss()
        }

        dialogBinding.tenKg.setOnClickListener {
            qty = 10.0
            addProduct(qty, false)
            dialog.dismiss()
        }

        dialogBinding.twentyKg.setOnClickListener {
            qty = 20.0
            addProduct(qty, false)
            dialog.dismiss()
        }

        dialogBinding.thirtyKg.setOnClickListener {
            qty = 30.0
            addProduct(qty, false)
            dialog.dismiss()
        }

        dialogBinding.fiftyKg.setOnClickListener {
            qty = 50.0
            addProduct(qty, false)
            dialog.dismiss()
        }

        dialogBinding.hundredKg.setOnClickListener {
            qty = 100.0
            addProduct(qty, false)
            dialog.dismiss()
        }

        viewModel.getProductDetails().observe(viewLifecycleOwner, Observer {
            if(it !=null){
                dialog.show()
            }
        })
    }

    private fun addProduct(qty: Double, increment : Boolean) {

        viewModel.getProductDetails().observe(this.viewLifecycleOwner, Observer { product ->

            if(product != null){
                viewModel.getProductList()

                addSearchedProductToList(
                    SalesProductModel(
                        product.id,
                        product.name,
                        qty,
                        product.sellingPrice,
                        product.sellingPrice
                    ), increment
                )
            }else{
                dialog.dismiss()
                showToast(getString(R.string.product_empty))
            }

        })
    }

    private fun addSearchedProductToList(product: SalesProductModel, increment: Boolean) {
        viewModel.addProduct(product , increment)
    }

    override fun updateProduct(
        position: Int,
        quantity: Double,
        totalPrice: Double,
        price : Double
    ) {
        viewModel.updateProduct(position,quantity,totalPrice,price)
    }

    override fun incrementQuantity(product : SalesProductModel) {
        viewModel.incrementQuantity(product , true)
        viewModel.totalAmount()
        viewModel.totalItem()
    }

    override fun deleteProduct(product: SalesProductModel) {
        viewModel.delete(product)
        adapter.notifyDataSetChanged()
    }

    override fun showWeightDialog(
        product: SalesProductModel,
        position: Int,
        binding: ProductSelectedLayoutBinding
    ) {
        selectQuantity(position,binding)
    }

    private fun selectQuantity(position: Int, binding: ProductSelectedLayoutBinding) {
        var qty = 1.0

        dialog = MaterialDialog(this.requireContext())

        val dialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.product_weight_layout,
            null,
            false
        ) as ProductWeightLayoutBinding

        dialog.setContentView(dialogBinding.root)

        dialogBinding.cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

        /**Quantity Click**/
        dialogBinding.one.setOnClickListener {
            qty = 1.0
            viewModel.updateProductQuantity(position,qty)
            binding.quantity.editText?.setText(qty.toString())
            dialog.dismiss()
        }

        dialogBinding.two.setOnClickListener {
            qty = 2.0
            viewModel.updateProductQuantity(position,qty)
            binding.quantity.editText?.setText(qty.toString())
            dialog.dismiss()
        }
        dialogBinding.three.setOnClickListener {
            qty = 3.0
            viewModel.updateProductQuantity(position,qty)
            binding.quantity.editText?.setText(qty.toString())
            dialog.dismiss()
        }
        dialogBinding.five.setOnClickListener {
            qty = 5.0
            viewModel.updateProductQuantity(position,qty)
            binding.quantity.editText?.setText(qty.toString())
            dialog.dismiss()
        }

        dialogBinding.ten.setOnClickListener {
            qty = 10.0
            viewModel.updateProductQuantity(position,qty)
            binding.quantity.editText?.setText(qty.toString())
            dialog.dismiss()
        }

        dialogBinding.twenty.setOnClickListener {
            qty = 20.0
            viewModel.updateProductQuantity(position,qty)
            binding.quantity.editText?.setText(qty.toString())
            dialog.dismiss()
        }

        dialogBinding.thirty.setOnClickListener {
            qty = 30.0
            viewModel.updateProductQuantity(position,qty)
            binding.quantity.editText?.setText(qty.toString())
            dialog.dismiss()
        }

        dialogBinding.fifty.setOnClickListener {
            qty = 50.0
            viewModel.updateProductQuantity(position,qty)
            binding.quantity.editText?.setText(qty.toString())
            dialog.dismiss()
        }

        dialogBinding.hundred.setOnClickListener {
            qty = 100.0
            viewModel.updateProductQuantity(position,qty)
            binding.quantity.editText?.setText(qty.toString())
            dialog.dismiss()
        }

        /**Grams click**/
        dialogBinding.fiftyGm.setOnClickListener {
            qty = .05
            viewModel.updateProductQuantity(position,qty)
            binding.quantity.editText?.setText(qty.toString())
            dialog.dismiss()
        }

        dialogBinding.hundredGm.setOnClickListener {
            qty = .100
            viewModel.updateProductQuantity(position,qty)
            binding.quantity.editText?.setText(qty.toString())
            dialog.dismiss()
        }

        dialogBinding.onwFiftyGm.setOnClickListener {
            qty = .150
            viewModel.updateProductQuantity(position,qty)
            binding.quantity.editText?.setText(qty.toString())
            dialog.dismiss()
        }

        dialogBinding.twoHundredGm.setOnClickListener {
            qty = .200
            viewModel.updateProductQuantity(position,qty)
            binding.quantity.editText?.setText(qty.toString())
            dialog.dismiss()
        }

        dialogBinding.threeHundredGm.setOnClickListener {
            qty = .300
            viewModel.updateProductQuantity(position,qty)
            binding.quantity.editText?.setText(qty.toString())
            dialog.dismiss()
        }

        dialogBinding.twoFiftyGm.setOnClickListener {
            qty = .250
            viewModel.updateProductQuantity(position,qty)
            binding.quantity.editText?.setText(qty.toString())
            dialog.dismiss()
        }

        dialogBinding.fiveHundredGm.setOnClickListener {
            qty = .500
            viewModel.updateProductQuantity(position,qty)
            binding.quantity.editText?.setText(qty.toString())
            dialog.dismiss()
        }

        dialogBinding.sevenFiftyGm.setOnClickListener {
            qty = .750
            viewModel.updateProductQuantity(position,qty)
            binding.quantity.editText?.setText(qty.toString())
            dialog.dismiss()
        }
        dialogBinding.eightHundred.setOnClickListener {
            qty = .800
            viewModel.updateProductQuantity(position,qty)
            binding.quantity.editText?.setText(qty.toString())
            dialog.dismiss()
        }


        /**In Kg**/
        dialogBinding.oneKg.setOnClickListener {
            qty = 1.0
            viewModel.updateProductQuantity(position,qty)
            binding.quantity.editText?.setText(qty.toString())
            dialog.dismiss()
        }

        dialogBinding.twoKg.setOnClickListener {
            qty = 2.0
            viewModel.updateProductQuantity(position,qty)
            binding.quantity.editText?.setText(qty.toString())
            dialog.dismiss()
        }
        dialogBinding.threeKg.setOnClickListener {
            qty = 3.0
            viewModel.updateProductQuantity(position,qty)
            binding.quantity.editText?.setText(qty.toString())
            dialog.dismiss()
        }
        dialogBinding.fiveKg.setOnClickListener {
            qty = 5.0
            viewModel.updateProductQuantity(position,qty)
            binding.quantity.editText?.setText(qty.toString())
            dialog.dismiss()
        }

        dialogBinding.tenKg.setOnClickListener {
            qty = 10.0
            viewModel.updateProductQuantity(position,qty)
            binding.quantity.editText?.setText(qty.toString())
            dialog.dismiss()
        }

        dialogBinding.twentyKg.setOnClickListener {
            qty = 20.0
            viewModel.updateProductQuantity(position,qty)
            binding.quantity.editText?.setText(qty.toString())
            dialog.dismiss()
        }

        dialogBinding.thirtyKg.setOnClickListener {
            qty = 30.0
            viewModel.updateProductQuantity(position,qty)
            binding.quantity.editText?.setText(qty.toString())
            dialog.dismiss()
        }

        dialogBinding.fiftyKg.setOnClickListener {
            qty = 50.0
            viewModel.updateProductQuantity(position,qty)
            binding.quantity.editText?.setText(qty.toString())
            dialog.dismiss()
        }

        dialogBinding.hundredKg.setOnClickListener {
            qty = 100.0
            viewModel.updateProductQuantity(position,qty)
            binding.quantity.editText?.setText(qty.toString())
            dialog.dismiss()
        }


    }

    /**Scanner SetUp**/
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
                    dialog.dismiss()
                    viewModel.name = it.text
                    viewModel.validProduct = true
                    weightDialog()
                    binding.selectProductField.setText("")

                }

            }

            errorCallback = ErrorCallback {
                Log.i("Main","Camera Initialization Error : ${it.message}")
                dialog.dismiss()
            }
        }

        dialogBinding.scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
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
            CAMERA_REQUEST_CODE
        )
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
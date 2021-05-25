package com.example.shopmanagement.ui.authentication

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.shopmanagement.BaseFragment
import com.example.shopmanagement.R
import com.example.shopmanagement.data.models.api.authentication.License
import com.example.shopmanagement.databinding.FragmentAuthBinding
import com.example.shopmanagement.ui.utils.*
import dagger.hilt.android.AndroidEntryPoint
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.dialog_otp_input.*
import kotlinx.android.synthetic.main.fragment_license.*

@AndroidEntryPoint
class AuthFragment : BaseFragment() {
    private val viewModel : AuthViewModel by viewModels()
    lateinit var binding : FragmentAuthBinding
    lateinit var dialog : MaterialDialog
    lateinit var  loader : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAuthBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.authViewModel = viewModel

        val toolbar = activity?.findViewById<TextView>(R.id.titleToolbar)
        toolbar?.setText("POSTALLY LITE")
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loader = SpotsDialog.Builder().setContext(requireContext()).build()

        val userID = Preference(requireContext()).getInt("USER_ID")
        val rememberToken = Preference(requireContext()).getString("REMEMBER_TOKEN")
        val expired = Preference(requireContext()).getString("EXPIRE_DATE")
        val mobile = Preference(requireContext()).getString("MOBILE")
        val email = Preference(requireContext()).getString("EMAIL")
        viewModel.mobileNumber.value = mobile



        if(viewModel.checkExpire(expired) && userID >0){

            rememberToken?.let {token ->
                    if(token.isNotEmpty()){
                        findNavController().navigate(R.id.homeFragment)
                    }
            }

        }else{
            var adapterCustomer = ArrayAdapter<String>(
                requireContext(),
                R.layout.drop_down_layout,
                listOf()
            )

            binding.selectCategoryField.setAdapter(adapterCustomer)

            dialog = MaterialDialog(requireContext())
                .customView(R.layout.dialog_otp_input)
                .noAutoDismiss()

            viewModel.errorMessage.observe(viewLifecycleOwner, EventObserver {
                showToast(it)
            })

            binding.selectCategoryField.setOnItemClickListener { parent, view, position, id ->
                val categoryName = adapterCustomer.getItem(position)
                viewModel.getCategoryDetails(categoryName)

            }

            viewModel.getCategory().observe(viewLifecycleOwner, Observer {
                it?.let {resource ->
                    when(resource.status){
                        Status.SUCCESS->{
                            adapterCustomer = ArrayAdapter<String>(
                                requireContext(),
                                R.layout.drop_down_layout,
                                viewModel.getCategoryName()
                            )
                            binding.selectCategoryField.setAdapter(adapterCustomer)

                        }
                        Status.LOADING->{

                        }
                        Status.ERROR->{
                            showToast("Error Category")
                        }
                    }
                }
            })

            binding.validateBtn.setOnClickListener {

                if(viewModel.fieldValidation()){
                    viewModel.authenticate(mobile,email).observe(viewLifecycleOwner, Observer {
                        it?.let { resource ->
                            when (resource.status) {
                                Status.SUCCESS -> {
                                    loader.dismiss()
                                    val license = it.data?.body()?.license
                                    if(viewModel.checkExpire(license?.expire_at)){
                                        Preference(requireContext()).addString("EXPIRE_DATE",license?.expire_at?:"")
                                        createUser(license)
                                    }else{
                                        Preference(requireContext()).addString("EXPIRE_DATE",license?.expire_at?:"")
                                        val dialog = MaterialDialog(requireContext())
                                            .title(R.string.expired_title)
                                            .message(R.string.expired)
                                            .positiveButton {
                                                dialog.dismiss()
                                            }
                                        dialog.show()
                                    }

                                }
                                Status.LOADING -> {
                                    loader.show()
                                }
                                Status.ERROR -> {
                                    loader.dismiss()
                                    showToast("${it.message}")
                                }
                            }
                        }
                    })
                }else{
                    showToast("Error")
                }

            }
        }




    }

    private fun createUser(license: License?) {
        val mobile = Preference(requireContext()).getString("MOBILE")
        val email = Preference(requireContext()).getString("EMAIL")
        license?.let {
            viewModel.createUser(license,mobile,email).observe(viewLifecycleOwner, Observer {
                it?.let {resource->
                    when(resource.status){
                        Status.SUCCESS->{
                            loader.dismiss()
                            LoadingDialog(requireContext()).dismissDialog()
                            val user = it.data?.body()
                            when(user?.success){
                                200->{
                                    Preference(requireContext()).addString("REMEMBER_TOKEN", user.data.remember_token)
                                    Preference(requireContext()).addString("CATEGORY_ID", user.category_id.toString())
                                    Preference(requireContext()).addInt("USER_ID",user.data.id)
                                    Preference(requireContext()).addInt("LOAD_USER_DATA",1)
                                    findNavController().navigate(R.id.action_authFragment_to_homeFragment)
                                    dialog.dismiss()
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

}
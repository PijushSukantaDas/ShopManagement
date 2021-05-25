package com.example.shopmanagement.ui.authentication

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.shopmanagement.BaseFragment
import com.example.shopmanagement.R
import com.example.shopmanagement.databinding.FragmentOtpBinding
import com.example.shopmanagement.ui.utils.LoadingDialog
import com.example.shopmanagement.ui.utils.Preference
import com.example.shopmanagement.ui.utils.Status
import com.example.shopmanagement.ui.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import dmax.dialog.SpotsDialog

@AndroidEntryPoint
class OtpFragment : BaseFragment() {
    lateinit var binding : FragmentOtpBinding
    private val viewModel : LicenseViewModel by viewModels()
    lateinit var  loader : AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loader = SpotsDialog.Builder().setContext(requireContext()).build()

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Preference(requireContext()).addString("OTP_SEND", "Not SEND")
                findNavController().navigate(R.id.action_otpFragment_to_licenseFragment)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOtpBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mobile = Preference(requireContext()).getString("MOBILE")
        val licenseId = Preference(requireContext()).getString("LICENSE_ID")
        val email = Preference(requireContext()).getString("EMAIL")
        val otp = Preference(requireContext()).getString("OTP")
        val expired = Preference(requireContext()).getString("EXPIRE_DATE")
        val userID = Preference(requireContext()).getInt("USER_ID")
        val companyName = Preference(requireContext()).getString("COMPANY_NAME")
        

        binding.submitBtn.setOnClickListener {
            when {
                binding.otp.text.toString().isNullOrEmpty() -> {
                    showToast("Enter Otp")
                }
                binding.otp.text.toString() == otp -> {
                    if((viewModel.checkExpire(expired) || viewModel.dateDifference(expired)!! < 30) && (companyName.toString().isNotEmpty() || companyName != "")){
                        createUser(mobile,licenseId,email,companyName)
                    }else{
                        findNavController().navigate(R.id.action_otpFragment_to_authFragment)
                    }
                }
                else -> {
                    showToast("OTP Doesn't Matched")
                }
            }
        }
    }

    private fun createUser(
        mobile: String?,
        licenseId: String?,
        email: String?,
        companyName: String?
    ) {

        viewModel.createUser(mobile,licenseId,email,companyName).observe(viewLifecycleOwner, Observer {
            it?.let {resource->
                when(resource.status){
                    Status.SUCCESS->{
                        loader.dismiss()
                        LoadingDialog(requireContext()).dismissDialog()
                        val user = it.data?.body()
                        when(user?.success){
                            200->{
                                Preference(requireContext()).addString("REMEMBER_TOKEN", user.data.remember_token)
                                Preference(requireContext()).addString("CATEGORY_ID", user.category_id.toString().trim())
                                Preference(requireContext()).addInt("USER_ID",user.data.id)
                                Preference(requireContext()).addInt("LOAD_USER_DATA",1)
                                findNavController().navigate(R.id.action_otpFragment_to_homeFragment)
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
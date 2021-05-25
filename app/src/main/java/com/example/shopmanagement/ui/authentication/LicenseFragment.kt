package com.example.shopmanagement.ui.authentication

import android.app.AlertDialog
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.shopmanagement.BaseFragment
import com.example.shopmanagement.R
import com.example.shopmanagement.databinding.FragmentLicenseBinding
import com.example.shopmanagement.ui.utils.*
import dagger.hilt.android.AndroidEntryPoint
import dmax.dialog.SpotsDialog
import java.util.regex.Pattern

@AndroidEntryPoint
class LicenseFragment : BaseFragment() {
    lateinit var binding : FragmentLicenseBinding
    private val viewModel : LicenseViewModel by viewModels()
    var country : String? = null
    lateinit var dialog : MaterialDialog
    lateinit var  loader : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tm  = requireContext().getSystemService(AppCompatActivity.TELEPHONY_SERVICE) as TelephonyManager
        country = tm.simCountryIso
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLicenseBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userID = Preference(requireContext()).getInt("USER_ID")
        val rememberToken = Preference(requireContext()).getString("REMEMBER_TOKEN")
        val expired = Preference(requireContext()).getString("EXPIRE_DATE")
        val otpSend = Preference(requireContext()).getString("OTP_SEND")
        val mobilePref = Preference(requireContext()).getString("MOBILE")
        val emailPref = Preference(requireContext()).getString("EMAIL")

        loader = SpotsDialog.Builder().setContext(requireContext()).build()

        dialog = MaterialDialog(requireContext())
            .customView(R.layout.dialog_otp_input)
            .noAutoDismiss()

        when(country?.toUpperCase()){
            "BD" -> {
                binding.clientEmailNumber.visibility = View.GONE
            }
            else->{
                binding.clientMobileNumber.visibility = View.GONE
            }
        }


        if(viewModel.checkExpire(expired) && userID >0){
            rememberToken?.let {token ->
                if(token.isNotEmpty()){
                    findNavController().navigate(R.id.homeFragment)
                }
            }

        }else if(otpSend=="SEND"){
            findNavController().navigate(R.id.action_licenseFragment_to_otpFragment)
        }else{
            viewModel.errorMessage.observe(viewLifecycleOwner, EventObserver {
                showToast(it)
            })

            binding.registerBtn.setOnClickListener {

                val mobile = binding.mobile.text.toString().trim()
                val email = binding.email.text.toString().trim()
                val otp = numberValidator(mobile)
                val emailOtp = email.isNotEmpty()

                if((((mobilePref == mobile) && (email.isNullOrEmpty())) || ((emailPref == email) && mobile.isNullOrEmpty())) && (otpSend=="Not SEND")){
                    Preference(requireContext()).addString("OTP_SEND", "SEND")
                    findNavController().navigate(R.id.action_licenseFragment_to_otpFragment)
                }else{
                    if((numberValidator(mobile)&&(mobile.startsWith("01"))) || (email.isNotEmpty() && country?.toUpperCase() != "BD")){
                        if(viewModel.emailValidation() && mobile.isNullOrEmpty()){
                            sendOtp(otp,emailOtp)
                        }else if(!viewModel.emailValidation() && mobile.isNullOrEmpty()){
                            showToast("Enter Valid Email")
                        }else{
                            sendOtp(otp,emailOtp)
                        }

                    }else{
                        showToast("Enter Valid Phone Number")
                    }
                }

            }
        }

    }


    private fun sendOtp(otp: Boolean, emailOtp: Boolean) {
        viewModel.bDAuthenticate(otp,emailOtp).observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        loader.dismiss()

                        val license = it.data?.body()?.license
                        Preference(requireContext()).addString("EXPIRE_DATE",license?.expire_at?:"")

                        if(viewModel.checkExpire(license?.expire_at)) {
                            dialog.cancelOnTouchOutside(false)
                            Preference(requireContext()).addString("MOBILE",license?.mobile?:"")
                            Preference(requireContext()).addString("LICENSE_ID",license?.id?.toString()?:"")
                            Preference(requireContext()).addString("EMAIL",binding.email.text.toString().trim())
                            Preference(requireContext()).addString("OTP_SEND", "SEND")
                            Preference(requireContext()).addString("COMPANY_NAME", license?.client_company_name?:"")

                            findNavController().navigate(R.id.action_licenseFragment_to_otpFragment)
                        }
                        else{
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
    }


    private fun numberValidator(number : String): Boolean {
        val format = Pattern.compile("^(?:\\+?88)?01[15-9]\\d{8}\$")
        return format.matcher(number).matches()
    }

}
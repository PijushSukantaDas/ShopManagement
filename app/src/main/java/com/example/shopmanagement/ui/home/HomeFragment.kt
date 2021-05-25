package com.example.shopmanagement.ui.home

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.shopmanagement.BaseFragment
import com.example.shopmanagement.R
import com.example.shopmanagement.databinding.FragmentHomeBinding
import com.example.shopmanagement.ui.utils.Preference
import com.example.shopmanagement.ui.utils.Status
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.AndroidEntryPoint
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.dashboard_dialog_show.*
import kotlinx.android.synthetic.main.fragment_home.*


@AndroidEntryPoint
class HomeFragment : BaseFragment() {
    lateinit var binding : FragmentHomeBinding
    private val viewModel : HomeViewModel by viewModels()
    private lateinit var dialog: MaterialDialog
    lateinit var  loader : AlertDialog

    private var mInterstitialAd: InterstitialAd? = null


    private val colorList : ArrayList<Int> = arrayListOf()
    var pieDataSet = PieDataSet(listOf(), "Info")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MobileAds.initialize(requireContext()) {}

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mInterstitialAd?.show(requireActivity())
                requireActivity().finishAffinity()
            }
        })
    }

    private fun bannerAddInit() {
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        val toolbar = activity?.findViewById<TextView>(R.id.titleToolbar)
        toolbar?.setText("POSTALLY LITE")

        bannerAddInit()
        inertialAdInit()

        return binding.root
    }

    private fun inertialAdInit() {
        var adRequest = AdRequest.Builder().build()

        InterstitialAd.load(requireContext(),requireActivity().getString(R.string.INTERSTITIAL_AD_ID),adRequest,object : InterstitialAdLoadCallback(){
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }
        })


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val userID = Preference(requireContext()).getInt("LOAD_USER_DATA")

        loader = SpotsDialog.Builder().setContext(requireContext()).build()

        if(userID == 1){
            storeToLocalDb()
        }



        binding.monthlyBtn.setOnClickListener {

            mInterstitialAd?.show(requireActivity())
            val list = viewModel.pieChartMonthlyData()

            binding.dailyBtn.setTextSize(12f)
            binding.monthlyBtn.setTextSize(17f)
            binding.yearlyBtn.setTextSize(12f)
            pieChartColor()

            setPieDataSet(list)

            setPieData(pieDataSet)

            binding.pieChart.notifyDataSetChanged()

            binding.pieChart.invalidate()

        }

        binding.yearlyBtn.setOnClickListener {
            val list = viewModel.pieChartYearlyData()

            binding.dailyBtn.setTextSize(12f)
            binding.monthlyBtn.setTextSize(12f)
            binding.yearlyBtn.setTextSize(18f)

            pieChartColor()

            setPieDataSet(list)

            setPieData(pieDataSet)

            binding.pieChart.notifyDataSetChanged()

            binding.pieChart.notifyDataSetChanged()

            binding.pieChart.invalidate()
        }

        binding.dailyBtn.setOnClickListener {
            val list = viewModel.pieChartDailyData()


            binding.dailyBtn.setTextSize(18f)
            binding.monthlyBtn.setTextSize(12f)
            binding.yearlyBtn.setTextSize(12f)

            pieChartColor()

            setPieDataSet(list)

            setPieData(pieDataSet)

            binding.pieChart.notifyDataSetChanged()

            binding.pieChart.notifyDataSetChanged()

            binding.pieChart.invalidate()
        }

        viewModel.getDashBoard().observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        loader.dismiss()

                        binding.totalProduct.text = it.data?.body()?.data?.active_product.toString()
                        binding.totalCustomer.text =
                            it.data?.body()?.data?.no_of_customer.toString()
                        binding.totalSupplier.text =
                            it.data?.body()?.data?.no_of_supplier.toString()
                        binding.dailyTotalSale.text = it.data?.body()?.data?.sale_today.toString()
                        binding.monthlytotalSale.text =
                            it.data?.body()?.data?.sale_amount_this_month.toString()
                        binding.yearlyTotalSale.text =
                            it.data?.body()?.data?.sale_amount_this_year.toString()

                        binding.dailytotalreceipt.text =
                            it.data?.body()?.data?.deposit_today.toString()
                        binding.monthlyTotalReceip.text =
                            it.data?.body()?.data?.deposit_thisMonth.toString()
                        binding.yearlyTotalReceipt.text =
                            it.data?.body()?.data?.receipt_amount_this_year.toString()

                        binding.dailyTotalPayment.text =
                            it.data?.body()?.data?.expense_today.toString()
                        binding.monthlyTotalPayment.text =
                            it.data?.body()?.data?.expense_amount_this_month.toString()
                        binding.yearlyTotalPayment.text =
                            it.data?.body()?.data?.expense_amount_this_year.toString()

                        binding.duePurchase.text = it.data?.body()?.data?.duePurchase.toString()

                        binding.dueSale.text = it.data?.body()?.data?.dueSale.toString()

                        val list = viewModel.pieChartMonthlyData()
                        binding.dailyBtn.setTextSize(12f)
                        binding.monthlyBtn.setTextSize(18f)
                        binding.yearlyBtn.setTextSize(12f)

                        pieChartColor()

                        setPieDataSet(list)

                        setPieData(pieDataSet)

                        binding.pieChart.notifyDataSetChanged()

                        binding.pieChart.notifyDataSetChanged()
                        binding.pieChart.invalidate()


                    }
                    Status.LOADING -> {
                        loader.show()
                    }
                    Status.ERROR -> {
                        loader.dismiss()
                    }
                }
            }
        })





        binding.goToDashboard.setOnClickListener {

            dialog = MaterialDialog(requireContext())
                .customView(R.layout.dashboard_dialog_show)


            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog.gotoProduct.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_productActivity)
                dialog.dismiss()
            }

            dialog.gotoPurchase.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_purchaseActivity)
                dialog.dismiss()
            }

            dialog.gotoSales.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_salesActivity)
                dialog.dismiss()
            }

            dialog.gotoCustomer.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_customerActivity)
                dialog.dismiss()
            }

            dialog.gotoSupplier.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_supplierActivity)
                dialog.dismiss()
            }

            dialog.gotoReceipt.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_receiptActivity)
                dialog.dismiss()
            }

            dialog.gotoPayment.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_paymentActivity)
                dialog.dismiss()
            }

            dialog.show()
        }
    }

    private fun storeToLocalDb() {

        viewModel.getCustomer()
        viewModel.getProduct()
        viewModel.getSupplier()

        Preference(requireContext()).addInt("LOAD_USER_DATA",2)
    }


    private fun setPieDataSet(list: java.util.ArrayList<PieEntry>) {
        pieDataSet = PieDataSet(list, "Info")
        pieDataSet.setColors(colorList)
        pieDataSet.valueTextColor = Color.WHITE
        pieDataSet.valueTextSize = 12f
        pieChart.setEntryLabelTextSize(0f)
    }

    private fun setPieData(pieDataSet: PieDataSet) {
        val pieData = PieData(pieDataSet)
        binding.pieChart.data = pieData
        binding.pieChart.description.isEnabled = false
        binding.pieChart.centerText = "POSTALLY"
        binding.pieChart.animate()

    }

    private fun pieChartColor() {
        colorList.add(resources.getColor(R.color.invoice_color))
        colorList.add(resources.getColor(R.color.receipt_color))
        colorList.add(resources.getColor(R.color.purchase_color))
        colorList.add(resources.getColor(R.color.payment_color))

    }


}
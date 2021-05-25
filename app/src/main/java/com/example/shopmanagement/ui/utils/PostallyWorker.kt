package com.example.shopmanagement.ui.utils

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.lifecycle.liveData
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.shopmanagement.data.models.Customer
import com.example.shopmanagement.domain.CustomerUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers

@HiltWorker
class PostallyWorker @AssistedInject constructor(@Assisted appContext: Context, @Assisted workerParams: WorkerParameters,
                                             private val customerUseCase: CustomerUseCase
): Worker(appContext, workerParams) {


    override fun doWork(): Result {

        val customer : HashMap<String, com.example.shopmanagement.data.models.api.customer.Data> = hashMapOf()

        val id = inputData.getString("id")
        val name =  inputData.getString("name")
        val address =  inputData.getString("address")
        val mobile =  inputData.getString("mobile")


        // Do the work here--in this case, upload the images.
        createCustomer(name,address,mobile,id)

        // Indicate whether the work finished successfully with the Result
        return Result.success()
    }

    private fun createCustomer(name: String?, address: String?, mobile: String?, id: String?) {

        liveData(Dispatchers.IO) {
            emit(
                customerUseCase.insertCustomer(
                    Customer(
                        id?.toInt()?:0,
                        name?:"",
                        address?:"",
                        mobile?:"",
                        "1",
                        "",
                        "",
                        "",
                        0.0,
                        ""
                    )
                )
            )
        }

    }

}
package com.example.shopmanagement.di

import android.content.Context
import com.example.shopmanagement.data.dao.*
import com.example.shopmanagement.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun getProductDao(@ApplicationContext context: Context): IProductDao {
        return AppDatabase.getInstance(context).getProductDao()
    }

    @Singleton
    @Provides
    fun getCustomerDao(@ApplicationContext context: Context): ICustomerDao {
        return AppDatabase.getInstance(context).getCustomerDao()
    }

    @Singleton
    @Provides
    fun getSupplierDao(@ApplicationContext context: Context): ISupplierDao {
        return AppDatabase.getInstance(context).getSupplierDao()
    }

    @Singleton
    @Provides
    fun getPurchaseDao(@ApplicationContext context: Context): IPurchaseDao {
        return AppDatabase.getInstance(context).getPurchaseDao()
    }

    @Singleton
    @Provides
    fun getInvoiceDao(@ApplicationContext context: Context): IInvoiceDao {
        return AppDatabase.getInstance(context).getInvoiceDao()
    }

    @Singleton
    @Provides
    fun getSalesDao(@ApplicationContext context: Context): ISalesDao {
        return AppDatabase.getInstance(context).getSalesDao()
    }

    @Singleton
    @Provides
    fun getCodesDao(@ApplicationContext context: Context): ICodesDao {
        return AppDatabase.getInstance(context).getCodesDao()
    }

    @Singleton
    @Provides
    fun getReceiptDao(@ApplicationContext context: Context): IReceiptDao {
        return AppDatabase.getInstance(context).getReceiptDao()
    }

    @Singleton
    @Provides
    fun getDailyAccountDao(@ApplicationContext context: Context): IDailyAccountDao {
        return AppDatabase.getInstance(context).getDailyAccountDao()
    }

    @Singleton
    @Provides
    fun getPurchaseInvoiceDao(@ApplicationContext context: Context): IPurchaseInvoiceDao {
        return AppDatabase.getInstance(context).getPurchaseInvoiceDao()
    }

    @Singleton
    @Provides
    fun getPaymentDao(@ApplicationContext context: Context): IPaymentDao {
        return AppDatabase.getInstance(context).getPaymentDao()
    }

}
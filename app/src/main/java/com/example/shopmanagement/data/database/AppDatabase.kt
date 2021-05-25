package com.example.shopmanagement.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.shopmanagement.data.dao.*
import com.example.shopmanagement.data.models.*

@Database(
    entities = [
        Product::class,
        Customer::class,
        Supplier::class,
        Purchase::class,
        Invoice::class,
        Sales::class,
        Codes::class,
        DailyAccounts::class,
        PurchaseInvoice::class,
        Receipt::class,
        Payment::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getProductDao(): IProductDao
    abstract fun getCustomerDao(): ICustomerDao
    abstract fun getSupplierDao() : ISupplierDao
    abstract fun getPurchaseDao() : IPurchaseDao
    abstract fun getInvoiceDao() : IInvoiceDao
    abstract fun getSalesDao() : ISalesDao
    abstract fun getCodesDao() : ICodesDao
    abstract fun getReceiptDao() : IReceiptDao
    abstract fun getDailyAccountDao() : IDailyAccountDao
    abstract fun getPurchaseInvoiceDao() : IPurchaseInvoiceDao
    abstract fun getPaymentDao() : IPaymentDao

    companion object {
        private lateinit var INSTANCE: AppDatabase

        fun getInstance(context: Context): AppDatabase {
            if (!::INSTANCE.isInitialized) {
                synchronized(AppDatabase::class) {
                    INSTANCE =
                        Room.databaseBuilder(context, AppDatabase::class.java, "POSTALLY")
                            .fallbackToDestructiveMigration()
                            .build()
                }
            }
            return INSTANCE
        }
    }
}
package com.example.shopmanagement.data.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "Customer", indices = [Index("id", unique = true)])
data class Customer(
    @PrimaryKey(autoGenerate = false)
    val id : Int,
    @ColumnInfo(name = "name")
    var customerName: String,
    var customerAddress: String,
    var customerMobile: String,
    var customerGender: String?,
    val customerNid : String?,
    var customerVat: String?,
    var customerEmail: String?,
    var customerOpeningBalance : Double,
    var customerReceiptType : String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readDouble(),
        parcel.readString()?:""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(customerName)
        parcel.writeString(customerAddress)
        parcel.writeString(customerMobile)
        parcel.writeString(customerGender)
        parcel.writeString(customerNid)
        parcel.writeString(customerVat)
        parcel.writeString(customerEmail)
        parcel.writeDouble(customerOpeningBalance)
        parcel.writeString(customerReceiptType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Customer> {
        override fun createFromParcel(parcel: Parcel): Customer {
            return Customer(parcel)
        }

        override fun newArray(size: Int): Array<Customer?> {
            return arrayOfNulls(size)
        }
    }
}
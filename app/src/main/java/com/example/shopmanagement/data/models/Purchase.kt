package com.example.shopmanagement.data.models

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.*
import java.util.*

@Entity(tableName = "Purchase", indices = [Index("id", unique = true)])
@TypeConverters(DateTypeConverter::class)
data class Purchase(
        @PrimaryKey(autoGenerate = true)
        @NonNull
        val id: Int,
        val productId : Int,
        val dailyAccountId : String,
        val manufactureDate : Date,
        val expireDate : Date,
        val purchasePrice : Double,
        val quantity : Double,
        val sellQuantity : Double,
        val supplierId : Int,
        val purchaseDate: Date,
        val totalAmount : Double,
        val userId : String,
        val purchaseInvoice : Int,
        val paymentId : String,
        val createdAt : Date,
        val updatedAt : Date,
        val deletedAt : Date

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()?:"",
        TODO("manufactureDate"),
        TODO("expireDate"),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readInt(),
        TODO("purchaseDate"),
        parcel.readDouble(),
        parcel.readString()?:"",
        parcel.readInt(),
        parcel.readString()?:"",
        TODO("createdAt"),
        TODO("updatedAt"),
        TODO("deletedAt")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(productId)
        parcel.writeString(dailyAccountId)
        parcel.writeDouble(purchasePrice)
        parcel.writeDouble(quantity)
        parcel.writeDouble(sellQuantity)
        parcel.writeInt(supplierId)
        parcel.writeDouble(totalAmount)
        parcel.writeString(userId)
        parcel.writeInt(purchaseInvoice)
        parcel.writeString(paymentId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Purchase> {
        override fun createFromParcel(parcel: Parcel): Purchase {
            return Purchase(parcel)
        }

        override fun newArray(size: Int): Array<Purchase?> {
            return arrayOfNulls(size)
        }
    }
}


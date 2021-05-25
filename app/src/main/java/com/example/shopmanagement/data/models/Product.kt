package com.example.shopmanagement.data.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "Product", indices = [Index("id", unique = true)])
data class Product(

    @PrimaryKey(autoGenerate = false)
    val id : Int,
    var name: String,
    var sellingPrice: Double,
    var category: String?,
    var subCategory: String?,
    val code: String?,
    var description: String?,
    var productImage: String?,
    var alertQuantity : Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()?:"",
        parcel.readDouble(),
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeDouble(sellingPrice)
        parcel.writeString(category)
        parcel.writeString(subCategory)
        parcel.writeString(code)
        parcel.writeString(description)
        parcel.writeString(productImage)
        parcel.writeInt(alertQuantity)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}
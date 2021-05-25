package com.example.shopmanagement.data.models

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.*
import com.google.gson.Gson
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "Supplier", indices = [Index("id", unique = true)])
@TypeConverters(DateTypeConverter::class)
data class Supplier(
    @PrimaryKey(autoGenerate = false)
    @NonNull
    var id: Int,
    val name : String,
    var ownerName : String,
    var contactPerson : String,
    var tinNo : String?,
    var taxNo : String?,
    var vatNo : String?,
    var bstiNo : String?,
    var mobile : String,
    var address : String,
    var email : String?,
    var image : String?,
    var openingBalance : Double,
    var status : Int,
    var createdAt : Date,
    var updateAt : Date
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
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readDouble(),
        parcel.readInt(),
        TODO("createdAt"),
        TODO("updateAt")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(ownerName)
        parcel.writeString(contactPerson)
        parcel.writeString(tinNo)
        parcel.writeString(taxNo)
        parcel.writeString(vatNo)
        parcel.writeString(bstiNo)
        parcel.writeString(mobile)
        parcel.writeString(address)
        parcel.writeString(email)
        parcel.writeString(image)
        parcel.writeDouble(openingBalance)
        parcel.writeInt(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Supplier> {
        override fun createFromParcel(parcel: Parcel): Supplier {
            return Supplier(parcel)
        }

        override fun newArray(size: Int): Array<Supplier?> {
            return arrayOfNulls(size)
        }
    }
}

class DateTypeConverter {
    val gson = Gson()

    @TypeConverter
    fun dateToString(date: Date?): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.US)
        try {
            val dateTime: String = dateFormat.format(date!!)
            println("Current Date Time : $dateTime")
            return dateTime;
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    @TypeConverter
    fun stringToDate(dateString: String): Date? {
        val format = SimpleDateFormat("dd-MM-yyyy", Locale.US)
        try {
            val date = format.parse(dateString)
            println(date)
            return date
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }
}
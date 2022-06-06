package com.unnamedgroup.tourapp.model.business

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.unnamedgroup.tourapp.model.rest.PassengerREST

class Passenger(
    var id: Int,
    var name: String,
    var dni: String,
    var busBoarded: Boolean
) : Parcelable {

    fun Boolean.toInt() = if (this) 1 else 0

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt() == 1
    )

    fun toRest(): PassengerREST {
        return PassengerREST(id, name, dni, busBoarded)
    }
    fun getFormatted() : String {
        return "$name - $dni"
    }

    @SuppressLint("NewApi")
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(dni)
        parcel.writeInt(busBoarded.toInt())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Passenger> {
        override fun createFromParcel(parcel: Parcel): Passenger {
            return Passenger(parcel)
        }

        override fun newArray(size: Int): Array<Passenger?> {
            return arrayOfNulls(size)
        }
    }
}
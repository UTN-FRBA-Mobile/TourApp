package com.unnamedgroup.tourapp.model.business

import android.os.Parcel
import android.os.Parcelable
import com.unnamedgroup.tourapp.model.rest.PassengerREST

class Passenger(
    var id: Int,
    var name: String,
    var dni: String,
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!
    )

    fun toRest(): PassengerREST {
        return PassengerREST(id, name, dni)
    }
    fun getFormatted() : String {
        return "$name - $dni"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(dni)
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
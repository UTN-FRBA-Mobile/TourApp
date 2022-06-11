package com.unnamedgroup.tourapp.model.business

import android.os.Parcel
import android.os.Parcelable

class TripPassenger(
    val name: String,
    val dni: String,
    val busBoarding: String,
    val busStop: String,
    val busBoarded: Boolean
): Parcelable {

    fun Boolean.toInt() = if (this) 1 else 0

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt() == 1
    )


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(dni)
        parcel.writeString(busBoarding)
        parcel.writeString(busStop)
        parcel.writeInt(busBoarded.toInt())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TripPassenger> {
        override fun createFromParcel(parcel: Parcel): TripPassenger {
            return TripPassenger(parcel)
        }

        override fun newArray(size: Int): Array<TripPassenger?> {
            return arrayOfNulls(size)
        }
    }
}
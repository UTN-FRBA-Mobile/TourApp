package com.unnamedgroup.tourapp.model.business

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable

class TripPassenger(
    val name: String,
    val dni: String,
    val busBoarding: String,
    val busStop: String,
): Parcelable {
    @SuppressLint("NewApi")
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(dni)
        parcel.writeString(busBoarding)
        parcel.writeString(busStop)
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
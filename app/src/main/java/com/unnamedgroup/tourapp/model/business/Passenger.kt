package com.unnamedgroup.tourapp.model.business

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable

class Passenger(
    val name: String?,
    val dni: String?,
    val busBoarded: Boolean,
) : Parcelable {
    @SuppressLint("NewApi")
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readBoolean()!!
    ) {
    }

    @SuppressLint("NewApi")
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(dni)
        parcel.writeBoolean(busBoarded)
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
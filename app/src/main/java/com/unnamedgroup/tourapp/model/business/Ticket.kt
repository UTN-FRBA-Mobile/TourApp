package com.unnamedgroup.tourapp.model.business

import android.os.Parcel
import android.os.Parcelable

class Ticket(
    val id: Int,
    val user: User,
    val dni: String,
    val trip: Trip,
    val busBoarding: String,
    val busStop: String,
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readParcelable(User::class.java.classLoader)!!,
        parcel.readString()!!,
        parcel.readParcelable(Trip::class.java.classLoader)!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeParcelable(user, flags)
        parcel.writeString(dni)
        parcel.writeParcelable(trip, flags)
        parcel.writeString(busBoarding)
        parcel.writeString(busStop)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Ticket> {
        override fun createFromParcel(parcel: Parcel): Ticket {
            return Ticket(parcel)
        }

        override fun newArray(size: Int): Array<Ticket?> {
            return arrayOfNulls(size)
        }
    }

}
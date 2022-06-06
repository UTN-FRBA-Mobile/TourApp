package com.unnamedgroup.tourapp.model.business

import android.os.Parcel
import android.os.Parcelable
import com.unnamedgroup.tourapp.model.rest.PassengerREST
import com.unnamedgroup.tourapp.model.rest.TicketREST

class Ticket(
    val id: Int?,
    val user: User?,
    var passengers: MutableList<Passenger>,
    val trip: Trip,
    var busBoarding: String,
    var busStop: String,
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readParcelable(User::class.java.classLoader)!!,
        mutableListOf<Passenger>().apply {  parcel.readParcelable(Passenger::class.java.classLoader)!! },
        parcel.readParcelable(Trip::class.java.classLoader)!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    fun toRest(): TicketREST{
        val restPassengers: MutableList<PassengerREST> = mutableListOf()
        for (p in passengers) {
                restPassengers.add(p.toRest())
        }
        return TicketREST(id, restPassengers, user.toRest(), trip.toRest(), busBoarding, busStop)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        id?.let { parcel.writeInt(it) }
        parcel.writeParcelable(user, flags)
        parcel.writeArray(passengers.toTypedArray())
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

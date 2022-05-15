package com.unnamedgroup.tourapp.model

import android.os.Parcel
import android.os.Parcelable
import com.unnamedgroup.tourapp.utils.Utils
import java.util.*

class Trip(
    val id: Int,
    var state: TripState,
    val origin: String?,
    val destination: String?,
    val departureTime: String?,
    val date: Date,
    val passengers: MutableList<Passenger>
): Parcelable {

    val dateStr = Utils.getDateWithFormat(date, "EEEE dd/MM/yyyy")

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        TripState.valueOf(parcel.readString()!!),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        Date(parcel.readLong()),
        mutableListOf<Passenger>().apply {
            parcel.readArray(Passenger::class.java.classLoader)
        }
    )

    enum class TripState(val int: Int, val text: String) {
        PROCESSING(1, "Procesando"),
        CONFIRMED(2, "Confirmado"),
        DELAYED(3, "Demorado"),
        CANCELLED(4, "Cancelado"),
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(state.name)
        parcel.writeString(origin)
        parcel.writeString(destination)
        parcel.writeString(departureTime)
        parcel.writeLong(date.time)
        parcel.writeParcelableArray(passengers.toTypedArray(), flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Trip> {
        override fun createFromParcel(parcel: Parcel): Trip {
            return Trip(parcel)
        }

        override fun newArray(size: Int): Array<Trip?> {
            return arrayOfNulls(size)
        }
    }

}
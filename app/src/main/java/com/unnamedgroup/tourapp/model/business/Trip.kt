package com.unnamedgroup.tourapp.model.business

import android.os.Parcel
import android.os.Parcelable
import com.unnamedgroup.tourapp.utils.Utils
import java.util.*

class Trip(
    val id: Int,
    val origin: String,
    val destination: String,
    val passengerAmount: Int,
    val price: Float,
    val busBoardings: MutableList<String>,
    val busStops: MutableList<String>,
    val departureTime: String,
    val date: Date,
    val state: TripState
    ): Parcelable {

    val dateStr = Utils.getDateWithFormat(date, "dd/MM/yyyy")

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readFloat(),
        mutableListOf<String>().apply { parcel.readArray(String::class.java.classLoader) },
        mutableListOf<String>().apply { parcel.readArray(String::class.java.classLoader) },
        parcel.readString()!!,
        Date(parcel.readLong()),
        TripState.valueOf(parcel.readString()!!),
    )

    enum class TripState(val int: Int, val text: String) {
        PROCESSING(1, "Procesando"),
        CONFIRMED(2, "Confirmado"),
        DELAYED(3, "Demorado"),
        CANCELLED(4, "Cancelado"),
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(origin)
        parcel.writeString(destination)
        parcel.writeInt(passengerAmount)
        parcel.writeFloat(price)
        parcel.writeStringArray(busBoardings.toTypedArray())
        parcel.writeStringArray(busStops.toTypedArray())
        parcel.writeString(departureTime)
        parcel.writeLong(date.time)
        parcel.writeString(state.name)
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
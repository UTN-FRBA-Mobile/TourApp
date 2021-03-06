package com.unnamedgroup.tourapp.model.business

import android.os.Parcel
import android.os.Parcelable
import com.unnamedgroup.tourapp.R
import com.unnamedgroup.tourapp.model.rest.TripREST
import com.unnamedgroup.tourapp.utils.Utils
import java.util.*

class Trip(
    val id: Int,
    val origin: String,
    val destination: String,
    var passengersAmount: Int,
    var totalPassengers: Int,
    val price: Float,
    val busBoardings: MutableList<String>,
    val busStops: MutableList<String>,
    var departureTime: String,
    var date: Date,
    var state: TripState,
    val driver: User
) : Parcelable {

    val dateStr = Utils.getDateWithFormat(date, "dd/MM/yyyy")

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readFloat(),
        mutableListOf<String>().apply { parcel.readArray(String::class.java.classLoader) },
        mutableListOf<String>().apply { parcel.readArray(String::class.java.classLoader) },
        parcel.readString()!!,
        Date(parcel.readLong()),
        TripState.valueOf(parcel.readString()!!),
        parcel.readParcelable(User::class.java.classLoader)!!,
    )

    fun toRest(): TripREST {
        val restDate = Utils.getDateWithFormat(date, "dd-MM-yyyy")
        return TripREST(
            id,
            origin,
            destination,
            passengersAmount,
            totalPassengers,
            price,
            busBoardings,
            busStops,
            departureTime,
            restDate,
            this.getRestTripState(),
            driver
        )
    }

    enum class TripState(val int: Int, val text: String) {
        PROCESSING(1, "Procesando"),
        CONFIRMED(2, "Confirmado"),
        DELAYED(3, "Demorado"),
        CANCELLED(4, "Cancelado"),
    }

    private fun getRestTripState(): String {
        return when (state.text) {
            TripState.PROCESSING.text -> "PROCESSING"
            TripState.DELAYED.text -> "DELAYED"
            TripState.CONFIRMED.text -> "CONFIRMED"
            TripState.CANCELLED.text -> "CANCELLED"
            else -> {
                "PROCESSING"
            }
        }
    }

    fun getStateByText(value : String): TripState {
        return when (value) {
            TripState.PROCESSING.text -> TripState.PROCESSING
            TripState.DELAYED.text -> TripState.DELAYED
            TripState.CONFIRMED.text -> TripState.CONFIRMED
            TripState.CANCELLED.text -> TripState.CANCELLED
            else -> {
                TripState.PROCESSING
            }
        }
    }

    fun getName(): String {
        return "$origin - $destination"
    }

    fun getFormattedDepartureTime(): String {
        return "$dateStr - $departureTime"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(origin)
        parcel.writeString(destination)
        parcel.writeInt(passengersAmount)
        parcel.writeInt(totalPassengers)
        parcel.writeFloat(price)
        parcel.writeStringArray(busBoardings.toTypedArray())
        parcel.writeStringArray(busStops.toTypedArray())
        parcel.writeString(departureTime)
        parcel.writeLong(date.time)
        parcel.writeString(state.name)
        parcel.writeParcelable(driver, flags)
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
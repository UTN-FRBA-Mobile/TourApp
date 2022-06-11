package com.unnamedgroup.tourapp.model.rest

import com.unnamedgroup.tourapp.model.business.Trip
import com.unnamedgroup.tourapp.model.business.User
import com.unnamedgroup.tourapp.utils.Utils

data class TripREST(
    val id: Int,
    val origin: String,
    val destination: String,
    val passengersAmount: Int,
    val price: Float,
    val busBoardings: MutableList<String>,
    val busStops: MutableList<String>,
    val departureTime: String,
    val date: String,
    val state: String,
    val driver: User
) {
    fun toTrip(): Trip {
        val date = Utils.parseDateWithFormat(date, "dd-MM-yyyy")
        val state = Trip.TripState.valueOf(state)
        return Trip(
            id,
            origin,
            destination,
            passengersAmount,
            price,
            busBoardings,
            busStops,
            departureTime,
            date,
            state,
            driver
        )
    }
}
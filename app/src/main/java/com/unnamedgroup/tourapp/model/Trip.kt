package com.unnamedgroup.tourapp.model

import java.util.*

class Trip(
    val id: Int,
    var state: TripState,
    val origin: String,
    val destination: String,
    val departureTime: Date,
    val date: Date,
    val passengers: List<Passenger>
) {

    enum class TripState(val int: Int) {
        PROCESSING(1),
        CONFIRMED(2),
        DELAYED(3),
        CANCELLED(4),
    }

}
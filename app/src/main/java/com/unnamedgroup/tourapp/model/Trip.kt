package com.unnamedgroup.tourapp.model

import com.unnamedgroup.tourapp.utils.Utils
import java.util.*

class Trip(
    val id: Int,
    var state: TripState,
    val origin: String,
    val destination: String,
    val departureTime: String,
    val date: Date,
    val passengers: List<Passenger>
) {

    val dateStr = Utils.getDateWithFormat(date, "EEEE dd/MM/yyyy")

    enum class TripState(val int: Int, val text: String) {
        PROCESSING(1, "Procesando"),
        CONFIRMED(2, "Confirmado"),
        DELAYED(3, "Demorado"),
        CANCELLED(4, "Cancelado"),
    }

}
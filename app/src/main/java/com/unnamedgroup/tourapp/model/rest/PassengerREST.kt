package com.unnamedgroup.tourapp.model.rest

import com.unnamedgroup.tourapp.model.business.Passenger

data class PassengerREST(
    val name: String,
    val dni: String,
    val busBoarded: Boolean,
) {
    fun toPassenger(): Passenger {
        return Passenger(
            name,
            dni,
            busBoarded,
        )
    }
}
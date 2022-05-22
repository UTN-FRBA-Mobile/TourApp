package com.unnamedgroup.tourapp.model.rest

import com.unnamedgroup.tourapp.model.business.Passenger

data class PassengerREST(
    val dni: String,
    val name: String,
) {
    fun toPassenger(): Passenger {
        return Passenger(
            dni,
            name,
        )
    }
}
package com.unnamedgroup.tourapp.model.rest

import com.unnamedgroup.tourapp.model.business.Passenger

data class PassengerREST(
    val id: Int,
    val name: String,
    val dni: String,
) {
    fun toPassenger(): Passenger {
        return Passenger(
            id,
            name,
            dni,
        )
    }
}
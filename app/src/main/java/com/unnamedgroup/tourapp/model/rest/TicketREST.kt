package com.unnamedgroup.tourapp.model.rest

import com.unnamedgroup.tourapp.model.business.Passenger
import com.unnamedgroup.tourapp.model.business.Ticket

data class TicketREST(
    val id: Int,
    val passengers: MutableList<PassengerREST>,
    val user: UserREST,
    val trip: TripREST,
    val busBoarding: String,
    val busStop: String
) {
    fun toTicket(): Ticket {
        val passengers: MutableList<Passenger> = mutableListOf()
        for (p in this.passengers) {
            passengers.add(p.toPassenger())
        }
        return Ticket(
            id,
            user.toUser(),
            passengers,
            trip.toTrip(),
            busBoarding,
            busStop
        )
    }
}
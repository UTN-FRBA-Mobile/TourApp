package com.unnamedgroup.tourapp.repository

import com.unnamedgroup.tourapp.model.Passenger
import com.unnamedgroup.tourapp.model.Trip
import java.util.*
import kotlin.collections.ArrayList

class TripsRepository {

    fun getTrips(): ArrayList<Trip> {
        val passenger: Passenger = Passenger("Benja", "40255931")
        val trips = ArrayList<Trip>()
        trips.add(Trip(1, Trip.TripState.PROCESSING, "Mercedes", "Capital Federal", Date(), Date(), ArrayList<Passenger>(
            listOf(passenger))))
        trips.add(Trip(2, Trip.TripState.CONFIRMED, "Capital Federal", "Mercedes", Date(), Date(), ArrayList<Passenger>(
            listOf(passenger))))
        return trips
    }

}
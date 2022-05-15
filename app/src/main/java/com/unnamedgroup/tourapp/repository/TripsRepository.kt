package com.unnamedgroup.tourapp.repository

import com.unnamedgroup.tourapp.model.Passenger
import com.unnamedgroup.tourapp.model.Trip
import java.util.*
import kotlin.collections.ArrayList

class TripsRepository {

    fun getTrips(): MutableList<Trip> {
        val passenger = Passenger("Benja", "40255931")
        val trips = mutableListOf<Trip>()
        trips.add(Trip(1, Trip.TripState.PROCESSING, "Mercedes", "Capital Federal", "16:00", Date(), ArrayList<Passenger>(listOf(passenger))))
        trips.add(Trip(2, Trip.TripState.CONFIRMED, "Capital Federal", "Mercedes", "18:00", Date(), ArrayList<Passenger>(listOf(passenger))))
        trips.add(Trip(3, Trip.TripState.CONFIRMED, "Ituzaingó", "San Martín", "9:00", Date(), ArrayList<Passenger>(listOf(passenger))))
        trips.add(Trip(4, Trip.TripState.CANCELLED, "Mercedes", "San Martín", "13:00", Date(), ArrayList<Passenger>(listOf(passenger))))
        trips.add(Trip(5, Trip.TripState.DELAYED, "Mercedes", "San Jacinto", "19:00", Date(), ArrayList<Passenger>(listOf(passenger))))
        trips.add(Trip(6, Trip.TripState.DELAYED, "Mercedes", "Luján", "19:00", Date(), ArrayList<Passenger>(listOf(passenger))))
        return trips
    }

}
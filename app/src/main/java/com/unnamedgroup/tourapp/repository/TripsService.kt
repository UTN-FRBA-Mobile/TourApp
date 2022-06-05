package com.unnamedgroup.tourapp.repository

import com.unnamedgroup.tourapp.model.rest.TicketREST
import com.unnamedgroup.tourapp.model.rest.TripREST
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TripsService {
    @GET("trips")
    fun getTrips(): Call<MutableList<TripREST>>

    @GET("tickets")
    fun getTicketsByUser(@Query("user.id") userId: Int): Call<MutableList<TicketREST>>

    @GET("tickets?_sort=trip.date,trip.departureTime&_order=desc,desc&_limit=1")
    fun getLastTicketByUser(@Query("user.id") userId: Int): Call<MutableList<TicketREST>>
}
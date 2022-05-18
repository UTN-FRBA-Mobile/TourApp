package com.unnamedgroup.tourapp.repository

import com.unnamedgroup.tourapp.model.rest.TripREST
import retrofit2.Call
import retrofit2.http.GET

interface TripsService {
    @GET("trips")
    fun getTrips(): Call<MutableList<TripREST>>
}
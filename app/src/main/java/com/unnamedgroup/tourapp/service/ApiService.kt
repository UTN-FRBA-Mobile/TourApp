package com.unnamedgroup.tourapp.service

import com.unnamedgroup.tourapp.model.rest.TicketREST
import com.unnamedgroup.tourapp.model.rest.TripREST
import com.unnamedgroup.tourapp.model.rest.UserREST
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("trips")
    fun getTrips(): Call<MutableList<TripREST>>

    @GET("tickets")
    fun getTicketsByUser(@Query("user.id") userId: Int): Call<MutableList<TicketREST>>

    @GET("user")
    fun getLogin(@Query("email") email: String, @Query("password") password: String): Call<UserREST>
}
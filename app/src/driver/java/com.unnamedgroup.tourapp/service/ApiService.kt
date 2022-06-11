package com.unnamedgroup.tourapp.service

import com.unnamedgroup.tourapp.model.rest.NewUserREST
import com.unnamedgroup.tourapp.model.rest.TicketREST
import com.unnamedgroup.tourapp.model.rest.TripREST
import com.unnamedgroup.tourapp.model.rest.UserREST
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("trips")
    fun getTrips(): Call<MutableList<TripREST>>

    @GET("drivers")
    fun getLogin(
        @Query("email") email: String,
        @Query("password") password: String
    ): Call<MutableList<UserREST>>

    @GET("users")
    fun getUserByDNI(
        @Query("dni") dni: String
    ): Call<MutableList<UserREST>>

    @GET("users")
    fun getUserByEmail(
        @Query("email") email: String
    ): Call<MutableList<UserREST>>

    @Headers(
        "Content-Type: application/json",
        "Accept: application/json"
    )
    @POST("users")
    fun createUser(@Body user: NewUserREST): Call<UserREST>

    @GET("tickets")
    fun getTicketsByTrip(@Query("trip.id") tripId: Int): Call<MutableList<TicketREST>>

}
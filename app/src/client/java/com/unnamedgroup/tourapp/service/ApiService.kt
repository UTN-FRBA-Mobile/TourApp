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

    @GET("tickets")
    fun getTicketsByUser(@Query("user.id") userId: Int): Call<MutableList<TicketREST>>

    @GET("users")
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

    @GET("users/{id}")
    fun getUser(@Path("id") id: Int): Call<UserREST>

    @Headers(
        "Content-Type: application/json",
        "Accept: application/json"
    )
    @POST("users")
    fun createUser(@Body user: NewUserREST): Call<UserREST>

    @PUT("tickets/{id}")
    fun modifyTicket(@Path("id") ticketId: Int , @Body newTicket: TicketREST): Call<TicketREST>

    @POST("tickets")
    fun addTicket(@Body newTicket: TicketREST): Call<TicketREST>


    @GET("tickets?_sort=trip.date,trip.departureTime&_order=desc,desc&_limit=1")
    fun getLastTicketByUser(@Query("user.id") userId: Int): Call<MutableList<TicketREST>>

    @GET("trips")
    fun getTrips(@Query("origin") origin: String, @Query("destination") destination: String): Call<MutableList<TripREST>>

    @GET("trips")
    fun getTrips(@Query("origin") origin: String, @Query("destination") destination: String, @Query("date") date: String): Call<MutableList<TripREST>>
}
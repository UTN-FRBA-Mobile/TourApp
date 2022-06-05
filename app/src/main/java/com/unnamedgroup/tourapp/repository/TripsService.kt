package com.unnamedgroup.tourapp.repository

import com.unnamedgroup.tourapp.model.business.Ticket
import com.unnamedgroup.tourapp.model.rest.TicketREST
import com.unnamedgroup.tourapp.model.rest.TripREST
import retrofit2.Call
import retrofit2.http.*

interface TripsService {
    @GET("trips")
    fun getTrips(): Call<MutableList<TripREST>>

    @GET("tickets")
    fun getTicketsByUser(@Query("user.id") userId: Int): Call<MutableList<TicketREST>>

    @PUT("tickets/{id}")
    fun modifyTicket(@Path("id") ticketId: Int , @Body newTicket: TicketREST): Call<TicketREST>
}
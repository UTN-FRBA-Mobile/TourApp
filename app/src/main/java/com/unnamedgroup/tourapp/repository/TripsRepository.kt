package com.unnamedgroup.tourapp.repository

import com.unnamedgroup.tourapp.BuildConfig
import com.unnamedgroup.tourapp.model.business.Ticket
import com.unnamedgroup.tourapp.model.business.Trip
import com.unnamedgroup.tourapp.model.business.TripPassenger
import com.unnamedgroup.tourapp.model.rest.TicketREST
import com.unnamedgroup.tourapp.model.rest.TripREST
import com.unnamedgroup.tourapp.presenter.interfaces.GetTripsPresenterInt
import com.unnamedgroup.tourapp.presenter.interfaces.MyTripsPresenterInt
import com.unnamedgroup.tourapp.presenter.interfaces.TripDetailsPresenterInt
import com.unnamedgroup.tourapp.presenter.interfaces.TripsPresenterInt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TripsRepository() {

    private val service = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BuildConfig.API_URL)
        .build()
        .create(TripsService::class.java)

    fun getTrips(presenter: GetTripsPresenterInt) {
        service.getTrips().enqueue(object : Callback<MutableList<TripREST>> {
            override fun onResponse(
                call: Call<MutableList<TripREST>>,
                response: Response<MutableList<TripREST>>
            ) {
                val respList: MutableList<TripREST> = response.body()!!
                val trips: MutableList<Trip> = mutableListOf()
                for (r in respList) {
                    trips.add(r.toTrip())
                }
                presenter.onGetTripsOk(trips)
            }

            override fun onFailure(call: Call<MutableList<TripREST>>, t: Throwable) {
                presenter.onGetTripsFailed(t.toString())
            }
        })
    }

    fun getTicketsByUser(presenter: MyTripsPresenterInt, userId: Int) {
        service.getTicketsByUser(userId).enqueue(object : Callback<MutableList<TicketREST>> {
            override fun onResponse(
                call: Call<MutableList<TicketREST>>,
                response: Response<MutableList<TicketREST>>
            ) {
                val respList: MutableList<TicketREST> = response.body()!!
                val tickets: MutableList<Ticket> = mutableListOf()
                for (r in respList) {
                    tickets.add(r.toTicket())
                }
                presenter.onGetTicketsByUserOk(tickets)
            }

            override fun onFailure(call: Call<MutableList<TicketREST>>, t: Throwable) {
                presenter.onGetTicketsByUserFailed(t.toString())
            }
        })
    }

    fun modifyTicket(presenter: TripDetailsPresenterInt, ticketId :Int, newTicket: TicketREST) {
        service.modifyTicket(ticketId, newTicket).enqueue(object : Callback<TicketREST> {
            override fun onResponse(call: Call<TicketREST>, response: Response<TicketREST>) {
                val resp: TicketREST = response.body()!!
                val ticket: Ticket = resp.toTicket()
                presenter.onModifyTicketOk(ticket)
            }

            override fun onFailure(call: Call<TicketREST>, t: Throwable) {
                presenter.onModifyTicketFailed(t.toString())

            }
        })
    }

    fun getLastTicketByUser(presenter: TripsPresenterInt, userId: Int) {
        service.getLastTicketByUser(userId).enqueue(object : Callback<MutableList<TicketREST>> {
            override fun onResponse(
                call: Call<MutableList<TicketREST>>,
                response: Response<MutableList<TicketREST>>
            ) {
                val resp: MutableList<TicketREST> = response.body()!!
                presenter.onGetLastTicketByUserOk(resp.get(0).toTicket())
            }

            override fun onFailure(call: Call<MutableList<TicketREST>>, t: Throwable) {
                presenter.onGetLastTicketByUserFailed(t.toString())
            }
        })
    }

    fun getTicketsByTrip(presenter: MyTripsPresenterInt, tripId: Int) {
        service.getTicketsByTrip(tripId).enqueue(object : Callback<MutableList<TicketREST>> {
            override fun onResponse(
                call: Call<MutableList<TicketREST>>,
                response: Response<MutableList<TicketREST>>
            ) {
                val respList: MutableList<TicketREST> = response.body()!!
                val tripPassengers: MutableList<TripPassenger> = mutableListOf()
                for (r in respList) {
                    for (p in r.toTicket().passengers){
                        tripPassengers.add(TripPassenger(p.name, p.dni, r.toTicket().busBoarding, r.toTicket().busStop, p.busBoarded))
                    }
                }
                presenter.onGetTicketsByTripOk(tripPassengers)
            }

            override fun onFailure(call: Call<MutableList<TicketREST>>, t: Throwable) {
                presenter.onGetTicketsByUserFailed(t.toString())
            }
        })
    }

}
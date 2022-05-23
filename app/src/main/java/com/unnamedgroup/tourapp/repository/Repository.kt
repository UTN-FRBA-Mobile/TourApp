package com.unnamedgroup.tourapp.repository

import com.unnamedgroup.tourapp.BuildConfig
import com.unnamedgroup.tourapp.model.business.Ticket
import com.unnamedgroup.tourapp.model.business.Trip
import com.unnamedgroup.tourapp.model.rest.TicketREST
import com.unnamedgroup.tourapp.model.rest.TripREST
import com.unnamedgroup.tourapp.model.rest.UserREST
import com.unnamedgroup.tourapp.presenter.interfaces.GetTripsPresenterInt
import com.unnamedgroup.tourapp.presenter.interfaces.LoginPresenterInt
import com.unnamedgroup.tourapp.presenter.interfaces.MyTripsPresenterInt
import com.unnamedgroup.tourapp.service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Repository() {

    private val service = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BuildConfig.API_URL)
        .build()
        .create(ApiService::class.java)

    fun getTrips(presenter: GetTripsPresenterInt) {
        service.getTrips().enqueue(object : Callback<MutableList<TripREST>> {
            override fun onResponse(
                call: Call<MutableList<TripREST>>,
                response: Response<MutableList<TripREST>>
            ) {
                if (response.isSuccessful) {
                    val respList: MutableList<TripREST> = response.body()!!
                    val trips: MutableList<Trip> = mutableListOf()
                    for (r in respList) {
                        trips.add(r.toTrip())
                    }
                    presenter.onGetTripsOk(trips)
                } else {
                    presenter.onGetTripsFailed(response.message())
                }
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
                if (response.isSuccessful) {
                    val respList: MutableList<TicketREST> = response.body()!!
                    val tickets: MutableList<Ticket> = mutableListOf()
                    for (r in respList) {
                        tickets.add(r.toTicket())
                    }
                    presenter.onGetTicketsByUserOk(tickets)
                } else {
                    presenter.onGetTicketsByUserFailed(response.message())
                }
            }
            override fun onFailure(call: Call<MutableList<TicketREST>>, t: Throwable) {
                presenter.onGetTicketsByUserFailed(t.toString())
            }
        })
    }

    fun getLogin(presenter: LoginPresenterInt, email: String, password: String) {
        service.getLogin(email, password).enqueue(object : Callback<UserREST> {
            override fun onResponse(call: Call<UserREST>, response: Response<UserREST>) {
                if (response.isSuccessful) {
                    presenter.onGetLoginOk(response.body()!!.toUser())
                } else {
                    presenter.onGetLoginFailed("Credenciales incorrectas")
                }
            }
            override fun onFailure(call: Call<UserREST>, t: Throwable) {
                presenter.onGetLoginFailed(t.toString())
            }
        })
    }
}
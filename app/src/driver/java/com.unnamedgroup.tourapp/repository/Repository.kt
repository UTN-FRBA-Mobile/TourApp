package com.unnamedgroup.tourapp.repository

import com.unnamedgroup.tourapp.BuildConfig
import com.unnamedgroup.tourapp.model.business.Ticket
import com.unnamedgroup.tourapp.model.business.Trip
import com.unnamedgroup.tourapp.model.rest.NewUserREST
import com.unnamedgroup.tourapp.model.rest.TicketREST
import com.unnamedgroup.tourapp.model.rest.TripREST
import com.unnamedgroup.tourapp.model.rest.UserREST
import com.unnamedgroup.tourapp.presenter.interfaces.GetTripsPresenterInt
import com.unnamedgroup.tourapp.presenter.interfaces.LoginPresenterInt
import com.unnamedgroup.tourapp.presenter.interfaces.MyTripsPresenterInt
import com.unnamedgroup.tourapp.presenter.interfaces.RegisterPresenterInt
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

    fun getLogin(presenter: LoginPresenterInt, email: String, password: String) {
        service.getLogin(email, password).enqueue(object : Callback<MutableList<UserREST>> {
            override fun onResponse(
                call: Call<MutableList<UserREST>>,
                response: Response<MutableList<UserREST>>
            ) {
                if (response.isSuccessful && response.body()!!.isNotEmpty()) {
                    presenter.onGetLoginOk(response.body()!![0].toUser())
                } else {
                    presenter.onGetLoginFailed("Credenciales incorrectas")
                }
            }

            override fun onFailure(call: Call<MutableList<UserREST>>, t: Throwable) {
                presenter.onGetLoginFailed(t.toString())
            }
        })
    }

    fun registerUser(
        presenter: RegisterPresenterInt,
        name: String,
        email: String,
        password: String,
        dni: String
    ) {
        checkUserDNI(presenter, name, email, password, dni)
    }

    private fun checkUserDNI(
        presenter: RegisterPresenterInt,
        name: String,
        email: String,
        password: String,
        dni: String
    ) {
        service.getUserByDNI(dni).enqueue(object : Callback<MutableList<UserREST>> {
            override fun onResponse(
                call: Call<MutableList<UserREST>>,
                response: Response<MutableList<UserREST>>
            ) {
                if (response.isSuccessful && response.body()!!.size == 0) {
                    checkUserEmail(presenter, name, email, password, dni)
                } else {
                    presenter.onRegisterUserFailed("Ya exite un usuario con ese DNI")
                }
            }

            override fun onFailure(call: Call<MutableList<UserREST>>, t: Throwable) {
                presenter.onRegisterUserFailed(t.toString())
            }
        })
    }

    private fun checkUserEmail(
        presenter: RegisterPresenterInt,
        name: String,
        email: String,
        password: String,
        dni: String
    ) {
        service.getUserByEmail(email).enqueue(object : Callback<MutableList<UserREST>> {
            override fun onResponse(
                call: Call<MutableList<UserREST>>,
                response: Response<MutableList<UserREST>>
            ) {
                if (response.isSuccessful && response.body()!!.size == 0) {
                    registerUserAfterChecks(presenter, name, email, password, dni)
                } else {
                    presenter.onRegisterUserFailed("Ya exite un usuario con ese email")
                }
            }

            override fun onFailure(call: Call<MutableList<UserREST>>, t: Throwable) {
                presenter.onRegisterUserFailed(t.toString())
            }
        })
    }

    private fun registerUserAfterChecks(
        presenter: RegisterPresenterInt,
        name: String,
        email: String,
        password: String,
        dni: String
    ) {
        val newUser = NewUserREST(name, email, dni, password, "asdasdasd")
        service.createUser(newUser).enqueue(object : Callback<UserREST> {
            override fun onResponse(call: Call<UserREST>, response: Response<UserREST>) {
                if (response.isSuccessful) {
                    presenter.onRegisterUserOk(response.body()!!.toUser())
                } else {
                    presenter.onRegisterUserFailed("Credenciales incorrectas")
                }
            }

            override fun onFailure(call: Call<UserREST>, t: Throwable) {
                presenter.onRegisterUserFailed(t.toString())
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
                val tickets: MutableList<Ticket> = mutableListOf()
                for (r in respList) {
                    tickets.add(r.toTicket())
                }
                presenter.onGetTicketsByTripOk(tickets)
            }

            override fun onFailure(call: Call<MutableList<TicketREST>>, t: Throwable) {
                presenter.onGetTicketsByTripFailed(t.toString())
            }
        })
    }

    fun saveTrip(presenter: MyTripsPresenterInt, tripId: Int, trip: TripREST) {
        service.saveTrip(tripId, trip).enqueue(object : Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                if (response.isSuccessful) {
                    presenter.onSaveTripOk()
                } else {
                    presenter.onSaveTripError(response.message())
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                presenter.onSaveTripError(t.toString())
            }
        })
    }

    fun saveTickets(presenter: MyTripsPresenterInt, tickets: MutableList<Ticket>) {

        try {
            for (ticket: Ticket in tickets) {
                service.saveTicket(ticket.id!!, ticket.toRest()).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {}
                    override fun onFailure(call: Call<Void>, t: Throwable) {}
                })
            }
        } catch (e: Exception) {
            presenter.onSaveTicketError(e.toString())
        }


    }


}
package com.unnamedgroup.tourapp.repository

import com.unnamedgroup.tourapp.BuildConfig
import com.unnamedgroup.tourapp.model.business.Ticket
import com.unnamedgroup.tourapp.model.business.Trip
import com.unnamedgroup.tourapp.model.rest.NewUserREST
import com.unnamedgroup.tourapp.model.rest.TicketREST
import com.unnamedgroup.tourapp.model.rest.TripREST
import com.unnamedgroup.tourapp.model.rest.UserREST
import com.unnamedgroup.tourapp.presenter.interfaces.*
import com.unnamedgroup.tourapp.service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

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
        service.getLogin(email, password).enqueue(object : Callback<MutableList<UserREST>> {
            override fun onResponse(call: Call<MutableList<UserREST>>, response: Response<MutableList<UserREST>>) {
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

    fun registerUser(presenter: RegisterPresenterInt, name: String, email: String, password: String, dni: String) {
        checkUserDNI(presenter, name, email, password, dni)
    }

    private fun checkUserDNI(presenter: RegisterPresenterInt, name: String, email: String, password: String, dni: String) {
        service.getUserByDNI(dni).enqueue(object : Callback<MutableList<UserREST>> {
            override fun onResponse(call: Call<MutableList<UserREST>>, response: Response<MutableList<UserREST>>) {
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

    private fun checkUserEmail(presenter: RegisterPresenterInt, name: String, email: String, password: String, dni: String) {
        service.getUserByEmail(email).enqueue(object : Callback<MutableList<UserREST>> {
            override fun onResponse(call: Call<MutableList<UserREST>>, response: Response<MutableList<UserREST>>) {
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

    private fun registerUserAfterChecks(presenter: RegisterPresenterInt, name: String, email: String, password: String, dni: String) {
        val newUser = NewUserREST(name, email, dni, password,"asdasdasd")
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

    fun getUserById(presenter: NewTripPresenterInt,id: Int) {
        service.getUser(id).enqueue(object : Callback<UserREST> {
            override fun onResponse(call: Call<UserREST>, response: Response<UserREST>) {
                if (response.isSuccessful) {
                    presenter.onGetUserByIdOk(response.body()!!.toUser())
                } else {
                    presenter.onGetUserByIdFailed("Credenciales incorrectas")
                }
            }
            override fun onFailure(call: Call<UserREST>, t: Throwable) {
                presenter.onGetUserByIdFailed(t.toString())
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


    fun modifyTrip(presenter: ConfirmPresenterInt, tripId :Int, newTrip: TripREST) {
        service.modifyTrip(tripId, newTrip).enqueue(object : Callback<TripREST> {
            override fun onResponse(call: Call<TripREST>, response: Response<TripREST>) {
                val resp: TripREST = response.body()!!
                val trip: Trip = resp.toTrip()
                presenter.onModifyTripOk(trip)
            }

            override fun onFailure(call: Call<TripREST>, t: Throwable) {
                presenter.onModifyTripFailed(t.toString())

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

    fun addTicket(presenter: TripDetailsPresenterInt, newTicket: TicketREST) {
        service.addTicket(newTicket).enqueue(object : Callback<TicketREST> {
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

    fun getTripsByOriginAndDestination(presenter: NewTripPresenterInt, origin: String, destination: String) {
        service.getTrips(origin, destination).enqueue(object : Callback<MutableList<TripREST>> {
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
                    presenter.onGetTripsByOriginAndDestinationOk(trips)
                } else {
                    presenter.onGetTripsByOriginAndDestinationFailed(response.message())
                }
            }
            override fun onFailure(call: Call<MutableList<TripREST>>, t: Throwable) {
                presenter.onGetTripsByOriginAndDestinationFailed(t.toString())
            }
        })
    }

    fun getTripsByOriginAndDestinationAndDate(presenter: NewTripPresenterInt, origin: String, destination: String, date: String) {
        service.getTrips(origin, destination, date).enqueue(object : Callback<MutableList<TripREST>> {
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
                    presenter.onGetTripsByOriginAndDestinationAndDateOk(trips)
                } else {
                    presenter.onGetTripsByOriginAndDestinationAndDateFailed(response.message())
                }
            }
            override fun onFailure(call: Call<MutableList<TripREST>>, t: Throwable) {
                presenter.onGetTripsByOriginAndDestinationAndDateFailed(t.toString())
            }
        })
    }
}
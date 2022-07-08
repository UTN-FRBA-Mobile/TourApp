package com.unnamedgroup.tourapp.repository

import android.util.Log
import com.unnamedgroup.tourapp.BuildConfig
import com.unnamedgroup.tourapp.model.business.Ticket
import com.unnamedgroup.tourapp.model.business.Trip
import com.unnamedgroup.tourapp.model.rest.*
import com.unnamedgroup.tourapp.presenter.interfaces.GetTripsPresenterInt
import com.unnamedgroup.tourapp.presenter.interfaces.LoginPresenterInt
import com.unnamedgroup.tourapp.presenter.interfaces.MyTripsPresenterInt
import com.unnamedgroup.tourapp.presenter.interfaces.RegisterPresenterInt
import com.unnamedgroup.tourapp.service.ApiService
import com.unnamedgroup.tourapp.service.PNService
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class Repository {

    private val service = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BuildConfig.API_URL)
        .build()
        .create(ApiService::class.java)

    private val pnService = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BuildConfig.PN_URL)
        .build()
        .create(PNService::class.java)

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

    fun saveTicket(presenter: MyTripsPresenterInt, ticket: Ticket, passengerPosition: Int, newValue: Boolean) {
        service.saveTicket(ticket.id!!, ticket.toRest()).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    presenter.onSaveTicketOk(passengerPosition, newValue)
                } else {
                    presenter.onSaveTicketError(response.message(), passengerPosition, newValue)
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                presenter.onSaveTicketError(t.toString(), passengerPosition, newValue)
            }
        })
    }

    fun generatePushNotification(presenter: MyTripsPresenterInt, tripId: Int, newState: String) {
        val pnBody = PushNotificationREST("/topics/trip$tripId", PushNotificationDataREST(1, tripId, newState))
        pnService.createTripStateChangePN(pnBody).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (!response.isSuccessful) {
                    Log.d("PN", "Cannot create PN with Trip Change. Error: " + response.message())
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("PN", "Cannot create PN with Trip Change")
            }
        })
    }


}
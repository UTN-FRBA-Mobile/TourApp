package com.unnamedgroup.tourapp.repository

import com.unnamedgroup.tourapp.model.business.Trip
import com.unnamedgroup.tourapp.model.rest.TripREST
import com.unnamedgroup.tourapp.presenter.interfaces.MyTripsPresenterInt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TripsRepository() {

    private val service = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("http://192.168.0.218:3000/")
        .build()
        .create(TripsService::class.java)

    fun getTrips(presenter: MyTripsPresenterInt) {
        service.getTrips().enqueue(object: Callback<MutableList<TripREST>> {
            override fun onResponse(call: Call<MutableList<TripREST>>, response: Response<MutableList<TripREST>>) {
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

}
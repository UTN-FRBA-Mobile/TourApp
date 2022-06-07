package com.unnamedgroup.tourapp.presenter.interfaces

import com.unnamedgroup.tourapp.model.business.TripPassenger
import com.unnamedgroup.tourapp.utils.BaseView

interface MyTripsPresenterInt {

    fun getPassengersByTrip(tripId: Int)
    fun onGetPassengersByTripOk(passengers: MutableList<TripPassenger>)
    fun onGetPassengersByTripFailed(error: String)

    interface View: BaseView {
        fun onGetPassengersByTripOk(passengers: MutableList<TripPassenger>)
        fun onGetPassengersByTripFailed(error: String)
    }

}
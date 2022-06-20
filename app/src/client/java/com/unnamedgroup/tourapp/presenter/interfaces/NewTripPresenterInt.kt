package com.unnamedgroup.tourapp.presenter.interfaces

import com.unnamedgroup.tourapp.model.business.Trip
import com.unnamedgroup.tourapp.utils.BaseView

interface NewTripPresenterInt {

    fun getTripsByOriginAndDestination(origin: String, destination: String)
    fun getTripsByOriginAndDestinationAndDate(origin: String, destination: String, date: String)
    fun onGetTripsByOriginAndDestinationOk(trips: MutableList<Trip>)
    fun onGetTripsByOriginAndDestinationFailed(error: String)
    fun onGetTripsByOriginAndDestinationAndDateOk(trips: MutableList<Trip>)
    fun onGetTripsByOriginAndDestinationAndDateFailed(error: String)

    interface View: BaseView {
        fun onGetTripsByOriginAndDestinationOk(trips: MutableList<Trip>)
        fun onGetTripsByOriginAndDestinationFailed(error: String)
        fun onGetTripsByOriginAndDestinationAndDateOk(trips: MutableList<Trip>)
        fun onGetTripsByOriginAndDestinationAndDateFailed(error: String)
    }

}
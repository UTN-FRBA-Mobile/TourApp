package com.unnamedgroup.tourapp.presenter.interfaces

import com.unnamedgroup.tourapp.model.business.Trip
import com.unnamedgroup.tourapp.utils.BaseView

interface GetTripsPresenterInt {

    fun getTrips()
    fun onGetTripsOk(trips: MutableList<Trip>)
    fun onGetTripsFailed(error: String)

    interface View: BaseView {
        fun onGetTripsOk(trips : MutableList<Trip>)
        fun onGetTripsError(error : String)
    }

}
package com.unnamedgroup.tourapp.presenter.interfaces

import com.unnamedgroup.tourapp.model.Trip
import com.unnamedgroup.tourapp.utils.BaseView

interface MyTripsPresenterInt {

    fun getTrips()

    interface View: BaseView {
        fun onGetTripsOk(trips : MutableList<Trip>)
        fun onGetTripsError(error : String)
    }

}
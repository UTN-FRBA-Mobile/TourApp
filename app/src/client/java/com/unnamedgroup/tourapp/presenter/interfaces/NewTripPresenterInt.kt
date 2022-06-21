package com.unnamedgroup.tourapp.presenter.interfaces

import com.unnamedgroup.tourapp.model.business.Trip
import com.unnamedgroup.tourapp.model.business.User
import com.unnamedgroup.tourapp.utils.BaseView

interface NewTripPresenterInt {

    fun getUserById(id: Int)
    fun onGetUserByIdOk(user: User)
    fun onGetUserByIdFailed(error: String)
    fun getTripsByOriginAndDestination(origin: String, destination: String)
    fun onGetTripsByOriginAndDestinationOk(trips: MutableList<Trip>)
    fun onGetTripsByOriginAndDestinationFailed(error: String)
    fun getTripsByOriginAndDestinationAndDate(origin: String, destination: String, date: String)
    fun onGetTripsByOriginAndDestinationAndDateOk(trips: MutableList<Trip>)
    fun onGetTripsByOriginAndDestinationAndDateFailed(error: String)

    interface View: BaseView {
        fun onGetUserByIdOk(user: User)
        fun onGetUserByIdFailed(error: String)
        fun onGetTripsByOriginAndDestinationOk(trips: MutableList<Trip>)
        fun onGetTripsByOriginAndDestinationFailed(error: String)
        fun onGetTripsByOriginAndDestinationAndDateOk(trips: MutableList<Trip>)
        fun onGetTripsByOriginAndDestinationAndDateFailed(error: String)
    }

}
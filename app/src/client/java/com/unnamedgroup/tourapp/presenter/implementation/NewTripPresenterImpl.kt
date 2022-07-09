package com.unnamedgroup.tourapp.presenter.implementation

import com.unnamedgroup.tourapp.model.business.Ticket
import com.unnamedgroup.tourapp.model.business.Trip
import com.unnamedgroup.tourapp.model.business.User
import com.unnamedgroup.tourapp.presenter.interfaces.NewTripPresenterInt
import com.unnamedgroup.tourapp.presenter.interfaces.TripsPresenterInt
import com.unnamedgroup.tourapp.repository.Repository
import com.unnamedgroup.tourapp.utils.AbstractPresenter

class NewTripPresenterImpl(private val mView: NewTripPresenterInt.View) : NewTripPresenterInt, AbstractPresenter<NewTripPresenterInt.View>(
    mView
) {
    override fun getUserById(id: Int) {
        Repository().getUserById(this, id)
    }

    override fun onGetUserByIdOk(user: User) {
        mView.onGetUserByIdOk(user)
    }

    override fun onGetUserByIdFailed(error: String) {
        mView.onGetUserByIdFailed(error)
    }

    override fun getTripsByOriginAndDestination(origin: String, destination: String) {
        Repository().getTripsByOriginAndDestination(this, origin, destination)
    }

    override fun getTripsByOriginAndDestinationAndDate(origin: String, destination: String, date: String) {
        Repository().getTripsByOriginAndDestinationAndDate(this, origin, destination, date)
    }

    override fun getTripsByOriginAndDestinationAndDateAndTime(origin: String, destination: String, date: String, departureTime: String) {
        Repository().getTripsByOriginAndDestinationAndDateAndTime(this, origin, destination, date, departureTime)
    }

    override fun getRoundTrip(origin: String, destination: String) {
        Repository().getRoundTrip(this, origin, destination)
    }

    override fun onGetTripsByOriginAndDestinationOk(trips: MutableList<Trip>) {
        mView.onGetTripsByOriginAndDestinationOk(trips)
    }

    override fun onGetTripsByOriginAndDestinationFailed(error: String) {
        mView.onGetTripsByOriginAndDestinationFailed(error)
    }

    override fun onGetTripsByOriginAndDestinationAndDateOk(trips: MutableList<Trip>) {
        mView.onGetTripsByOriginAndDestinationAndDateOk(trips)
    }

    override fun onGetTripsByOriginAndDestinationAndDateFailed(error: String) {
        mView.onGetTripsByOriginAndDestinationAndDateFailed(error)
    }

    override fun onGetTripsByOriginAndDestinationAndDateAndTimeOk(trips: MutableList<Trip>) {
        mView.onGetTripsByOriginAndDestinationAndDateAndTimeOk(trips)
    }

    override fun onGetTripsByOriginAndDestinationAndDateAndTimeFailed(error: String) {
        mView.onGetTripsByOriginAndDestinationAndDateAndTimeFailed(error)
    }

    override fun onGetRoundTripOk(trips: MutableList<Trip>) {
        mView.onGetRoundTripOk(trips)
    }

    override fun onGetRoundTripFailed(error: String) {
        mView.onGetRoundTripFailed(error)
    }

}
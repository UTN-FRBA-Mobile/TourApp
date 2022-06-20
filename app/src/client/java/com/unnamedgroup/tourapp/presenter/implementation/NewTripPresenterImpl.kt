package com.unnamedgroup.tourapp.presenter.implementation

import com.unnamedgroup.tourapp.model.business.Ticket
import com.unnamedgroup.tourapp.model.business.Trip
import com.unnamedgroup.tourapp.presenter.interfaces.NewTripPresenterInt
import com.unnamedgroup.tourapp.presenter.interfaces.TripsPresenterInt
import com.unnamedgroup.tourapp.repository.Repository
import com.unnamedgroup.tourapp.utils.AbstractPresenter

class NewTripPresenterImpl(private val mView: NewTripPresenterInt.View) : NewTripPresenterInt, AbstractPresenter<NewTripPresenterInt.View>(
    mView
) {

    override fun getTripsByOriginAndDestination(origin: String, destination: String) {
        Repository().getTripsByOriginAndDestination(this, origin, destination)
    }

    override fun getTripsByOriginAndDestinationAndDate(origin: String, destination: String, date: String) {
        Repository().getTripsByOriginAndDestinationAndDate(this, origin, destination, date)
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

}
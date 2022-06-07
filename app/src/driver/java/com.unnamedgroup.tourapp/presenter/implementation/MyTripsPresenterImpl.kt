package com.unnamedgroup.tourapp.presenter.implementation

import com.unnamedgroup.tourapp.model.business.TripPassenger
import com.unnamedgroup.tourapp.presenter.interfaces.MyTripsPresenterInt
import com.unnamedgroup.tourapp.repository.Repository
import com.unnamedgroup.tourapp.utils.AbstractPresenter

class MyTripsPresenterImpl(private val mView: MyTripsPresenterInt.View) : MyTripsPresenterInt, AbstractPresenter<MyTripsPresenterInt.View>(
    mView
) {

    override fun getPassengersByTrip(tripId: Int) {
        Repository().getTicketsByTrip(this, tripId)
    }

    override fun onGetPassengersByTripOk(passengers: MutableList<TripPassenger>) {
        mView.onGetPassengersByTripOk(passengers)
    }

    override fun onGetPassengersByTripFailed(error: String) {
        mView.onGetPassengersByTripFailed(error)
    }
}
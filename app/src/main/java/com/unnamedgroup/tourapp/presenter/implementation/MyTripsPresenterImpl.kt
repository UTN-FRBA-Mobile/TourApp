package com.unnamedgroup.tourapp.presenter.implementation

import com.unnamedgroup.tourapp.model.business.Ticket
import com.unnamedgroup.tourapp.model.business.TripPassenger
import com.unnamedgroup.tourapp.presenter.interfaces.MyTripsPresenterInt
import com.unnamedgroup.tourapp.repository.Repository
import com.unnamedgroup.tourapp.repository.TripsRepository
import com.unnamedgroup.tourapp.utils.AbstractPresenter

class MyTripsPresenterImpl(private val mView: MyTripsPresenterInt.View) : MyTripsPresenterInt, AbstractPresenter<MyTripsPresenterInt.View>(
    mView
) {

    override fun getTicketsByUser(userId: Int) {
        Repository().getTicketsByUser(this, userId)
    }

    override fun onGetTicketsByUserOk(tickets: MutableList<Ticket>) {
        mView.onGetTicketsByUserOk(tickets)
    }

    override fun onGetTicketsByUserFailed(error: String) {
        mView.onGetTicketsByUserFailed(error)
    }

    override fun getTicketsByTrip(tripId: Int) {
        TripsRepository().getTicketsByTrip(this, tripId)
    }

    override fun onGetTicketsByTripOk(tickets: MutableList<TripPassenger>) {
        mView.onGetTicketsByTripOk(tickets)
    }

    override fun onGetTicketsByTripFailed(error: String) {
        mView.onGetTicketsByTripFailed(error)
    }
}
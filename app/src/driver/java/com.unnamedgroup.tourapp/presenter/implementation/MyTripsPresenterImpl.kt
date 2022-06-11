package com.unnamedgroup.tourapp.presenter.implementation

import com.unnamedgroup.tourapp.model.business.Passenger
import com.unnamedgroup.tourapp.model.business.Ticket
import com.unnamedgroup.tourapp.model.business.Trip
import com.unnamedgroup.tourapp.model.business.TripPassenger
import com.unnamedgroup.tourapp.presenter.interfaces.MyTripsPresenterInt
import com.unnamedgroup.tourapp.repository.Repository
import com.unnamedgroup.tourapp.utils.AbstractPresenter

class MyTripsPresenterImpl(private val mView: MyTripsPresenterInt.View) : MyTripsPresenterInt, AbstractPresenter<MyTripsPresenterInt.View>(
    mView
) {

    override fun getTicketsByTrip(tripId: Int) {
        Repository().getTicketsByTrip(this, tripId)
    }

    override fun onGetTicketsByTripOk(tickets: MutableList<Ticket>) {
        val passengers: MutableList<TripPassenger> = mutableListOf()
        for (ticket: Ticket in tickets) {
            for (passenger: Passenger in ticket.passengers)
            passengers.add(TripPassenger(passenger.name, passenger.dni, ticket.busBoarding, ticket.busStop, passenger.busBoarded))
        }
        mView.onGetTicketsByTripOk(tickets, passengers)
    }

    override fun onGetTicketsByTripFailed(error: String) {
        mView.onGetTicketsByTripFailed(error)
    }

    override fun saveTrip(trip: Trip) {
        Repository().saveTrip(this, trip.id, trip.toRest())
    }

    override fun onSaveTripOk() {
        mView.onSaveTripOk()
    }

    override fun onSaveTripError(error: String) {
        mView.onSaveTripError(error)
    }

    override fun saveTickets(tickets: MutableList<Ticket>) {
        Repository().saveTickets(this, tickets)
    }

    override fun onSaveTicketOk() {
        mView.onSaveTicketOk()
    }

    override fun onSaveTicketError(error: String) {
        mView.onSaveTicketError(error)
    }
}
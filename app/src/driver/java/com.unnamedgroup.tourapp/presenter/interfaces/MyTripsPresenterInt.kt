package com.unnamedgroup.tourapp.presenter.interfaces

import com.unnamedgroup.tourapp.model.business.Ticket
import com.unnamedgroup.tourapp.model.business.Trip
import com.unnamedgroup.tourapp.model.business.TripPassenger
import com.unnamedgroup.tourapp.utils.BaseView

interface MyTripsPresenterInt {

    fun getTicketsByTrip(tripId: Int)
    fun onGetTicketsByTripOk(tickets: MutableList<Ticket>)
    fun onGetTicketsByTripFailed(error: String)

    fun saveTrip(trip: Trip)
    fun onSaveTripOk()
    fun onSaveTripError(error: String)

    fun saveTickets(tickets: MutableList<Ticket>, passengers: MutableList<TripPassenger>)
    fun onSaveTicketOk()
    fun onSaveTicketError(error: String)

    interface View: BaseView {
        fun onGetTicketsByTripOk(tickets: MutableList<Ticket>, passengers: MutableList<TripPassenger>)
        fun onGetTicketsByTripFailed(error: String)

        fun onSaveTripOk()
        fun onSaveTripError(error: String)

        fun onSaveTicketOk()
        fun onSaveTicketError(error: String)
    }

}
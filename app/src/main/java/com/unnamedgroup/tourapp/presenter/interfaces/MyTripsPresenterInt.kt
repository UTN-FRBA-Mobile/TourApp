package com.unnamedgroup.tourapp.presenter.interfaces

import com.unnamedgroup.tourapp.model.business.Ticket
import com.unnamedgroup.tourapp.model.business.TripPassenger
import com.unnamedgroup.tourapp.utils.BaseView

interface MyTripsPresenterInt {

    fun getTicketsByUser(userId: Int)
    fun getTicketsByTrip(tripId: Int)
    fun onGetTicketsByUserOk(tickets: MutableList<Ticket>)
    fun onGetTicketsByUserFailed(error: String)
    fun onGetTicketsByTripOk(tickets: MutableList<TripPassenger>)
    fun onGetTicketsByTripFailed(error: String)

    interface View: BaseView {
        fun onGetTicketsByUserOk(tickets: MutableList<Ticket>)
        fun onGetTicketsByUserFailed(error: String)
        fun onGetTicketsByTripOk(tickets: MutableList<TripPassenger>)
        fun onGetTicketsByTripFailed(error: String)
    }

}
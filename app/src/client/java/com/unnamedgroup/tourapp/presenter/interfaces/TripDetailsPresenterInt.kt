package com.unnamedgroup.tourapp.presenter.interfaces

import com.unnamedgroup.tourapp.model.business.Ticket
import com.unnamedgroup.tourapp.utils.BaseView

interface TripDetailsPresenterInt {

    fun modifyTicket(newTicket: Ticket)
    fun onModifyTicketOk(ticket: Ticket)
    fun addTicket(newTicket: Ticket)
    fun onModifyTicketFailed(error: String)
    fun getTicketByTripId(tripId: Int)
    fun onGetTicketByTripIdOk(ticket: Ticket)
    fun onGetTicketByTripIdFailed(error: String)

    interface View: BaseView {
        fun onModifyTicketOk(ticket: Ticket)
        fun onModifyTicketFailed(error: String)
        fun getTicketByTripIdOk(ticket: Ticket)
        fun getTicketByTripIdFailed(error: String)
    }

}
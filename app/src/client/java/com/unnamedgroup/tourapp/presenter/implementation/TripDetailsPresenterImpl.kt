package com.unnamedgroup.tourapp.presenter.implementation

import com.unnamedgroup.tourapp.model.business.Ticket
import com.unnamedgroup.tourapp.presenter.interfaces.TripDetailsPresenterInt
import com.unnamedgroup.tourapp.repository.Repository
import com.unnamedgroup.tourapp.utils.AbstractPresenter

class TripDetailsPresenterImpl(private val mView: TripDetailsPresenterInt.View) : TripDetailsPresenterInt, AbstractPresenter<TripDetailsPresenterInt.View>(
    mView
) {

    override fun modifyTicket(newTicket: Ticket) {
        Repository().modifyTicket(this, newTicket.id!!, newTicket.toRest())
    }

    override fun addTicket(newTicket: Ticket) {
        Repository().addTicket(this, newTicket.toRest())
    }

    override fun onModifyTicketOk(ticket: Ticket) {
        mView.onModifyTicketOk(ticket)
    }

    override fun onModifyTicketFailed(error: String) {
        mView.onModifyTicketFailed(error)
    }

    override fun getTicketByTripId(tripId: Int) {
        Repository().getTicketByTrip(this, tripId)
    }

    override fun onGetTicketByTripIdOk(ticket: Ticket) {
        mView.getTicketByTripIdOk(ticket)
    }

    override fun onGetTicketByTripIdFailed(error: String) {
        mView.getTicketByTripIdFailed(error)
    }

}
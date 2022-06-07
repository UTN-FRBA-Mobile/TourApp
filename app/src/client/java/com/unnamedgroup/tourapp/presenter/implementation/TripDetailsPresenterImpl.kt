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

    override fun onModifyTicketOk(ticket: Ticket) {
        mView.onModifyTicketOk(ticket)
    }

    override fun onModifyTicketFailed(error: String) {
        mView.onModifyTicketFailed(error)
    }

}
package com.unnamedgroup.tourapp.presenter.implementation

import com.unnamedgroup.tourapp.model.business.Ticket
import com.unnamedgroup.tourapp.presenter.interfaces.MyTripsPresenterInt
import com.unnamedgroup.tourapp.presenter.interfaces.TripDetailsPresenterInt
import com.unnamedgroup.tourapp.repository.TripsRepository
import com.unnamedgroup.tourapp.utils.AbstractPresenter
import com.unnamedgroup.tourapp.view.fragment.TripDetailsFragment

class TripDetailsPresenterImpl(private val mView: TripDetailsPresenterInt.View) : TripDetailsPresenterInt, AbstractPresenter<TripDetailsPresenterInt.View>(
    mView
) {

    override fun modifyTicket(newTicket: Ticket) {
        TripsRepository().modifyTicket(this, newTicket.id!!, newTicket.toRest())
    }

    override fun onModifyTicketOk(ticket: Ticket) {
        mView.onModifyTicketOk(ticket)
    }

    override fun onModifyTicketFailed(error: String) {
        mView.onModifyTicketFailed(error)
    }

}
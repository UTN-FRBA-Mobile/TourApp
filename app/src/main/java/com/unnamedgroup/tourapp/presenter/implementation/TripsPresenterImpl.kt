package com.unnamedgroup.tourapp.presenter.implementation

import com.unnamedgroup.tourapp.model.business.Ticket
import com.unnamedgroup.tourapp.presenter.interfaces.TripsPresenterInt
import com.unnamedgroup.tourapp.repository.TripsRepository
import com.unnamedgroup.tourapp.utils.AbstractPresenter

class TripsPresenterImpl(private val mView: TripsPresenterInt.View) : TripsPresenterInt, AbstractPresenter<TripsPresenterInt.View>(
    mView
) {

    override fun getLastTicketByUser(userId: Int) {
        TripsRepository().getLastTicketByUser(this, userId)
    }

    override fun onGetLastTicketByUserOk(ticket: Ticket) {
        mView.onGetLastTicketByUserOk(ticket)
    }

    override fun onGetLastTicketByUserFailed(error: String) {
        mView.onGetLastTicketByUserFailed(error)
    }

}
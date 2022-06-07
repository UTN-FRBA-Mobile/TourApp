package com.unnamedgroup.tourapp.presenter.implementation

import com.unnamedgroup.tourapp.model.business.Ticket
import com.unnamedgroup.tourapp.presenter.interfaces.MyTripsPresenterInt
import com.unnamedgroup.tourapp.repository.Repository
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
}
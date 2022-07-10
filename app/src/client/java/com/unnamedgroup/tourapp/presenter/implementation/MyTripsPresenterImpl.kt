package com.unnamedgroup.tourapp.presenter.implementation

import android.text.format.DateUtils
import com.unnamedgroup.tourapp.model.business.Ticket
import com.unnamedgroup.tourapp.presenter.interfaces.MyTripsPresenterInt
import com.unnamedgroup.tourapp.repository.Repository
import com.unnamedgroup.tourapp.utils.AbstractPresenter
import java.time.LocalDateTime
import java.util.*

class MyTripsPresenterImpl(private val mView: MyTripsPresenterInt.View) : MyTripsPresenterInt, AbstractPresenter<MyTripsPresenterInt.View>(
    mView
) {

    override fun getTicketsByUser(userId: Int) {
        Repository().getTicketsByUser(this, userId)
    }

    override fun onGetTicketsByUserOk(tickets: MutableList<Ticket>) {
        val currentDate = Date()
        val filteredTickets: MutableList<Ticket> = tickets.filter { t -> t.trip.date.after(currentDate) || DateUtils.isToday(t.trip.date.time) } as MutableList<Ticket>
        mView.onGetTicketsByUserOk(filteredTickets)
    }

    override fun onGetTicketsByUserFailed(error: String) {
        mView.onGetTicketsByUserFailed(error)
    }
}
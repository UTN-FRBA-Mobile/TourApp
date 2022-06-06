package com.unnamedgroup.tourapp.presenter.interfaces

import com.unnamedgroup.tourapp.model.business.Ticket
import com.unnamedgroup.tourapp.utils.BaseView

interface TripsPresenterInt {

    fun getLastTicketByUser(userId: Int)
    fun onGetLastTicketByUserOk(ticket: Ticket)
    fun onGetLastTicketByUserFailed(error: String)

    interface View: BaseView {
        fun onGetLastTicketByUserOk(ticket: Ticket)
        fun onGetLastTicketByUserFailed(error: String)
    }

}
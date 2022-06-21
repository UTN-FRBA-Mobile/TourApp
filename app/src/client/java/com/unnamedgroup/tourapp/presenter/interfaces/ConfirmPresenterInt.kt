package com.unnamedgroup.tourapp.presenter.interfaces

import com.unnamedgroup.tourapp.model.business.Trip
import com.unnamedgroup.tourapp.utils.BaseView

interface ConfirmPresenterInt {

    fun modifyTrip(newTrip: Trip)
    fun onModifyTripOk(trip: Trip)
    fun onModifyTripFailed(error: String)

    interface View: BaseView {
        fun onModifyTripOk(trip: Trip)
        fun onModifyTripFailed(error: String)

    }

}
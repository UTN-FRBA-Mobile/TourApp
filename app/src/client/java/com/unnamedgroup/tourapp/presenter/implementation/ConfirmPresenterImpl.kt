package com.unnamedgroup.tourapp.presenter.implementation

import com.unnamedgroup.tourapp.model.business.Trip
import com.unnamedgroup.tourapp.presenter.interfaces.ConfirmPresenterInt
import com.unnamedgroup.tourapp.presenter.interfaces.TripDetailsPresenterInt
import com.unnamedgroup.tourapp.repository.Repository
import com.unnamedgroup.tourapp.utils.AbstractPresenter

class ConfirmPresenterImpl(private val mView: ConfirmPresenterInt.View) : ConfirmPresenterInt, AbstractPresenter<ConfirmPresenterInt.View>(
    mView
) {

    override fun modifyTrip(newTrip: Trip) {
        Repository().modifyTrip(this, newTrip.id, newTrip.toRest())
    }


    override fun onModifyTripOk(trip: Trip) {
        mView.onModifyTripOk(trip)
    }

    override fun onModifyTripFailed(error: String) {
        mView.onModifyTripFailed(error)
    }

}
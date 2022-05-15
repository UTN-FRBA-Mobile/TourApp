package com.unnamedgroup.tourapp.presenter.implementation

import com.unnamedgroup.tourapp.presenter.interfaces.MyTripsPresenterInt
import com.unnamedgroup.tourapp.repository.TripsRepository
import com.unnamedgroup.tourapp.utils.AbstractPresenter

class MyTripsPresenterImpl(private val mView: MyTripsPresenterInt.View) : MyTripsPresenterInt, AbstractPresenter<MyTripsPresenterInt.View>(
    mView
) {

    override fun getTrips() {
        try {
            mView.onGetTripsOk(TripsRepository().getTrips())
        } catch (error: Exception) {
            mView.onGetTripsError(error.toString())
        }
    }

}
package com.unnamedgroup.tourapp.presenter.implementation

import com.unnamedgroup.tourapp.model.business.Trip
import com.unnamedgroup.tourapp.presenter.interfaces.GetTripsPresenterInt
import com.unnamedgroup.tourapp.repository.TripsRepository
import com.unnamedgroup.tourapp.utils.AbstractPresenter

class GetTripsPresenterImpl(private val mView: GetTripsPresenterInt.View) : GetTripsPresenterInt, AbstractPresenter<GetTripsPresenterInt.View>(
    mView
) {

    override fun getTrips() {
        TripsRepository().getTrips(this)
    }

    override fun onGetTripsOk(trips: MutableList<Trip>) {
        mView.onGetTripsOk(trips)
    }

    override fun onGetTripsFailed(error: String) {
        mView.onGetTripsError(error)
    }

}
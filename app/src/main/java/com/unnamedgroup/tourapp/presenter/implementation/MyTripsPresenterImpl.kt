package com.unnamedgroup.tourapp.presenter.implementation

import com.unnamedgroup.tourapp.model.Trip
import com.unnamedgroup.tourapp.presenter.MyTripsPresenterInt
import com.unnamedgroup.tourapp.repository.TripsRepository

class MyTripsPresenterImpl : MyTripsPresenterInt {

    override fun getTrips(): ArrayList<Trip> {
        return TripsRepository().getTrips()
    }

}
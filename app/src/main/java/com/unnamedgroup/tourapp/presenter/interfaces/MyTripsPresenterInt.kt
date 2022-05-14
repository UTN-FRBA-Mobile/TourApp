package com.unnamedgroup.tourapp.presenter.interfaces

import com.unnamedgroup.tourapp.model.Trip

interface MyTripsPresenterInt {
    fun getTrips(): ArrayList<Trip>
}
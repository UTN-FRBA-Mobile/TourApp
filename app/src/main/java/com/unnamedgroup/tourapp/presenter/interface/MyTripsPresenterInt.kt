package com.unnamedgroup.tourapp.presenter.interface

import com.unnamedgroup.tourapp.model.Trip

interface MyTripsPresenterInt {
    fun getTrips(): ArrayList<Trip>
}
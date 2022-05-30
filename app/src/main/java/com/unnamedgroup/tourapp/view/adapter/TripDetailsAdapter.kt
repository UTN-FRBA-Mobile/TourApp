package com.unnamedgroup.tourapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.unnamedgroup.tourapp.R
import com.unnamedgroup.tourapp.model.business.Passenger

class TripDetailsAdapter(
    private var mPassengers: MutableList<Passenger>,
) :
    RecyclerView.Adapter<TripDetailsAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.tripdetails_viewholder, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val passenger = mPassengers[position]
        val passengerName: TextInputEditText = holder.view.findViewById(R.id.td_passenger_name)
        val passengerDni: TextInputEditText = holder.view.findViewById(R.id.td_passenger_dni)
        passengerName.setText(passenger.name.toString())
        passengerDni.setText(passenger.dni.toString())
        //tripLinearLayout.setOnClickListener { view -> onClickListener.onClick(view, ticket) }
    }

    override fun getItemCount() = mPassengers.size

    fun setList(passengers: MutableList<Passenger>) {
        mPassengers = passengers
        notifyDataSetChanged()
    }

}
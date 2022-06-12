package com.unnamedgroup.tourapp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.unnamedgroup.tourapp.R
import com.unnamedgroup.tourapp.model.business.TripPassenger


class TripDetailsDriverAdapter(
    private var mPassengers: MutableList<TripPassenger>,
    private val mContext: Context?,
    private val onItemToggleListener: OnItemToggleListener
    ):
    RecyclerView.Adapter<TripDetailsDriverAdapter.ViewHolder>() {

    interface OnItemToggleListener {
        fun onToggle(passengerPosition: Int, newValue: Boolean)
    }

    var passengersFilter : String = ""
    private var showingList = mPassengers.toMutableList()

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tripdetailsdriver_viewholder, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val passenger = showingList[position]
        val nameTextview : TextView = holder.view.findViewById(R.id.passenger_name_textview)
        val dniTextview : TextView = holder.view.findViewById(R.id.passenger_dni_textview)
        val busBoardingTextview : TextView = holder.view.findViewById(R.id.passenger_bus_boarding_textview)
        val busStopTextview : TextView = holder.view.findViewById(R.id.passenger_bus_stop_textview)
        val busBoarded : CheckBox = holder.view.findViewById(R.id.passenger_bus_boarded_checkBox)

        nameTextview.text = passenger.name
        dniTextview.text = mContext?.getString(R.string.trip_details_dni, passenger.dni)
        busBoardingTextview.text = mContext?.getString(R.string.trip_details_bus_boarding, passenger.busBoarding)
        busStopTextview.text = mContext?.getString(R.string.trip_details_bus_stop, passenger.busStop)
        busBoarded.isChecked = passenger.busBoarded
        busBoarded.setOnCheckedChangeListener{ _, isChecked ->
            onItemToggleListener.onToggle(holder.adapterPosition, isChecked)
        }
    }

    override fun getItemCount() = showingList.size

    fun setFilter(filterValue : String) {
        passengersFilter = filterValue
        showingList = mPassengers.filter {
            it.dni.contains(filterValue, true) ||
            it.name.contains(filterValue, true) ||
            it.busBoarding.contains(filterValue, true) ||
            it.busStop.contains(filterValue, true)
        }.toMutableList()
        notifyDataSetChanged()
    }

    fun setList(passengers: MutableList<TripPassenger>) {
        mPassengers = passengers
        showingList = passengers
        notifyDataSetChanged()
    }
}
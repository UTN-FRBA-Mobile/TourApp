package com.unnamedgroup.tourapp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.unnamedgroup.tourapp.R
import com.unnamedgroup.tourapp.model.business.Trip

class MyTripsDriverAdapter(
    private var mTrips: MutableList<Trip>,
    private val mContext: Context?,
    private val onClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<MyTripsDriverAdapter.ViewHolder>() {

    var tripsFilter : String = ""
    private var showingList = mTrips.toMutableList()

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    interface OnItemClickListener {
        fun onClick(view: View, trip: Trip)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTripsDriverAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mytripsdriver_viewholder, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val trip = showingList[position]
        val tripLinearLayout: LinearLayout = holder.view.findViewById(R.id.trip_card_linear_layout)
        val idTextview : TextView = holder.view.findViewById(R.id.trip_id_textview)
        val destinationHourTextview : TextView = holder.view.findViewById(R.id.trip_destination_hour_textview)

        idTextview.text = mContext?.getString(R.string.trip_id, trip.id)
        destinationHourTextview.text = mContext?.getString(R.string.trip_origin_destination_hour, trip.origin, trip.destination, trip.departureTime)
        tripLinearLayout.setOnClickListener { view -> onClickListener.onClick(view, trip) }
    }

    override fun getItemCount() = showingList.size

    fun setFilter(filterValue : String) {
        tripsFilter = filterValue
        showingList = mTrips.filter {
            val tripIdText = "Viaje " + it.id.toString()
            it.state.text.contains(filterValue, true) ||
                    it.id.toString().contains(filterValue, true) ||
                    it.origin.contains(filterValue, true) ||
                    it.destination.contains(filterValue, true) ||
                    it.departureTime.contains(filterValue, true) ||
                    tripIdText.contains(filterValue, true)
        }.toMutableList()
        notifyDataSetChanged()
    }

    fun setList(trips: MutableList<Trip>) {
        mTrips = trips
        showingList = trips
        notifyDataSetChanged()
    }
}
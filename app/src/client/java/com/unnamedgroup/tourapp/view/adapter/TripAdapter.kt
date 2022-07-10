package com.unnamedgroup.tourapp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.unnamedgroup.tourapp.R
import com.unnamedgroup.tourapp.model.business.Trip

class TripAdapter(
    private var mTrips: MutableList<Trip>,
    private val mContext: Context?,
    private val onClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<TripAdapter.ViewHolder>() {

    var tripsFilter: String = ""
    private var showingList = mTrips.toMutableList()

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    interface OnItemClickListener {
        fun onClick(view: View, trip: Trip)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.trips_viewholder, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val trip = showingList[position]
        val tripLinearLayout: LinearLayout = holder.view.findViewById(R.id.trip_card_linear_layout)
        val stateTextView: TextView = holder.view.findViewById(R.id.trip_state_textview)
        val stateImageView: ImageView = holder.view.findViewById(R.id.trip_state_imageview)
        val originDestinationTextView: TextView =
            holder.view.findViewById(R.id.trip_origin_destination_textview)
        val dateHourTextView: TextView = holder.view.findViewById(R.id.trip_date_hour_textview)
        val priceTextView: TextView = holder.view.findViewById(R.id.trip_price_textview)
        val soldOutTextView: TextView = holder.view.findViewById(R.id.trip_sold_out_text)

        stateTextView.text = trip.state.text
        stateImageView.setColorFilter(
            ContextCompat.getColor(
                mContext!!,
                getColorByState(trip.state.int)
            )
        )
        originDestinationTextView.text =
            mContext.getString(R.string.trip_name_text, trip.origin, trip.destination)
        dateHourTextView.text = mContext.getString(
            R.string.trip_date_hour,
            trip.dateStr,
            trip.departureTime
        )
        tripLinearLayout.setOnClickListener { view -> onClickListener.onClick(view, trip) }
        if(trip.passengersAmount == 0){
            soldOutTextView.text = "Agotado"
            soldOutTextView.visibility = android.view.View.VISIBLE
        }
    }

    private fun getColorByState(state: Int): Int {
        val color = when (state) {
            Trip.TripState.PROCESSING.int -> R.color.black
            Trip.TripState.CONFIRMED.int -> R.color.green
            Trip.TripState.DELAYED.int -> R.color.yellow
            Trip.TripState.CANCELLED.int -> R.color.red
            else -> R.color.black
        }
        return color
    }

    override fun getItemCount() = showingList.size

    fun setFilter(filterValue: String) {
        tripsFilter = filterValue
        showingList = mTrips.filter {
            it.state.text.contains(filterValue, true) ||
                    it.origin.contains(filterValue, true) ||
                    it.destination.contains(filterValue, true) ||
                    it.dateStr.contains(filterValue, true) ||
                    it.departureTime.contains(filterValue, true)
        }.toMutableList()
        notifyDataSetChanged()
    }

    fun setList(trips: MutableList<Trip>) {
        mTrips = trips
        showingList = trips
        notifyDataSetChanged()
    }

}
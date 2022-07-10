package com.unnamedgroup.tourapp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.unnamedgroup.tourapp.R
import com.unnamedgroup.tourapp.model.business.Ticket
import com.unnamedgroup.tourapp.model.business.Trip

class MyTripsAdapter(
    private var mTickets: MutableList<Ticket>,
    private val mContext: Context?,
    private val onClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<MyTripsAdapter.ViewHolder>() {

    var tripsFilter: String = ""
    private var showingList = mTickets.toMutableList()

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    interface OnItemClickListener {
        fun onClick(view: View, ticket: Ticket)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.mytrips_viewholder, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ticket = showingList[position]
        val tripLinearLayout: LinearLayout = holder.view.findViewById(R.id.trip_card_linear_layout)
        val stateTextView: TextView = holder.view.findViewById(R.id.trip_state_textview)
        val stateImageView: ImageView = holder.view.findViewById(R.id.trip_state_imageview)
        val originDestinationTextView: TextView =
            holder.view.findViewById(R.id.trip_origin_destination_textview)
        val dateHourTextView: TextView = holder.view.findViewById(R.id.trip_date_hour_textview)

        stateTextView.text = ticket.trip.state.text
        stateImageView.setColorFilter(
            ContextCompat.getColor(
                mContext!!,
                getColorByState(ticket.trip.state.int)
            )
        )
        originDestinationTextView.text =
            mContext.getString(R.string.trip_name_text, ticket.trip.origin, ticket.trip.destination)
        dateHourTextView.text = mContext.getString(
            R.string.trip_date_hour,
            ticket.trip.dateStr,
            ticket.trip.departureTime
        )
        tripLinearLayout.setOnClickListener { view -> onClickListener.onClick(view, ticket) }
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
        showingList = mTickets.filter {
            val tripIdText = "Viaje " + it.id.toString()
            it.trip.state.text.contains(filterValue, true) ||
                    it.trip.origin.contains(filterValue, true) ||
                    it.trip.destination.contains(filterValue, true) ||
                    it.trip.dateStr.contains(filterValue, true) ||
                    it.trip.departureTime.contains(filterValue, true) ||
                    tripIdText.contains(filterValue, true)
        }.toMutableList()
        notifyDataSetChanged()
    }

    fun setList(tickets: MutableList<Ticket>) {
        mTickets = tickets
        showingList = tickets
        notifyDataSetChanged()
    }

}
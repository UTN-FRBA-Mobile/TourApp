package com.unnamedgroup.tourapp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.unnamedgroup.tourapp.R
import com.unnamedgroup.tourapp.model.business.Ticket
import com.unnamedgroup.tourapp.utils.Utils

class ConfirmTripTicketsAdapter (private val myDataset: List<Ticket>, private val context: Context?) :
    RecyclerView.Adapter<ConfirmTripTicketsAdapter.MyViewHolder>()  {

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.confirm_trip_ticket_viewholder, parent, false)
        return MyViewHolder(view)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.view.findViewById<TextView>(R.id.value_origin).text = myDataset[position].trip.origin
        holder.view.findViewById<TextView>(R.id.value_destination).text = myDataset[position].trip.destination
        holder.view.findViewById<TextView>(R.id.value_date).text = Utils.getDateWithFormat(myDataset[position].trip.date, "dd/MM/yyyy")
        holder.view.findViewById<TextView>(R.id.value_time).text = myDataset[position].trip.departureTime
        holder.view.findViewById<TextView>(R.id.value_origin_stop).text = myDataset[position].busBoarding
        holder.view.findViewById<TextView>(R.id.value_destination_stop).text = myDataset[position].busStop
        holder.view.findViewById<TextView>(R.id.value_number_of_passengers).text = myDataset[position].passengers.size.toString()

    }

    override fun getItemCount() = myDataset.size

}
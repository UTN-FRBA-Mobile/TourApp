package com.unnamedgroup.tourapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.unnamedgroup.tourapp.R
import com.unnamedgroup.tourapp.model.business.Passenger


class ConfirmTripPassengersAdapter(private val myDataset: List<Passenger>) :
    RecyclerView.Adapter<ConfirmTripPassengersAdapter.MyViewHolder>()  {

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.confirm_trip_passenger_viewholder, parent, false)
        return MyViewHolder(view)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.view.findViewById<TextView>(R.id.passengerNumber).text = ((position + 1).toString() + ": ")
        holder.view.findViewById<TextView>(R.id.passengerName).text = myDataset[position].name
        holder.view.findViewById<TextView>(R.id.passengerDNI).text = myDataset[position].dni
    }

    override fun getItemCount() = myDataset.size

}
package com.unnamedgroup.tourapp.view.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.unnamedgroup.tourapp.R
import com.unnamedgroup.tourapp.model.business.Passenger

class TripDetailsAdapter(
    private var mPassengers: MutableList<Passenger>,
    private val onItemEditListener: OnItemEditListener
) :
    RecyclerView.Adapter<TripDetailsAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    interface OnItemEditListener {
        fun onEdit(passengerPosition: Int, newValue: String, isName: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.tripdetails_viewholder, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val passenger = mPassengers[position]
        val passengerName: TextInputEditText = holder.view.findViewById(R.id.td_passenger_name)
        val passengerDni: TextInputEditText = holder.view.findViewById(R.id.td_passenger_dni)
        passengerName.setText(passenger.name)
        passengerDni.setText(passenger.dni)
        passengerName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(value: CharSequence?, start: Int, before: Int, count: Int) {
                onItemEditListener.onEdit(holder.adapterPosition, value.toString(), true)
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        passengerDni.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(value: CharSequence?, start: Int, before: Int, count: Int) {
                onItemEditListener.onEdit(holder.adapterPosition, value.toString(), false)
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    override fun getItemCount() = mPassengers.size

    fun setList(passengers: MutableList<Passenger>) {
        mPassengers = passengers
        notifyDataSetChanged()
    }

}
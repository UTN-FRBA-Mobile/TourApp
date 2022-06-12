package com.unnamedgroup.tourapp.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.unnamedgroup.tourapp.R
import com.unnamedgroup.tourapp.databinding.FragmentTripDetailsDriverBinding
import com.unnamedgroup.tourapp.model.business.Ticket
import com.unnamedgroup.tourapp.model.business.Trip
import com.unnamedgroup.tourapp.model.business.TripPassenger
import com.unnamedgroup.tourapp.presenter.implementation.MyTripsPresenterImpl
import com.unnamedgroup.tourapp.presenter.interfaces.MyTripsPresenterInt
import com.unnamedgroup.tourapp.view.adapter.TripDetailsDriverAdapter

class TripDetailsDriverFragment : Fragment(), MyTripsPresenterInt.View {

    private var _binding: FragmentTripDetailsDriverBinding? = null
    private var myTripsPresenter: MyTripsPresenterImpl = MyTripsPresenterImpl(this)
    private var viewAdapter: TripDetailsDriverAdapter? = null
    private var tripStatesAdapter: ArrayAdapter<String>? = null
    private lateinit var currentTickets: MutableList<Ticket>
    private lateinit var currentPassengers: MutableList<TripPassenger>
    private lateinit var trip: Trip

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewAdapter = TripDetailsDriverAdapter(
            mutableListOf(),
            context,
            object : TripDetailsDriverAdapter.OnItemToggleListener {
                override fun onToggle(passengerPosition: Int, newValue: Boolean) {
                    val passenger = currentPassengers[passengerPosition]
                    passenger.busBoarded = newValue
                    myTripsPresenter.saveTicket(currentTickets, passenger, passengerPosition, newValue)
                }
            })

        myTripsPresenter.getTicketsByTrip(arguments?.getParcelable<Trip>("Trip")!!.id)
        val tripStateList = resources.getStringArray((R.array.trip_state_list))
        tripStatesAdapter = context?.let { ArrayAdapter(it, R.layout.list_item, tripStateList) }

        _binding = FragmentTripDetailsDriverBinding.inflate(inflater, container, false)

        if (arguments != null && requireArguments().containsKey("Trip")) {
            trip = requireArguments().getParcelable("Trip")!!
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewManager = LinearLayoutManager(this.context)

        val tripIdTextview: TextView = view.findViewById(R.id.trip_details_id_textview)
        val tripBoardingStopHourTextview: TextView =
            view.findViewById(R.id.trip_origin_destination_hour_textview)

        tripIdTextview.text = context?.getString(R.string.trip_id, trip.id)
        tripBoardingStopHourTextview.text = context?.getString(
            R.string.trip_origin_destination_hour,
            trip.origin,
            trip.destination,
            trip.departureTime
        )

        with(binding.tripStatusTextView) {
            setAdapter(tripStatesAdapter)
            setText(trip.state.text, false)
            setOnItemClickListener { _, _, position, _ ->
                val newState: String = (adapter.getItem(position) ?: "") as String
                trip.state = trip.getStateByText(newState)
                myTripsPresenter.saveTrip(trip)
            }
        }

        binding.qrButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("Trip", trip)
            findNavController().navigate(R.id.action_TripDetailsDriverFragment_to_QrScannerFragment, bundle)
        }

        binding.tripDetailsDriverSearchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewAdapter!!.setFilter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.tripDetailsDriverRecyclerview.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    private fun setRecyclerViewList(passengers: MutableList<TripPassenger>) {
        viewAdapter!!.setList(passengers)
    }

    override fun onGetTicketsByTripOk(
        tickets: MutableList<Ticket>,
        passengers: MutableList<TripPassenger>
    ) {
        currentTickets = tickets
        currentPassengers = passengers
        setRecyclerViewList(passengers)

        if (arguments != null && requireArguments().containsKey("ScanResult")) {
            val scanResultPassenger = requireArguments().getString("ScanResult")!!
            val passenger: TripPassenger? = viewAdapter?.getPassengerById(scanResultPassenger.toInt())
            if (passenger == null) {
                Toast.makeText(context, getString(R.string.passenger_not_found_by_qr), Toast.LENGTH_SHORT).show()
            } else {
                val passengerPosition: Int = viewAdapter!!.getPassengerPosition(passenger)
                passenger.busBoarded = true
                myTripsPresenter.saveTicket(tickets, passenger, passengerPosition, true)
            }
        }

    }

    override fun onGetTicketsByTripFailed(error: String) {
        Toast.makeText(context, getString(R.string.get_trips_error), Toast.LENGTH_SHORT).show()
    }

    override fun onSaveTripOk() {
        return
    }

    override fun onSaveTripError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun onSaveTicketOk(passengerPosition: Int, newValue: Boolean) {
        viewAdapter!!.togglePassenger(passengerPosition, newValue)
    }

    override fun onSaveTicketError(error: String, passengerPosition: Int, newValue: Boolean) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        viewAdapter!!.togglePassenger(passengerPosition, !newValue)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
}
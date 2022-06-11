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
import com.unnamedgroup.tourapp.model.business.Trip
import com.unnamedgroup.tourapp.model.business.TripPassenger
import com.unnamedgroup.tourapp.presenter.implementation.MyTripsPresenterImpl
import com.unnamedgroup.tourapp.presenter.interfaces.MyTripsPresenterInt
import com.unnamedgroup.tourapp.view.adapter.TripDetailsDriverAdapter

class TripDetailsDriverFragment : Fragment(), MyTripsPresenterInt.View {

    private var _binding: FragmentTripDetailsDriverBinding? = null
    private var myTripsPresenter : MyTripsPresenterImpl = MyTripsPresenterImpl(this)
    private var viewAdapter : TripDetailsDriverAdapter? = null
    private var tripStatesAdapter: ArrayAdapter<String>? = null
    private var scanResult: String = ""

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewAdapter = TripDetailsDriverAdapter(mutableListOf(), requireArguments()!!.getParcelable<Trip>("Trip")!!, context)
        myTripsPresenter.getPassengersByTrip(arguments?.getParcelable<Trip>("Trip")!!.id)
        val tripStateList = resources.getStringArray((R.array.trip_state_list))
        tripStatesAdapter = context?.let { ArrayAdapter(it, R.layout.list_item, tripStateList) }

        _binding = FragmentTripDetailsDriverBinding.inflate(inflater, container, false)

        val bundle  = this.arguments
        if (bundle !=null && bundle.containsKey("ScanResult")){
            scanResult = bundle!!.getString("ScanResult")!!
            //todo: select passenger by resul logic
            //scanresult tripId-passengerId
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewManager = LinearLayoutManager(this.context)

        val tripIdTextview : TextView = view.findViewById(R.id.trip_details_id_textview)
        val tripBoardingStopHourTextview : TextView = view.findViewById(R.id.trip_origin_destination_hour_textview)

        val trip = arguments?.getParcelable<Trip>("Trip")
        tripIdTextview.text = context?.getString(R.string.trip_id, trip!!.id)
        tripBoardingStopHourTextview.text = context?.getString(R.string.trip_origin_destination_hour, trip!!.origin, trip!!.destination, trip!!.departureTime)

        with(binding.tripStatusTextView) {
            setAdapter(tripStatesAdapter)
            setText(tripStatesAdapter!!.getItem(0), false)
        }

        binding.qrButton.setOnClickListener(){
            findNavController().navigate(R.id.action_TripDetailsDriverFragment_to_QrScannerFragment)
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

    override fun onGetPassengersByTripOk(passengers: MutableList<TripPassenger>) {
        setRecyclerViewList(passengers)
    }

    override fun onGetPassengersByTripFailed(error: String) {
        Toast.makeText(context, getString(R.string.get_trips_error), Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.unnamedgroup.tourapp.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.unnamedgroup.tourapp.R
import com.unnamedgroup.tourapp.databinding.FragmentTripDetailsBinding
import com.unnamedgroup.tourapp.databinding.FragmentTripDetailsDriverBinding
import com.unnamedgroup.tourapp.model.business.Ticket
import com.unnamedgroup.tourapp.model.business.Trip
import com.unnamedgroup.tourapp.model.business.TripPassenger
import com.unnamedgroup.tourapp.presenter.implementation.GetTripsPresenterImpl
import com.unnamedgroup.tourapp.presenter.implementation.MyTripsPresenterImpl
import com.unnamedgroup.tourapp.presenter.interfaces.GetTripsPresenterInt
import com.unnamedgroup.tourapp.presenter.interfaces.MyTripsPresenterInt
import com.unnamedgroup.tourapp.view.adapter.MyTripsDriverAdapter
import com.unnamedgroup.tourapp.view.adapter.TripDetailsDriverAdapter

class TripDetailsDriverFragment : Fragment(), MyTripsPresenterInt.View {

    private var _binding: FragmentTripDetailsDriverBinding? = null
    private var myTripsPresenter : MyTripsPresenterImpl = MyTripsPresenterImpl(this)
    private var viewAdapter : TripDetailsDriverAdapter? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewAdapter = TripDetailsDriverAdapter(mutableListOf(), context)
        myTripsPresenter.getTicketsByTrip(arguments?.getParcelable<Trip>("Trip")!!.id)

        _binding = FragmentTripDetailsDriverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewManager = LinearLayoutManager(this.context)

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

    override fun onGetTicketsByTripOk(trips: MutableList<TripPassenger>) {
        setRecyclerViewList(trips)
    }

    override fun onGetTicketsByTripFailed(error: String) {
        Toast.makeText(context, getString(R.string.get_trips_error), Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onGetTicketsByUserOk(tickets: MutableList<Ticket>) {
        TODO("Not yet implemented")
    }

    override fun onGetTicketsByUserFailed(error: String) {
        TODO("Not yet implemented")
    }
}
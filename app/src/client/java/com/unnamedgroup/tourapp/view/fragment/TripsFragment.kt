package com.unnamedgroup.tourapp.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.unnamedgroup.tourapp.R
import com.unnamedgroup.tourapp.databinding.FragmentTripsBinding
import com.unnamedgroup.tourapp.model.business.Ticket
import com.unnamedgroup.tourapp.model.business.Trip
import com.unnamedgroup.tourapp.presenter.implementation.GetTripsPresenterImpl
import com.unnamedgroup.tourapp.presenter.implementation.TripsPresenterImpl
import com.unnamedgroup.tourapp.presenter.interfaces.GetTripsPresenterInt
import com.unnamedgroup.tourapp.presenter.interfaces.TripsPresenterInt
import com.unnamedgroup.tourapp.view.adapter.TripAdapter

/**
 * A simple [Fragment] subclass.
 * Use the [TripsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TripsFragment : Fragment(), GetTripsPresenterInt.View, TripsPresenterInt.View {

    private var _binding: FragmentTripsBinding? = null
    private var getTripPresenter : GetTripsPresenterInt = GetTripsPresenterImpl(this)
    private var tripsPresenter : TripsPresenterInt = TripsPresenterImpl(this)
    private var viewAdapter : TripAdapter? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewAdapter = TripAdapter(mutableListOf(), context, object: TripAdapter.OnItemClickListener {
            override fun onClick(view: View, trip: Trip) {
                val bundle = Bundle()
                bundle.putParcelable("Trip", trip)
                findNavController().navigate(R.id.action_tripsFragment_to_NewTripFragment, bundle)
            }
        })
        //TODO: Traer solo los viajes validos
        getTripPresenter.getTrips()
        _binding = FragmentTripsBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewManager = LinearLayoutManager(this.context)

        binding.searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewAdapter!!.setFilter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.tripsRecyclerview.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        binding.bCopyLastTrip.setOnClickListener {
            tripsPresenter.getLastTicketByUser(1)//TODO: Asignar usuario real
        }
    }

    private fun setRecyclerViewList(trips: MutableList<Trip>) {
        viewAdapter!!.setList(trips)
    }

    override fun onGetTripsOk(trips: MutableList<Trip>) {
        setRecyclerViewList(trips)
    }

    override fun onGetTripsError(error: String) {
        Toast.makeText(context, getString(R.string.get_trips_error), Toast.LENGTH_SHORT).show()
    }

    override fun onGetLastTicketByUserOk(ticket: Ticket) {
        val bundle = Bundle()
        bundle.putParcelable("LastTicket", ticket)
        findNavController().navigate(R.id.action_tripsFragment_to_NewTripFragment, bundle)
    }

    override fun onGetLastTicketByUserFailed(error: String) {
        Toast.makeText(context, getString(R.string.get_trips_error), Toast.LENGTH_SHORT).show()
    }
}
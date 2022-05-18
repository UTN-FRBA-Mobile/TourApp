package com.unnamedgroup.tourapp.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.unnamedgroup.tourapp.R
import com.unnamedgroup.tourapp.databinding.FragmentMyTripsBinding
import com.unnamedgroup.tourapp.model.business.Trip
import com.unnamedgroup.tourapp.presenter.implementation.MyTripsPresenterImpl
import com.unnamedgroup.tourapp.presenter.interfaces.MyTripsPresenterInt
import com.unnamedgroup.tourapp.view.adapter.MyTripsAdapter

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MyTripsFragment : Fragment(),
    MyTripsPresenterInt.View {

    private var _binding: FragmentMyTripsBinding? = null
    private var myTripsPresenter : MyTripsPresenterInt = MyTripsPresenterImpl(this)
    private var viewAdapter : MyTripsAdapter? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        viewAdapter = MyTripsAdapter(mutableListOf(), context, object: MyTripsAdapter.OnItemClickListener {
            override fun onClick(view: View, trip: Trip) {
                val bundle = Bundle()
                bundle.putParcelable("Trip", trip)
                findNavController().navigate(R.id.action_MyTripsFragment_to_tripDetailsFragment, bundle)
            }
        })
        myTripsPresenter.getTrips()

        _binding = FragmentMyTripsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_MyTripsFragment_to_FragmentNewTrip)
        }

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
    }

    private fun setRecyclerViewList(trips: MutableList<Trip>) {
        viewAdapter!!.setList(trips)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onGetTripsOk(trips: MutableList<Trip>) {
        setRecyclerViewList(trips)
    }

    override fun onGetTripsError(error: String) {
        Toast.makeText(context, getString(R.string.get_trips_error), Toast.LENGTH_SHORT).show()
    }

}
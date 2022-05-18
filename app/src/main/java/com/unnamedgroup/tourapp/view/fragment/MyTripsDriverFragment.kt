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
import com.unnamedgroup.tourapp.databinding.FragmentMyTripsDriverBinding
import com.unnamedgroup.tourapp.model.business.Trip
import com.unnamedgroup.tourapp.presenter.implementation.MyTripsPresenterImpl
import com.unnamedgroup.tourapp.presenter.interfaces.MyTripsPresenterInt
import com.unnamedgroup.tourapp.view.adapter.MyTripsDriverAdapter

/**
 * A simple [Fragment] subclass.
 */
class MyTripsDriverFragment : Fragment(), MyTripsPresenterInt.View {

    private var _binding: FragmentMyTripsDriverBinding? = null
    private var myTripsPresenter : MyTripsPresenterInt = MyTripsPresenterImpl(this)
    private var viewAdapter : MyTripsDriverAdapter? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewAdapter = MyTripsDriverAdapter(mutableListOf(), context)
        myTripsPresenter.getTrips()

        _binding = FragmentMyTripsDriverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewManager = LinearLayoutManager(this.context)

        binding.driverSearchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewAdapter!!.setFilter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.driverTripsRecyclerview.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
}
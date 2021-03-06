package com.unnamedgroup.tourapp.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.unnamedgroup.tourapp.R
import com.unnamedgroup.tourapp.databinding.FragmentMyTripsBinding
import com.unnamedgroup.tourapp.model.business.Ticket
import com.unnamedgroup.tourapp.model.business.TripPassenger
import com.unnamedgroup.tourapp.presenter.implementation.MyTripsPresenterImpl
import com.unnamedgroup.tourapp.presenter.interfaces.MyTripsPresenterInt
import com.unnamedgroup.tourapp.utils.MyPreferences
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
            override fun onClick(view: View, ticket: Ticket) {
                val bundle = Bundle()
                bundle.putParcelable("Ticket", ticket)
                findNavController().navigate(R.id.action_MyTripsFragment_to_tripDetailsFragment, bundle)
            }
        })

        _binding = FragmentMyTripsBinding.inflate(inflater, container, false)
        myTripsPresenter.getTicketsByUser(MyPreferences.getUserId(requireContext()))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_MyTripsFragment_to_tripsFragment)
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

    private fun setRecyclerViewList(tickets: MutableList<Ticket>) {
        viewAdapter!!.setList(tickets)
        binding.noTripsLayout.visibility = if (tickets.size > 0) GONE else VISIBLE
        binding.tripsRecyclerview.visibility = if (tickets.size > 0) VISIBLE else GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        binding.searchInput.setText("")
    }

    override fun onGetTicketsByUserOk(tickets: MutableList<Ticket>) {
        setRecyclerViewList(tickets)
    }

    override fun onGetTicketsByUserFailed(error: String) {
        Toast.makeText(context, getString(R.string.get_trips_error), Toast.LENGTH_SHORT).show()
    }
}
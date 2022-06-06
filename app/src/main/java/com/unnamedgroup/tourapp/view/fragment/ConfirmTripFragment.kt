package com.unnamedgroup.tourapp.view.fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unnamedgroup.tourapp.R
import com.unnamedgroup.tourapp.databinding.FragmentConfirmTripBinding
import com.unnamedgroup.tourapp.model.business.Passenger
import com.unnamedgroup.tourapp.model.business.Ticket
import com.unnamedgroup.tourapp.model.business.Trip
import com.unnamedgroup.tourapp.model.business.User
import com.unnamedgroup.tourapp.utils.Utils
import com.unnamedgroup.tourapp.view.adapter.ConfirmTripPassengersAdapter
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ConfirmTripFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConfirmTripFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private lateinit var recyclerView: RecyclerView


    private var _binding: FragmentConfirmTripBinding? = null

    private val binding get() = _binding!!

    private var ticket : Ticket? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ticket = arguments?.getParcelable<Ticket>("Ticket")!!

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConfirmTripBinding.inflate(inflater, container, false)

        // Complete textview
        ticket?.let {
            binding.valueOrigin.text = it.trip.origin
            binding.valueDestination.text = it.trip.destination
            binding.valueDate.text = Utils.getDateWithFormat(it.trip.date, "dd/MM/yyyy")
            binding.valueTime.text = it.trip.departureTime
            binding.valueOriginStop.text = it.busBoarding
            binding.valueDestinationStop.text = it.busStop
            binding.valueNumberOfPassengers.text = it.passengers.size.toString()
        }

        // Complete recyclerView of passengers
        val viewManager = LinearLayoutManager(this.context)
        val viewAdapter = ConfirmTripPassengersAdapter(ticket!!.passengers)

        recyclerView = binding.confirmTripReciclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.confirmButton.setOnClickListener {
            findNavController().navigate(R.id.action_confirmTripFragment_to_resultScreenFragment)
        }

        binding.bankCbu.setOnClickListener {

        }

    }

}

fun generateTicket(): Ticket{
    val user = User(1,"pablito","pablito@mail","1234564","asdasdasd")

    val trip = Trip(1,"Mercedes","Capital Federal",20,500.0F, mutableListOf(), mutableListOf(),"18:00", Date(),Trip.TripState.CONFIRMED, User(1, "test", "test@test.ar", "40233444", "asdasd"))

     val listPassenger = mutableListOf<Passenger>(Passenger(name="Maria Felcitas", dni = "25.060.550",id=1, busBoarded = false),
        Passenger(name="Juana de Arco", dni = "5.060.550",id=2, busBoarded = false),Passenger(name="Juana de Barco", dni = "5.060.550",id=3, busBoarded = false),
        Passenger(name="Juana de Arco", dni = "5.060.550",id=4, busBoarded = false),Passenger(name="Juana de Barco", dni = "5.060.550",id=5, busBoarded = false))

    return Ticket(1, user,listPassenger,trip,"Iglesia","Obelisco")
}
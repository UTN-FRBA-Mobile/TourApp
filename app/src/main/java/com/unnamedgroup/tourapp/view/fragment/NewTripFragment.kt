package com.unnamedgroup.tourapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.unnamedgroup.tourapp.R
import com.unnamedgroup.tourapp.databinding.FragmentNewTripBinding
import com.unnamedgroup.tourapp.model.business.Passenger
import com.unnamedgroup.tourapp.model.business.Ticket
import com.unnamedgroup.tourapp.model.business.Trip

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class NewTripFragment : Fragment() {

    private var _binding: FragmentNewTripBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var lastTicket : Ticket? = null
    private var trip : Trip? = null
    private var boardingsAdapter: ArrayAdapter<String>? = null
    private var stopsAdapter: ArrayAdapter<String>? = null
    private var numberOfTicketsAdapter: ArrayAdapter<Int>? = null
    private var passengersLayouts: MutableList<LinearLayout> = ArrayList()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        lastTicket = arguments?.getParcelable<Ticket>("Ticket")
        lastTicket?.let {
            trip = it.trip
        } ?: run {
            trip = arguments?.getParcelable<Trip>("Trip")
        }
        trip?.busBoardings?.let { boardingsAdapter = ArrayAdapter(requireContext(), R.layout.list_item, it) }
        trip?.busStops?.let { stopsAdapter = ArrayAdapter(requireContext(), R.layout.list_item, it) }
        trip?.passengersAmount?.let { numberOfTicketsAdapter = ArrayAdapter(requireContext(), R.layout.list_item, (1..it).toList())}
        _binding = FragmentNewTripBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bNext.setOnClickListener {
            next()
        }
        binding.tilOrigin.editText?.setText(trip?.origin)
        binding.tilDestination.editText?.setText(trip?.destination)
        binding.tilDepartureDate.editText?.setText(trip?.dateStr)
        binding.tilDepartureHour.editText?.setText(trip?.departureTime)
        with(binding.actvBoarding) {
            setAdapter(boardingsAdapter)
            lastTicket?.let {
                setText(it.busBoarding, false)
            } ?: run {
                setText(boardingsAdapter!!.getItem(0), false)
            }
        }
        with(binding.actvStop) {
            setAdapter(stopsAdapter)
            lastTicket?.let {
                setText(it.busStop, false)
            } ?: run {
                setText(stopsAdapter!!.getItem(0), false)
            }
        }
        with(binding.actvNumberOfTickets) {
            setAdapter(numberOfTicketsAdapter)
            lastTicket?.let {
                setText(it.passengers.size.toString(), false)
                binding.llPassengers.removeAllViews()
                it.passengers.forEach {
                    val linearLayout = addPassengerLayout()
                    val textInputLayoutName = linearLayout.findViewById<TextInputLayout>(R.id.til_passenger_name)
                    textInputLayoutName.editText?.setText(it.name)
                    val textInputLayoutDni = linearLayout.findViewById<TextInputLayout>(R.id.til_passenger_dni)
                    textInputLayoutDni.editText?.setText(it.dni)
                }
            } ?: run {
                //TODO: Corregir error al volver del confirmar
                addPassengerLayout()
                setText(numberOfTicketsAdapter!!.getItem(0).toString(), false)
            }
            onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                onChangeNumberOfTickets(position)
            }
        }
        refreshPrice(binding.actvNumberOfTickets.text.toString().toInt())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun refreshPrice(numberOfTickets : Int) {
        binding.mtvPrice.text = getString(R.string.price) + (numberOfTickets) * trip!!.price
    }

    private fun onChangeNumberOfTickets(numberOfTickets : Int) {
        refreshPrice(numberOfTickets + 1)
        binding.llPassengers.removeAllViews()

        for(i in 0 .. numberOfTickets){
            if (i > passengersLayouts.count()-1) {
                addPassengerLayout()
            } else {
                binding.llPassengers.addView(passengersLayouts.get(i))
            }
        }
    }

    private fun addPassengerLayout() : LinearLayout {
        val linearLayout = LayoutInflater.from(requireContext()).inflate(R.layout.passenger,null) as LinearLayout
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.topMargin = 20
        passengersLayouts.add(linearLayout)
        binding.llPassengers.addView(linearLayout, layoutParams)
        return linearLayout
    }

    private fun next() {
        //TODO: Validar campos?
        var passengers = mutableListOf<Passenger>()
        passengersLayouts.take(binding.tilNumberOfTickets.editText?.text.toString().toInt()).forEach {
            val name = it.findViewById<TextInputLayout>(R.id.til_passenger_name).editText?.text.toString()
            val dni = it.findViewById<TextInputLayout>(R.id.til_passenger_dni).editText?.text.toString()
            passengers.add(Passenger(name, dni))
        }

        val busBoarding = binding.tilBoarding.editText?.text.toString()
        val busStop = binding.tilStop.editText?.text.toString()
        //TODO: Falta el campo ida y vuelta
        var ticket = Ticket(null,null, passengers, trip!!, busBoarding, busStop)
        val bundle = Bundle()
        bundle.putParcelable("Ticket", ticket)
        findNavController().navigate(R.id.action_NewTripFragment_to_confirmTripFragment, bundle)
    }
}
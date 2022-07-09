package com.unnamedgroup.tourapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.unnamedgroup.tourapp.R
import com.unnamedgroup.tourapp.databinding.FragmentNewTripBinding
import com.unnamedgroup.tourapp.model.business.Passenger
import com.unnamedgroup.tourapp.model.business.Ticket
import com.unnamedgroup.tourapp.model.business.Trip
import com.unnamedgroup.tourapp.model.business.User
import com.unnamedgroup.tourapp.presenter.implementation.NewTripPresenterImpl
import com.unnamedgroup.tourapp.presenter.implementation.TripsPresenterImpl
import com.unnamedgroup.tourapp.presenter.interfaces.NewTripPresenterInt
import com.unnamedgroup.tourapp.presenter.interfaces.TripsPresenterInt
import com.unnamedgroup.tourapp.utils.MyPreferences
import com.unnamedgroup.tourapp.utils.Utils
import java.text.SimpleDateFormat

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class NewTripFragment : Fragment(), NewTripPresenterInt.View {

    private var _binding: FragmentNewTripBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var newTripPresenter : NewTripPresenterInt = NewTripPresenterImpl(this)
    private var lastTicket : Ticket? = null
    private var firstTicket : Ticket? = null
    private var firstTicketPrice : Float = 0f
    private var trip : Trip? = null
    private var lastTrip : Trip? = null
    private var departureDatesList : List<String>? = null
    private var departureTimesList : List<String>? = null
    private var departureDateAdapter: ArrayAdapter<String>? = null
    private var departureTimeAdapter: ArrayAdapter<String>? = null
    private var boardingsAdapter: ArrayAdapter<String>? = null
    private var stopsAdapter: ArrayAdapter<String>? = null
    private var numberOfTicketsAdapter: ArrayAdapter<Int>? = null
    private var passengersLayouts: MutableList<LinearLayout> = ArrayList()
    private var roundTrip: Boolean = false
    private var user: User? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        firstTicket = arguments?.getParcelable<Ticket>("Ticket")
        firstTicket?.let {
            trip = arguments?.getParcelable<Trip>("LastTrip")
            roundTrip = true
            lastTicket = Ticket(null, it.user, it.passengers, trip!!,"", it.busStop, it.busBoarding)
            firstTicketPrice = it.passengers.size * it.trip.price
        } ?: run {
            lastTicket = arguments?.getParcelable<Ticket>("LastTicket")
            lastTicket?.let {
                trip = it.trip
            } ?: run {
                trip = arguments?.getParcelable<Trip>("Trip")
            }
        }
        trip?.busBoardings?.let { boardingsAdapter = ArrayAdapter(requireContext(), R.layout.list_item, it ) }
        trip?.busStops?.let { stopsAdapter = ArrayAdapter(requireContext(), R.layout.list_item, it) }
        trip?.passengersAmount?.let { numberOfTicketsAdapter = ArrayAdapter(requireContext(), R.layout.list_item, (1..it).toList())}
        trip?.let { newTripPresenter.getTripsByOriginAndDestination(it.origin,it.destination) }
        newTripPresenter.getUserById(MyPreferences.getUserId(requireContext()))
        _binding = FragmentNewTripBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bNext.setOnClickListener {
            if (binding.smRoundTrip.isChecked) {
                newTripPresenter.getRoundTrip(trip!!.destination, trip!!.origin)
            } else {
                next()
            }
        }
        binding.tilOrigin.editText?.setText(trip?.origin)
        binding.tilDestination.editText?.setText(trip?.destination)
        binding.tilDepartureDate.editText?.setText(trip?.dateStr)
        binding.tilDepartureHour.editText?.setText(trip?.departureTime)
        binding.actvDepartureDate.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            trip!!.date = Utils.parseDateWithFormat(departureDatesList!!.get(position), "dd-MM-yyyy")
            newTripPresenter.getTripsByOriginAndDestinationAndDate(trip!!.origin, trip!!.destination, departureDatesList!!.get(position))
        }
        binding.actvDepartureTime.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            trip!!.departureTime = departureTimesList!!.get(position)
            newTripPresenter.getTripsByOriginAndDestinationAndDateAndTime(trip!!.origin, trip!!.destination, Utils.getDateWithFormat(trip!!.date, "dd-MM-yyyy"), trip!!.departureTime)
        }
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
            stopsAdapter!!.notifyDataSetChanged()
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
        if (roundTrip) {
            binding.smRoundTrip.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun refreshPrice(numberOfTickets : Int) {
        var price : Float = 0f
        /*if (roundTrip) {
            price =  firstTicketPrice + numberOfTickets * trip!!.price
        } else {*/
        price =  numberOfTickets * trip!!.price
        /*}*/
        binding.mtvPrice.text = getString(R.string.price) + price
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
        //TODO: Reserva de pasajes
        var passengers = mutableListOf<Passenger>()
        var i: Int = 1
        passengersLayouts.take(binding.tilNumberOfTickets.editText?.text.toString().toInt()).forEach {
            val name = it.findViewById<TextInputLayout>(R.id.til_passenger_name).editText?.text.toString()
            if (name.isEmpty()) {
                Toast.makeText(context, getString(R.string.empty_name_error), Toast.LENGTH_SHORT).show()
                return;
            }
            val dni = it.findViewById<TextInputLayout>(R.id.til_passenger_dni).editText?.text.toString()
            if (dni.isEmpty()) {
                Toast.makeText(context, getString(R.string.empty_dni_error), Toast.LENGTH_SHORT).show()
                return;
            }
            passengers.add(Passenger(i++, name, dni, false))
        }

        val busBoarding = binding.tilBoarding.editText?.text.toString()
        val busStop = binding.tilStop.editText?.text.toString()
        var ticket = Ticket(null, user!!, passengers, trip!!,"", busBoarding, busStop)
        val bundle = Bundle()
        bundle.putParcelable("Ticket", ticket)
        if (binding.smRoundTrip.isChecked) {
            lastTrip?.let { bundle.putParcelable("LastTrip", lastTrip) }
            findNavController().navigate(R.id.action_NewTripFragment_self, bundle)
        } else {
            if (roundTrip) {
                bundle.putParcelable("Ticket", firstTicket)
                bundle.putParcelable("Ticket2", ticket)
            }
            findNavController().navigate(R.id.action_NewTripFragment_to_confirmTripFragment, bundle)
        }
    }

    private fun getDatesArray(trips: MutableList<Trip>) : List<String> {
        return trips.map { Utils.getDateWithFormat(it.date, "dd-MM-yyyy") } .distinct()
    }

    private fun getTimesArray(trips: MutableList<Trip>) : List<String> {
        return trips.map { it.departureTime } .distinct()
    }

    override fun onGetUserByIdOk(user: User) {
        this.user = user
    }

    override fun onGetUserByIdFailed(error: String) {
        Toast.makeText(context, getString(R.string.get_user_error), Toast.LENGTH_SHORT).show()
    }

    override fun onGetTripsByOriginAndDestinationOk(trips: MutableList<Trip>) {
        departureDatesList = getDatesArray(trips)
        departureDateAdapter = ArrayAdapter(requireContext(), R.layout.list_item, departureDatesList!!)
        with(binding.actvDepartureDate) {
            setAdapter(departureDateAdapter)
            lastTicket?.let {
                setText(Utils.getDateWithFormat(it.trip.date, "dd-MM-yyyy"), false)
                newTripPresenter.getTripsByOriginAndDestinationAndDate(it.trip.origin, it.trip.destination, Utils.getDateWithFormat(it.trip.date, "dd-MM-yyyy"))
            } ?: run {
                setText(Utils.getDateWithFormat(trip!!.date, "dd-MM-yyyy"), false)
                newTripPresenter.getTripsByOriginAndDestinationAndDate(trip!!.origin, trip!!.destination, Utils.getDateWithFormat(trip!!.date, "dd-MM-yyyy"))
            }
        }
    }

    override fun onGetTripsByOriginAndDestinationFailed(error: String) {
        Toast.makeText(context, getString(R.string.get_trips_error), Toast.LENGTH_SHORT).show()
    }

    override fun onGetTripsByOriginAndDestinationAndDateOk(trips: MutableList<Trip>) {
        trips?.let { trip = it.get(0) }
        departureTimesList = getTimesArray(trips)
        departureTimeAdapter = ArrayAdapter(requireContext(), R.layout.list_item, departureTimesList!!)
        with(binding.actvDepartureTime) {
            setAdapter(departureTimeAdapter)
            lastTicket?.let {
                setText(it.trip.departureTime, false)
            } ?: run {
                setText(trip?.departureTime, false)
            }
        }
    }

    override fun onGetTripsByOriginAndDestinationAndDateFailed(error: String) {
        Toast.makeText(context, getString(R.string.get_trips_error), Toast.LENGTH_SHORT).show()
    }

    override fun onGetTripsByOriginAndDestinationAndDateAndTimeOk(trips: MutableList<Trip>) {
        //TODO: Recargar lugar de subida y bajada
        trips?.let { trip = it.get(0) }
    }

    override fun onGetTripsByOriginAndDestinationAndDateAndTimeFailed(error: String) {
        Toast.makeText(context, getString(R.string.get_trips_error), Toast.LENGTH_SHORT).show()
    }

    override fun onGetRoundTripOk(trips: MutableList<Trip>) {
        trips?.let{ lastTrip = it.get(0)}
        next()
    }

    override fun onGetRoundTripFailed(error: String) {
        Toast.makeText(context, getString(R.string.get_trips_error), Toast.LENGTH_SHORT).show()
    }
}
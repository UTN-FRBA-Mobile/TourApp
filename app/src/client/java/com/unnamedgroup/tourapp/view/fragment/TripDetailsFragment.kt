package com.unnamedgroup.tourapp.view.fragment
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.unnamedgroup.tourapp.R
import com.unnamedgroup.tourapp.databinding.FragmentTripDetailsBinding
import com.unnamedgroup.tourapp.model.business.Ticket
import com.unnamedgroup.tourapp.presenter.implementation.TripDetailsPresenterImpl
import com.unnamedgroup.tourapp.presenter.interfaces.TripDetailsPresenterInt
import com.unnamedgroup.tourapp.view.adapter.TripDetailsAdapter

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class TripDetailsFragment : Fragment(),
    TripDetailsPresenterInt.View{

    private var _binding: FragmentTripDetailsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var departureStopsAdapter: ArrayAdapter<String>? = null
    private var arrivalStopsAdapter: ArrayAdapter<String>? = null
    private var currentTicket : Ticket? = null
    private var isEditing : Boolean = false
    private var viewAdapter : TripDetailsAdapter? = null
    private var tripDetailsPresenter : TripDetailsPresenterInt = TripDetailsPresenterImpl(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTripDetailsBinding.inflate(inflater, container, false)
        val bundle  = this.arguments
        if (bundle!!.containsKey("fromNotification") && bundle.getBoolean("fromNotification")) {
            tripDetailsPresenter.getTicketByTripId(bundle.getInt("tripId"))
        } else {
            currentTicket = bundle.getParcelable("Ticket")!!
            initAdapters()
        }

        return binding.root
    }

    private fun initAdapters() {
        val currentTicket = currentTicket!!

        val passengers = currentTicket.passengers
        val departureLocations = currentTicket.trip.busBoardings
        val arrivalLocations = currentTicket.trip.busStops

        departureStopsAdapter =
            context?.let { ArrayAdapter(it, R.layout.list_item, departureLocations) }
        arrivalStopsAdapter =
            context?.let { ArrayAdapter(it, R.layout.list_item, arrivalLocations) }

        viewAdapter = TripDetailsAdapter(passengers, object: TripDetailsAdapter.OnItemEditListener{
            override fun onEdit(passengerPosition: Int, newValue: String, isName: Boolean) {
                val newPassengers = currentTicket.passengers
                val newPassenger = currentTicket.passengers[passengerPosition]
                if(isName){
                    newPassenger.name = newValue
                } else {
                    newPassenger.dni = newValue
                }
                newPassengers[passengerPosition] = newPassenger
                currentTicket.passengers = newPassengers
                onEditTrip()
            }

        })

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (currentTicket != null) {
            setViewWithTicketInfo()
        }
    }

    private fun setViewWithTicketInfo() {
        if (currentTicket == null) {
            Toast.makeText(context, getString(R.string.get_trips_error), Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_tripDetailsFragment_to_MyTripsFragment)
        }

        val currentTicket = currentTicket!!

        binding.trip.text = currentTicket.trip.getName()
        binding.tripTime.text = currentTicket.trip.getFormattedDepartureTime()

        with(binding.departureStopText) {
            setAdapter(departureStopsAdapter)
            setText(currentTicket.busBoarding, false)
            setOnItemClickListener { _, _, position, _ ->
                val newStop = adapter.getItem(position) ?: ""
                currentTicket.busBoarding = newStop.toString()
                onEditTrip()
            }
        }
        with(binding.arrivalStopText) {
            setAdapter(arrivalStopsAdapter)
            setText(currentTicket.busStop, false)
            setOnItemClickListener { _, _, position, _ ->
                val newStop = adapter.getItem(position) ?: ""
                currentTicket.busStop= newStop.toString()
                onEditTrip()
            }
        }

        binding.cancelButton.setOnClickListener() {
            if(isEditing){
                val builder = AlertDialog.Builder(activity)
                builder.setTitle(R.string.details_alert_title)
                builder.setMessage(R.string.details_alert_message)
                builder.setPositiveButton(R.string.details_alert_yes) { _: DialogInterface, _: Int ->
                    findNavController().navigate(R.id.action_tripDetailsFragment_to_MyTripsFragment)
                }
                builder.setNegativeButton(R.string.details_alert_no) { _: DialogInterface, _: Int ->
                }
                builder.show()
            }
            else {
                findNavController().navigate(R.id.action_tripDetailsFragment_to_MyTripsFragment)
            }
        }

        with(binding.saveButton){
            setOnClickListener {
                tripDetailsPresenter.modifyTicket(currentTicket)
            }
            isEnabled = false
        }

        binding.saveButton.setOnClickListener() {
            tripDetailsPresenter.modifyTicket(currentTicket)
        }

        binding.qrButton.setOnClickListener(){
            val bundle = Bundle()
            bundle.putParcelable("Ticket", currentTicket)
            findNavController().navigate(R.id.action_tripDetailsFragment_to_tripQrFragment, bundle)
        }

        val viewManager = LinearLayoutManager(this.context)

        binding.passengersRecyclerview.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    fun onEditTrip(){
        isEditing = true
        binding.saveButton.isEnabled = true
        binding.qrButton.isEnabled = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onModifyTicketOk(ticket: Ticket) {
        //val bundle = Bundle()
        //bundle.putParcelable("ModifiedTicket", ticket)
        findNavController().navigate(R.id.action_tripDetailsFragment_to_MyTripsFragment)
    }

    override fun onModifyTicketFailed(error: String) {
        Toast.makeText(context, getString(R.string.modify_ticket_error), Toast.LENGTH_LONG).show()
    }

    override fun getTicketByTripIdOk(ticket: Ticket) {
        currentTicket = ticket
        initAdapters()
        setViewWithTicketInfo()
    }

    override fun getTicketByTripIdFailed(error: String) {
        Toast.makeText(context, getString(R.string.get_trips_error), Toast.LENGTH_LONG).show()
        findNavController().navigate(R.id.action_tripDetailsFragment_to_MyTripsFragment)
    }
}
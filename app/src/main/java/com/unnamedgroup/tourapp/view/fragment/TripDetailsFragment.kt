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
    private lateinit var currentTicket : Ticket
    private lateinit var draftTicket : Ticket
    private var isEditing : Boolean = false
    private var viewAdapter : TripDetailsAdapter? = null
    private var tripDetailsPresenter : TripDetailsPresenterInt = TripDetailsPresenterImpl(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTripDetailsBinding.inflate(inflater, container, false)
        val bundle  = this.arguments
        currentTicket = bundle!!.getParcelable("Ticket")!!
        draftTicket = bundle!!.getParcelable("Ticket")!!
        initAdapters()
        return binding.root

    }

    private fun initAdapters() {
        val passengers = currentTicket.passengers
        val departureLocations = currentTicket.trip.busBoardings
        val arrivalLocations = currentTicket.trip.busStops

        departureStopsAdapter =
            context?.let { ArrayAdapter(it, R.layout.list_item, departureLocations) }
        arrivalStopsAdapter =
            context?.let { ArrayAdapter(it, R.layout.list_item, arrivalLocations) }

        viewAdapter = TripDetailsAdapter(passengers, object: TripDetailsAdapter.OnItemEditListener{
            override fun onEdit(passengerPosition: Int, newValue: String, isName: Boolean) {
                var newPassengers = draftTicket.passengers
                var newPassenger = draftTicket.passengers[passengerPosition]
                if(isName){
                    newPassenger.name = newValue
                } else {
                    newPassenger.dni = newValue
                }
                newPassengers[passengerPosition] = newPassenger
                draftTicket.passengers = newPassengers
                onEditTrip()
            }

        })

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.trip.text = currentTicket.trip.getName()
        binding.tripTime.text = currentTicket.trip.getFormattedDepartureTime()

        with(binding.departureStopText) {
            setAdapter(departureStopsAdapter)
            setText(departureStopsAdapter!!.getItem(0), false)
            setOnItemClickListener { _, _, position, _ ->
                val newStop = adapter.getItem(position) ?: ""
                draftTicket.busBoarding = newStop.toString()
                onEditTrip()
            }
        }
        with(binding.arrivalStopText) {
            setAdapter(arrivalStopsAdapter)
            setText(arrivalStopsAdapter!!.getItem(0), false)
            setOnItemClickListener { _, _, position, _ ->
                val newStop = adapter.getItem(position) ?: ""
                draftTicket.busBoarding = newStop.toString()
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
            setOnClickListener() {
                tripDetailsPresenter.modifyTicket(draftTicket)
            }
            isEnabled = false
        }

        binding.saveButton.setOnClickListener() {
            tripDetailsPresenter.modifyTicket(draftTicket)
        }

        val viewManager = LinearLayoutManager(this.context)

        binding.passengersRecyclerview.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        with (binding.qrCode){
            try {
                val barcodeEncoder = BarcodeEncoder()
                val bitmap = barcodeEncoder.encodeBitmap(currentTicket.trip.id.toString(), BarcodeFormat.QR_CODE, 400, 400)
                setImageBitmap(bitmap)
            } catch (e: Exception) {
            }
        }

    }

    fun onEditTrip(){
        isEditing = true
        binding.saveButton.isEnabled = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onModifyTicketOk(ticket: Ticket) {
        val bundle = Bundle()
        bundle.putParcelable("modifyTicket", ticket)
        findNavController().navigate(R.id.action_tripDetailsFragment_to_MyTripsFragment, bundle)
    }

    override fun onModifyTicketFailed(error: String) {
        Toast.makeText(context, getString(R.string.modify_ticket_error), Toast.LENGTH_LONG).show()
    }
}
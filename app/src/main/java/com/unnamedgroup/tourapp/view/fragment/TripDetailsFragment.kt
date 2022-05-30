package com.unnamedgroup.tourapp.view.fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.unnamedgroup.tourapp.R
import com.unnamedgroup.tourapp.databinding.FragmentTripDetailsBinding
import com.unnamedgroup.tourapp.model.business.Passenger
import com.unnamedgroup.tourapp.model.business.Ticket
import com.unnamedgroup.tourapp.view.adapter.TripDetailsAdapter

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class TripDetailsFragment : Fragment() {

    private var _binding: FragmentTripDetailsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var departureStopsAdapter: ArrayAdapter<String>? = null
    private var arrivalStopsAdapter: ArrayAdapter<String>? = null
    private lateinit var currentTicket : Ticket
    private var viewAdapter : TripDetailsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTripDetailsBinding.inflate(inflater, container, false)
        val bundle  = this.arguments
        currentTicket = bundle!!.getParcelable("Ticket")!!
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

        viewAdapter = TripDetailsAdapter(passengers)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.trip.text = currentTicket.trip.getName()

        with(binding.departureStopText) {
            setAdapter(departureStopsAdapter)
            setText(departureStopsAdapter!!.getItem(0), false)
        }
        with(binding.arrivalStopText) {
            setAdapter(arrivalStopsAdapter)
            setText(arrivalStopsAdapter!!.getItem(0), false)
        }

        binding.cancelButton.setOnClickListener() {
            findNavController().navigate(R.id.action_tripDetailsFragment_to_MyTripsFragment)
        }

        binding.saveButton.setOnClickListener() {
            findNavController().navigate(R.id.action_tripDetailsFragment_to_MyTripsFragment)
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

    private fun setRecyclerViewList(passengers: MutableList<Passenger>) {
        viewAdapter!!.setList(passengers)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
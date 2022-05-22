package com.unnamedgroup.tourapp.view.fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.unnamedgroup.tourapp.R
import com.unnamedgroup.tourapp.databinding.FragmentTripDetailsBinding
import java.util.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class TripDetailsFragment : Fragment() {

    private var _binding: FragmentTripDetailsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var passengersAdapter: ArrayAdapter<String>? = null
    private var departureLocationsAdapter: ArrayAdapter<String>? = null
    private var arrivalsLocationsAdapter: ArrayAdapter<String>? = null
    private var departureTimesAdapter: ArrayAdapter<String>? = null
    private var departureStopsAdapter: ArrayAdapter<String>? = null
    private var arrivalStopsAdapter: ArrayAdapter<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        initAdapters()
        _binding = FragmentTripDetailsBinding.inflate(inflater, container, false)
        return binding.root

    }

    private fun initAdapters() {
        val passengers = resources.getStringArray((R.array.passengers_list))
        val departureLocations = resources.getStringArray((R.array.departure_locations_list))
        val arrivalLocations = resources.getStringArray((R.array.arrival_locations_list))
        val departureTimes = resources.getStringArray((R.array.departure_times_list))
        val departureStops = resources.getStringArray((R.array.departure_stops_list))
        val arrivalStops = resources.getStringArray((R.array.arrival_stops_list))

        passengersAdapter = context?.let { ArrayAdapter(it, R.layout.list_item, passengers) }
        departureLocationsAdapter =
            context?.let { ArrayAdapter(it, R.layout.list_item, departureLocations) }
        arrivalsLocationsAdapter =
            context?.let { ArrayAdapter(it, R.layout.list_item, arrivalLocations) }
        departureTimesAdapter =
            context?.let { ArrayAdapter(it, R.layout.list_item, departureTimes) }
        departureStopsAdapter =
            context?.let { ArrayAdapter(it, R.layout.list_item, departureStops) }
        arrivalStopsAdapter = context?.let { ArrayAdapter(it, R.layout.list_item, arrivalStops) }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        with(binding.passengersText) {
            setAdapter(passengersAdapter)
            setText(passengersAdapter!!.getItem(0), false)
        }
        with(binding.departureLocationText) {
            setAdapter(departureLocationsAdapter)
            setText(departureLocationsAdapter!!.getItem(0), false)
        }
        with(binding.arrivalLocationText) {
            setAdapter(arrivalsLocationsAdapter)
            setText(arrivalsLocationsAdapter!!.getItem(0), false)
        }
        with(binding.departureTimeText) {
            setAdapter(departureTimesAdapter)
            setText(departureTimesAdapter!!.getItem(0), false)
        }
        with(binding.departureStopText) {
            setAdapter(departureStopsAdapter)
            setText(departureStopsAdapter!!.getItem(0), false)
        }
        with(binding.arrivalStopText) {
            setAdapter(arrivalStopsAdapter)
            setText(arrivalStopsAdapter!!.getItem(0), false)
        }

        val c = Calendar.getInstance()
        val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH)
        val year = c.get(Calendar.YEAR)

        binding.datePickerText.setText("${day}/${month}/${year}")

        binding.datePickerText.setOnClickListener() {
            showDatePickerDialog()
        }

        binding.cancelButton.setOnClickListener() {
            findNavController().navigate(R.id.action_tripDetailsFragment_to_MyTripsFragment)
        }

        binding.saveButton.setOnClickListener() {
            findNavController().navigate(R.id.action_tripDetailsFragment_to_MyTripsFragment)
        }

        with (binding.qrCode){
            try {
                val barcodeEncoder = BarcodeEncoder()
                val bitmap = barcodeEncoder.encodeBitmap(R.string.details_trip_name.toString(), BarcodeFormat.QR_CODE, 400, 400)
                setImageBitmap(bitmap)
            } catch (e: Exception) {
            }
        }

    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, month, year ->
            onDateSelected(
                day,
                month,
                year
            )
        }
        datePicker.show(parentFragmentManager, "datePicker")
    }

    private fun onDateSelected(day: Int, month: Int, year: Int){
        binding.datePickerText.setText("${day}/${month}/${year}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.unnamedgroup.tourapp.view.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.unnamedgroup.tourapp.R
import com.unnamedgroup.tourapp.databinding.FragmentTripQrBinding
import com.unnamedgroup.tourapp.model.business.Passenger
import com.unnamedgroup.tourapp.model.business.Ticket
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class TripQrFragment : Fragment() {

    private var _binding: FragmentTripQrBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var passengersAdapter: ArrayAdapter<String>? = null
    private lateinit var currentTicket : Ticket
    private lateinit var currentPassenger : Passenger

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTripQrBinding.inflate(inflater, container, false)
        val bundle  = this.arguments
        currentTicket = bundle!!.getParcelable("Ticket")!!
        currentPassenger = currentTicket.passengers[0]
        initAdapters()
        return binding.root

    }

    private fun initAdapters() {
        val passengers = currentTicket.passengers
        val adapterPassengers : MutableList<String> = mutableListOf()

        for (p in passengers) {
            adapterPassengers.add(p.getFormatted())
        }

        passengersAdapter =
            context?.let { ArrayAdapter(it, R.layout.list_item, adapterPassengers) }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.trip.text = currentTicket.trip.getName()
        binding.tripTime.text = currentTicket.trip.getFormattedDepartureTime()

        setQrImage()

        with(binding.passengerText) {
            setAdapter(passengersAdapter)
            setText(passengersAdapter!!.getItem(0), false)
            setOnItemClickListener { _, _, position, _ ->
                val newPassenger = adapter.getItem(position) ?: ""
                currentPassenger = currentTicket.passengers.find { passenger -> passenger.getFormatted() == newPassenger.toString() }!!
                setQrImage()
            }
        }

        binding.backButton.setOnClickListener() {
            findNavController().popBackStack()
        }

        /*binding.shareButton.setOnClickListener(){
            try {
                var qrCodeData = "${currentTicket.id}-${currentPassenger.id}"
                val barcodeEncoder = BarcodeEncoder()
                val share = Intent(Intent.ACTION_SEND)
                share.type = "image/jpeg"
                val bitmap = barcodeEncoder.encodeBitmap(qrCodeData, BarcodeFormat.QR_CODE, 600, 600)
                val bytes = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                val f = File("${currentPassenger.name}-${currentPassenger.dni}.jpg")
                f.createNewFile()
                val fo = FileOutputStream(f)
                fo.write(bytes.toByteArray())
                share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporary_file.jpg"))
                startActivity(Intent.createChooser(share, "Share Image"))
            } catch (e: Exception) {
                Toast.makeText(context, getString(R.string.generate_qr_error), Toast.LENGTH_LONG).show()
            }
        }*/

    }

    private fun setQrImage(){
        with (binding.qrCode){
            try {
                var qrCodeData = "${currentTicket.id}-${currentPassenger.id}"
                val barcodeEncoder = BarcodeEncoder()
                val bitmap = barcodeEncoder.encodeBitmap(qrCodeData, BarcodeFormat.QR_CODE, 600, 600)
                setImageBitmap(bitmap)
            } catch (e: Exception) {
                Toast.makeText(context, getString(R.string.generate_qr_error), Toast.LENGTH_LONG).show()
            }
    }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
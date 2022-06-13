package com.unnamedgroup.tourapp.view.fragment

import android.content.Context
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
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.FileProvider
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
import java.util.*


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

        binding.shareButton.setOnClickListener(){
            try {
                // get qr
                val qrCodeData = "${currentTicket.id}-${currentPassenger.id}"
                val barcodeEncoder = BarcodeEncoder()
                val bitmap = barcodeEncoder.encodeBitmap(qrCodeData, BarcodeFormat.QR_CODE, 600, 600)

                // share qr

                val share = Intent(Intent.ACTION_SEND)
                share.type = "image/jpeg"

                val bmpUri : Uri = context?.let { c -> saveImage(bitmap, c.applicationContext) }!!
                share.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                share.putExtra(Intent.EXTRA_STREAM, bmpUri)
                startActivity(Intent.createChooser(share, "Share Image"))
            } catch (e: Exception) {
                Toast.makeText(context, getString(R.string.generate_qr_error), Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun setQrImage(){
        with (binding.qrCode){
            try {
                val qrCodeData = "${currentTicket.id}-${currentPassenger.id}"
                val barcodeEncoder = BarcodeEncoder()
                val bitmap = barcodeEncoder.encodeBitmap(qrCodeData, BarcodeFormat.QR_CODE, 600, 600)
                setImageBitmap(bitmap)
            } catch (e: Exception) {
                Toast.makeText(context, getString(R.string.generate_qr_error), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun saveImage(image: Bitmap, context: Context) : Uri? {
        val imagesFolder = File(context.cacheDir, "images")
        var uri: Uri? = null
        try {
         imagesFolder.mkdirs()
         val file = File(imagesFolder, "${currentPassenger.name}-${currentPassenger.dni}.jpg" )

         val stream = FileOutputStream(file)
         image.compress(Bitmap.CompressFormat.JPEG, 90, stream)
         stream.flush()
         uri = FileProvider.getUriForFile(requireContext(), "com.unnamedgroup.tourapp.client" +".provider", file)

        } catch (e: Exception){
            Toast.makeText(context, getString(R.string.generate_qr_error), Toast.LENGTH_LONG).show()
        }
        return uri
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
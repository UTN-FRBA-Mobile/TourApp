package com.unnamedgroup.tourapp.view.fragment

import android.app.Activity.RESULT_OK
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.unnamedgroup.tourapp.R
import com.unnamedgroup.tourapp.databinding.FragmentConfirmTripBinding
import com.unnamedgroup.tourapp.model.business.Passenger
import com.unnamedgroup.tourapp.model.business.Ticket
import com.unnamedgroup.tourapp.model.business.Trip
import com.unnamedgroup.tourapp.presenter.implementation.ConfirmPresenterImpl
import com.unnamedgroup.tourapp.presenter.implementation.TripDetailsPresenterImpl
import com.unnamedgroup.tourapp.presenter.interfaces.ConfirmPresenterInt
import com.unnamedgroup.tourapp.presenter.interfaces.TripDetailsPresenterInt
import com.unnamedgroup.tourapp.view.adapter.ConfirmTripPassengersAdapter
import com.unnamedgroup.tourapp.view.adapter.ConfirmTripTicketsAdapter
import org.apache.commons.io.FileUtils
import java.io.File


class ConfirmTripFragment : Fragment(), TripDetailsPresenterInt.View, ConfirmPresenterInt.View {

    private lateinit var recyclerView: RecyclerView


    private var _binding: FragmentConfirmTripBinding? = null

    private val binding get() = _binding!!

    private var tickets = mutableListOf<Ticket>()
    private var ticket: Ticket? = null
    private var ticket2: Ticket? = null
    private var numberOfTicketsOk = 0
    private var numberOfTripsOk = 0


    private var tripDetailsPresenterInt = TripDetailsPresenterImpl(this)
    private var confirmPresenterInt = ConfirmPresenterImpl(this)
    private var file : Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ticket = arguments?.getParcelable<Ticket>("Ticket")

        ticket?.let {
            tickets.add(it)
        }

        ticket2 = arguments?.getParcelable<Ticket>("Ticket2")
        ticket2?.let {
            tickets.add(it)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Complete recyclerView of tickets
        _binding = FragmentConfirmTripBinding.inflate(inflater, container, false)

        var viewManager = LinearLayoutManager(this.context)
        val viewAdapterTickets = ConfirmTripTicketsAdapter(tickets,this.context)

        recyclerView = binding.confirmTripTicketsReciclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapterTickets
        }

        // Complete recyclerView of passengers
        var value = 0F
        val passengers : MutableList<Passenger> = mutableListOf()
        tickets.forEach{
            value += it.passengers.size * it.trip.price
            passengers.addAll(it.passengers)
        }

        viewManager = LinearLayoutManager(this.context)
        val viewAdapterPassengers = ConfirmTripPassengersAdapter(passengers)
        recyclerView = binding.confirmTripPassengersReciclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapterPassengers
        }

        binding.valueImport.text = value.toString()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.confirmButton.setOnClickListener {
            if(tickets[0].isPaid()){
                tickets[0].trip.passengersAmount    -=  tickets[0].passengers.size
                confirmPresenterInt.modifyTrip(tickets[0].trip)
            }
            else{
                Toast.makeText(context, getString(R.string.invalidReciep), Toast.LENGTH_SHORT).show()
            }

        }

        binding.bankCbu.setEndIconOnClickListener {
            copyData(binding.bankCbuTextfield.text.toString(), "CBU")
        }

        binding.bankAlias.setEndIconOnClickListener {

            copyData(binding.bankAliasTextfield.text.toString(), "Alias")

        }

        binding.bankUploadReceipt.setEndIconOnClickListener {
            val intent = Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)
        }
    }

    override fun onModifyTicketOk(ticket: Ticket) {
        numberOfTicketsOk++
        if (numberOfTripsOk == 2 && numberOfTicketsOk == 1){
            //TODO Cambiar el it.trip.id == ticket.trip.id por it.trip.id != ticket.trip.id cuando cambien en el generar viaja
            tripDetailsPresenterInt.addTicket( tickets[1])
        }

        when (tickets.size){
            1 -> findNavController().navigate(R.id.action_confirmTripFragment_to_resultScreenFragment)
            2 -> if (numberOfTicketsOk == 2) findNavController().navigate(R.id.action_confirmTripFragment_to_resultScreenFragment)
            else -> findNavController().navigate(R.id.action_confirmTripFragment_to_resultScreenFragment)
        }

        Firebase.messaging.subscribeToTopic("trip" + ticket.trip.id.toString())
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Toast.makeText(context, "No se pudo suscribir al viaje. No recibirá alertas ante cambios de estado.", Toast.LENGTH_SHORT).show()
                }
            }

    }
    override fun onModifyTicketFailed(error: String) {
        Toast.makeText(context, "Error al insertar Viaje", Toast.LENGTH_SHORT).show()
    }

    override fun getTicketByTripIdOk(ticket: Ticket) {
        TODO("Not yet implemented")
    }

    override fun getTicketByTripIdFailed(error: String) {
        TODO("Not yet implemented")
    }


    override fun onModifyTripOk(trip: Trip) {
        numberOfTripsOk++
        //TODO Cambiar el trip.id == it.trip.id && trip.origin == it.trip.origin por trip.id == it.trip.id cuando cambien en el generar viaja
        if (numberOfTripsOk == 1) tripDetailsPresenterInt.addTicket( tickets.filter { trip.id == it.trip.id && trip.origin == it.trip.origin }.first())

        if(tickets.size == 2 && numberOfTripsOk == 1){
            tickets[1].trip.passengersAmount    -=  tickets[1].passengers.size
            confirmPresenterInt.modifyTrip(tickets[1].trip)
        }
    }

    override fun onModifyTripFailed(error: String) {
        Toast.makeText(context, "Error al modificar Viaje", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 111 && resultCode == RESULT_OK) {
            file = data?.data //The uri with the location of the file
            val fileName = getFileNameFromUri(requireContext(),file!!)
            binding.bankUploadReceiptTextfield.setText(fileName)
            val fileToWork = createFileFromUri(fileName!!,file!!)
            val receipt = fileToWork?.let { convertToBase64(it) }
            tickets.forEach{it.receipt = receipt}
            Toast.makeText(context, "Comprobante Seleccionado", Toast.LENGTH_SHORT).show()

        }
    }

    private fun copyData(text: String, data: String) {
        val myClipboard: ClipboardManager =
            requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", text)
        myClipboard.setPrimaryClip(clipData)
        Toast.makeText(context, "$data Copiado al portapapeles", Toast.LENGTH_SHORT).show()
    }

    private fun getFileNameFromUri(context: Context, uri: Uri): String? {
        val fileName: String?
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.moveToFirst()
        fileName = cursor?.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME).toInt())
        cursor?.close()
        return fileName
    }

    private fun  createFileFromUri(name: String, uri: Uri): File? {
        return try {
            val stream = context?.contentResolver?.openInputStream(uri)
            val file =
                File.createTempFile(
                    "${name}_${System.currentTimeMillis()}",
                    ".png",
                    context?.cacheDir
                )
            FileUtils.copyInputStreamToFile(stream, file)  // Use this one import org.apache.commons.io.FileUtils
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun convertToBase64(attachment: File): String {
        return Base64.encodeToString(attachment.readBytes(), Base64.NO_WRAP)
    }
}

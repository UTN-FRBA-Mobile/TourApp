package com.unnamedgroup.tourapp.view.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unnamedgroup.tourapp.R
import com.unnamedgroup.tourapp.databinding.FragmentConfirmTripBinding
import com.unnamedgroup.tourapp.model.business.Ticket
import com.unnamedgroup.tourapp.presenter.implementation.TripDetailsPresenterImpl
import com.unnamedgroup.tourapp.presenter.interfaces.TripDetailsPresenterInt
import com.unnamedgroup.tourapp.utils.Utils
import com.unnamedgroup.tourapp.view.adapter.ConfirmTripPassengersAdapter
import java.util.*

class ConfirmTripFragment : Fragment(), TripDetailsPresenterInt.View {

    private lateinit var recyclerView: RecyclerView


    private var _binding: FragmentConfirmTripBinding? = null

    private val binding get() = _binding!!

    private var ticket: Ticket? = null
    private var tripDetailsPresenterInt: TripDetailsPresenterInt = TripDetailsPresenterImpl(this)


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
            tripDetailsPresenterInt.addTicket(ticket!!)
        }

        binding.bankCbu.setEndIconOnClickListener {
            copyData(binding.bankCbuTextfield.text.toString(), "CBU")
        }



        binding.bankAlias.setEndIconOnClickListener {

            copyData(binding.bankCbuTextfield.text.toString(), "Alias")

        }



    }

    private fun copyData(text: String, data: String) {
        val myClipboard: ClipboardManager =
            requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", text)
        myClipboard.setPrimaryClip(clipData)
        Toast.makeText(context, "$data Copiado al portapapeles", Toast.LENGTH_SHORT).show()
    }

    override fun onModifyTicketOk(ticket: Ticket) {
        findNavController().navigate(R.id.action_confirmTripFragment_to_resultScreenFragment)
    }

    override fun onModifyTicketFailed(error: String) {
        Toast.makeText(context, getString(R.string.get_trips_error), Toast.LENGTH_SHORT).show()
    }

}

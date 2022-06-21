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
import com.unnamedgroup.tourapp.R
import com.unnamedgroup.tourapp.databinding.FragmentConfirmTripBinding
import com.unnamedgroup.tourapp.model.business.Ticket
import com.unnamedgroup.tourapp.presenter.implementation.TripDetailsPresenterImpl
import com.unnamedgroup.tourapp.presenter.interfaces.TripDetailsPresenterInt
import com.unnamedgroup.tourapp.utils.Utils
import com.unnamedgroup.tourapp.view.adapter.ConfirmTripPassengersAdapter
import org.apache.commons.io.FileUtils
import java.io.File


class ConfirmTripFragment : Fragment(), TripDetailsPresenterInt.View {

    private lateinit var recyclerView: RecyclerView


    private var _binding: FragmentConfirmTripBinding? = null

    private val binding get() = _binding!!

    private var ticket: Ticket? = null
    private var ticket2: Ticket? = null

    private var tripDetailsPresenterInt: TripDetailsPresenterInt = TripDetailsPresenterImpl(this)
    private var file : Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ticket = arguments?.getParcelable<Ticket>("Ticket")!!
        //ticket2 = arguments?.getParcelable<Ticket>("Ticket2")!!

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
            binding.valueImport.text = (it.trip.price *it.passengers.size).toString()
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
        findNavController().navigate(R.id.action_confirmTripFragment_to_resultScreenFragment)
    }

    override fun onModifyTicketFailed(error: String) {
        Toast.makeText(context, getString(R.string.get_trips_error), Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 111 && resultCode == RESULT_OK) {
            file = data?.data //The uri with the location of the file
            val fileName = getFileNameFromUri(requireContext(),file!!)
            binding.bankUploadReceiptTextfield.setText(fileName)
            val fileToWork = createFileFromUri(fileName!!,file!!)
            ticket?.receipt = fileToWork?.let { convertToBase64(it) }
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

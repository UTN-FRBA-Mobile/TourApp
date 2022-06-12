package com.unnamedgroup.tourapp.view.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ScanMode
import com.unnamedgroup.tourapp.R
import com.unnamedgroup.tourapp.databinding.FragmentQrScannerBinding
import com.unnamedgroup.tourapp.model.business.Trip

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class QrScannerFragment : Fragment() {

    private var _binding: FragmentQrScannerBinding? = null
    private val binding get() = _binding!!
    private lateinit var codeScanner: CodeScanner
    private var trip: Trip? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQrScannerBinding.inflate(inflater, container, false)

        if (arguments == null || !requireArguments().containsKey("Trip")) {
            val bundle = Bundle()
            bundle.putParcelable("Trip", trip)
            findNavController().navigate(R.id.action_QrScannerFragment_to_TripDetailsDriverFragment, bundle)
        }
        trip = requireArguments().getParcelable("Trip")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (
            context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.CAMERA) } == PackageManager.PERMISSION_DENIED
        ){
            val permissionLauncher = registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                if (isGranted) {
                    startScanning()
                }
                else {
                    Toast.makeText(context, getString(R.string.must_grant_permission), Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    bundle.putParcelable("Trip", trip)
                    findNavController().navigate(R.id.action_QrScannerFragment_to_TripDetailsDriverFragment, bundle)
                }
            }
            permissionLauncher.launch(Manifest.permission.CAMERA)
        } else{
            startScanning()
        }

    }

    private fun startScanning() {
        val activity = requireActivity()
        val scannerView: CodeScannerView = binding.scannerView
        codeScanner = context?.let { CodeScanner(it, scannerView) }!!
        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false

        codeScanner.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
                val scanResult = it.text
                val bundle = Bundle()
                val splitList = scanResult.split('-')
                if (splitList.size != 2) {
                    Toast.makeText(context, getString(R.string.invalid_qr), Toast.LENGTH_SHORT).show()
                } else if (splitList[0].toInt() != trip!!.id) {
                    Toast.makeText(context, getString(R.string.qr_from_another_trip), Toast.LENGTH_SHORT).show()
                } else {
                    bundle.putString("ScanResult", splitList[1])
                    bundle.putParcelable("Trip", trip)
                    findNavController().navigate(R.id.action_QrScannerFragment_to_TripDetailsDriverFragment, bundle)
                }
            }
        }

        scannerView.setOnClickListener{
            codeScanner.startPreview()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        if(::codeScanner.isInitialized){
            codeScanner.startPreview()
        }
    }

    override fun onPause() {
        if(::codeScanner.isInitialized){
            codeScanner.releaseResources()
        }
        super.onPause()
    }
}
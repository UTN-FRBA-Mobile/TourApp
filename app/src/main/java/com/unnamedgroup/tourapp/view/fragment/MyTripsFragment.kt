package com.unnamedgroup.tourapp.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.unnamedgroup.tourapp.R
import com.unnamedgroup.tourapp.databinding.FragmentMyTripsBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MyTripsFragment : Fragment() {

    private var _binding: FragmentMyTripsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMyTripsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_MyTripsFragment_to_FragmentNewTrip)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
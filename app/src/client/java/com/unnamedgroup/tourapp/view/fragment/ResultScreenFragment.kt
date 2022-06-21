package com.unnamedgroup.tourapp.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.unnamedgroup.tourapp.R
import com.unnamedgroup.tourapp.databinding.FragmentResultScreenBinding


class ResultScreenFragment : Fragment() {

    private var _binding: FragmentResultScreenBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentResultScreenBinding.inflate(inflater, container, false)
        return binding.root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonContinue.setOnClickListener {
            findNavController().navigate(R.id.action_resultScreenFragment_to_MyTripsFragment)
        }
    }


}
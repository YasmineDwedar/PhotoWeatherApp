package com.example.photoweatherapp.ui.history

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.photoweatherapp.R
import com.example.photoweatherapp.databinding.FragmentHistoryBinding
import com.example.photoweatherapp.ui.base.BaseFragment
import com.example.photoweatherapp.util.LocationProvider
import pub.devrel.easypermissions.EasyPermissions


class HistoryFragment : BaseFragment<FragmentHistoryBinding>(R.layout.fragment_history),
    LocationProvider.GetLocationCoordinatesCallBack {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun setListeners() {
        binding?.fabTakePhoto?.setOnClickListener {
            openCamera()
//            getCurrentLocation()


        }

    }

    override fun openCamera() {
        findNavController().navigate(R.id.action_historyFragment_to_photoFragment)
    }

    override fun getCurrentLocation() {
        val locationProvider = LocationProvider(requireContext(), this)
        locationProvider.requestNewLocationData()
    }


    override fun setObservers() {
    }

    override fun initializeViewModel() {
    }

    override fun sendCoordinates(long: Double, lat: Double) {
        //send these to vieModel
        Toast.makeText(
            requireContext(),
            "lat" + lat.toString() + "long" + long.toString(),
            Toast.LENGTH_SHORT
        ).show()
    }


}
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


class HistoryFragment : BaseFragment<FragmentHistoryBinding>(R.layout.fragment_history)
   {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun setListeners() {
        binding?.fabTakePhoto?.setOnClickListener {
//            openTheCamera()
openCameraAfterPermissionGranted()


        }

    }

    override fun openTheCamera() {
//        findNavController().navigate(R.id.action_historyFragment_to_photoFragment)
        findNavController().navigate(R.id.action_historyFragment_to_captureFragment)
    }




    override fun setObservers() {
    }

    override fun initializeViewModel() {
    }




}
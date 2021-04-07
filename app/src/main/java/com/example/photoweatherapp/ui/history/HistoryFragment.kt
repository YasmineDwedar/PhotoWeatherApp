package com.example.photoweatherapp.ui.history

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.photoweatherapp.R
import com.example.photoweatherapp.data.local.WeatherInfo
import com.example.photoweatherapp.databinding.FragmentHistoryBinding
import com.example.photoweatherapp.models.Sys
import com.example.photoweatherapp.models.Wind
import com.example.photoweatherapp.ui.base.BaseFragment
import com.example.photoweatherapp.ui.photo.PhotoFragmentViewModel
import com.example.photoweatherapp.util.Constants
import com.example.photoweatherapp.util.LocationProvider
import kotlinx.coroutines.InternalCoroutinesApi
import pub.devrel.easypermissions.EasyPermissions


class HistoryFragment : BaseFragment<FragmentHistoryBinding>(R.layout.fragment_history) {
    private val userAdapter = HistoryAdapter()

    @InternalCoroutinesApi
    private var viewModel: HistoryFragmentViewModel? = null

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
            getUserCurrentLocation()


        }

    }

    override fun openTheCamera() {
//        findNavController().navigate(R.id.action_historyFragment_to_photoFragment)
        findNavController().navigate(R.id.action_historyFragment_to_captureFragment)
    }

    override fun getCurrentLocation() {
        super.getCurrentLocation()
    }


    @InternalCoroutinesApi
    override fun setObservers() {
//        var arr= arrayListOf(WeatherInfo(293.11,283.11,"Cairo", Sys("EG"), Wind(5.66),"ghj",1),
//            WeatherInfo(293.11,283.11,"Alex", Sys("EG"), Wind(5.14),Constants.IMG,2),
//            WeatherInfo(293.11,283.11,"Alex", Sys("EG"), Wind(5.14),"ghj",2),
//            WeatherInfo(293.11,283.11,"Alex", Sys("EG"), Wind(5.14),"ghj",2))
//        userAdapter.submitList(arr)
//        binding?.emptyView?.visibility = View.GONE
//            buildUsersRV()
        viewModel?.weatherListLiveData?.observe(requireActivity(), {
            userAdapter.submitList(it)

            binding?.emptyView?.visibility = View.GONE
            buildUsersRV()
        })

    }

    @InternalCoroutinesApi
    override fun initializeViewModel() {
        viewModel = ViewModelProvider(this).get(HistoryFragmentViewModel::class.java)

    }

    private fun buildUsersRV() {

        val layoutManager = GridLayoutManager(activity, 2)
        binding?.rvHistory?.layoutManager = layoutManager
        userAdapter?.notifyDataSetChanged()

        binding?.rvHistory?.adapter = userAdapter
    }


}
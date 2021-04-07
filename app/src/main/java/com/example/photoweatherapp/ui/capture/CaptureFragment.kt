package com.example.photoweatherapp.ui.capture

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.photoweatherapp.R
import com.example.photoweatherapp.databinding.FragmentCaptureBinding
import com.example.photoweatherapp.ui.base.BaseFragment
import com.example.photoweatherapp.ui.photo.PhotoFragmentViewModel
import com.example.photoweatherapp.util.LocationProvider
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.PictureResult
import kotlinx.android.synthetic.main.fragment_capture.*


class CaptureFragment : BaseFragment<FragmentCaptureBinding>(R.layout.fragment_capture),
    LocationProvider.GetLocationCoordinatesCallBack  {

    private var viewModel: PhotoFragmentViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getCurrentLocation()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

    }

    override fun setListeners() {

        binding?.camView?.addCameraListener(object : CameraListener() {
            override fun onPictureTaken(result: PictureResult) {
                result.toBitmap(camView.width, camView.height) {
                    Log.d("TAG", "onPictureTaken: ${it}")
                    binding?.apply {
                        takeImage_group.visibility = View.GONE
                        imageView.visibility = View.VISIBLE
                        imageView.setImageBitmap(it)
                    }
                }

                val data = result.data
            }
        })


        binding?.camView?.open()
        binding?.btnCapturePhoto?.setOnClickListener{
            camView.takePicture()
        }

    }



    override fun setObservers() {
        viewModel?.networkState?.observe(viewLifecycleOwner, {
            handleError(it)
        })

        viewModel?.locationLiveData?.observe(viewLifecycleOwner, {
            hideProgress()
            binding?.weather = it
            binding?.apply {
                tvLocation.visibility=View.VISIBLE
                ivWeatherIcon.setImageResource(R.drawable.sample_weather_icon)
            }
        })
    }

    override fun initializeViewModel() {
        viewModel = ViewModelProvider(this).get(PhotoFragmentViewModel::class.java)

    }


    fun showProgress() {
        binding?.progreessBar?.visibility = View.VISIBLE
    }

    fun hideProgress() {
        binding?.progreessBar?.visibility = View.INVISIBLE
    }

    override fun sendCoordinates(long: Double, lat: Double) {
        //send to vm
        viewModel?.getWeatherData(lat, long)
//        Toast.makeText(requireContext(), "lat" + lat.toString() + "long" + long.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun getCurrentLocation() {

        val locationProvider = LocationProvider(requireContext(), this)
        locationProvider.requestNewLocationData()
    }
}
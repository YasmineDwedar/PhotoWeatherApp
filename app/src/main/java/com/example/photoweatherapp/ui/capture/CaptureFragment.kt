package com.example.photoweatherapp.ui.capture

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.example.photoweatherapp.R
import com.example.photoweatherapp.data.local.WeatherInfo
import com.example.photoweatherapp.databinding.FragmentCaptureBinding
import com.example.photoweatherapp.models.WeatherResponse
import com.example.photoweatherapp.ui.base.BaseFragment
import com.example.photoweatherapp.util.ImageLoader
import com.example.photoweatherapp.util.LocationProvider
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.PictureResult
import kotlinx.android.synthetic.main.fragment_capture.*
import kotlinx.coroutines.InternalCoroutinesApi
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class CaptureFragment : BaseFragment<FragmentCaptureBinding>(R.layout.fragment_capture),
    LocationProvider.GetLocationCoordinatesCallBack {

    private var viewModel: CaptureFragmentViewModel? = null
    private var imagePath: File? = null
 lateinit var  img :String
    private var weatherResponse: WeatherResponse? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getCurrentLocation()
        getUserCurrentLocation()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        binding?.camView?.open()

    }

    override fun onPause() {
        super.onPause()
        binding?.camView?.close()
    }
    @InternalCoroutinesApi
    override fun setListeners() {

        binding?.camView?.addCameraListener(object : CameraListener() {
            override fun onPictureTaken(result: PictureResult) {
                result.toBitmap(camView.width, camView.height) {
                    Log.d("TAG", "onPictureTaken: ${it}")
                    binding?.apply {
//                        takeImage_group.visibility = View.GONE
                        camView.visibility = View.GONE
                        btnCapturePhoto.visibility = View.GONE
                        ivCaptured.visibility = View.VISIBLE
                        ivCaptured.setImageBitmap(it)
                        img = ImageLoader.BitMapToString(it!!)
                        btnSharePhoto.visibility = View.VISIBLE
                        //MakeScreenshot
                    }
                }

//                val data = result.data
            }
        })




        binding?.apply {
            btnCapturePhoto.setOnClickListener {
                camView.takePicture()
            }
            btnSharePhoto.setOnClickListener {
                btnSharePhoto.visibility = View.INVISIBLE
                val lay = requireActivity().findViewById(R.id.root_layout) as ConstraintLayout
                val screenshot: Bitmap = getScreenShot(lay)
                Log.d("TAG", "onPictureTaken: ${screenshot}")
                saveBitmap(screenshot)
                Log.d("TAG", "setListeners: global zz ${weatherResponse?.sys?.country}")
                weather?.let {
                    val weatherInfo = WeatherInfo(
                        it.main.feels_like,
                        it.main.temp,
                        it.name,
                        it.sys,
                        it.wind,
                        img
                    )
                    viewModel?.insertWeatherInfo(weatherInfo)
                }

                shareIt()

            }
        }

    }


    fun getScreenShot(view: View): Bitmap {
        val screenView = view
        screenView.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(screenView.drawingCache)
        screenView.isDrawingCacheEnabled = false
        return bitmap
    }

    override fun setObservers() {
        showProgress()
        binding?.btnCapturePhoto?.visibility=View.GONE

        viewModel?.networkState?.observe(viewLifecycleOwner, {
            handleError(it)
        })

        viewModel?.locationLiveData?.observe(viewLifecycleOwner, {
            hideProgress()
            weatherResponse = it
            binding?.weather = it
            binding?.apply {
                btnCapturePhoto.visibility=View.VISIBLE
                tvLocation.visibility = View.VISIBLE
                ivWeatherIcon.setImageResource(R.drawable.sample_weather_icon)
            }
        })
    }

    override fun initializeViewModel() {
        viewModel = ViewModelProvider(this).get(CaptureFragmentViewModel::class.java)

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

    private fun shareIt() {
        val uri = Uri.fromFile(imagePath)
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "image/*"
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "")
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "")
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri)
        try {
            startActivity(Intent.createChooser(sharingIntent, "Share via"))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "No App Available", Toast.LENGTH_SHORT).show()
        }
    }

    fun saveBitmap(bitmap: Bitmap) {
        imagePath = File(requireContext().getExternalCacheDir()?.absolutePath + "/screenshot.png")
        val fos: FileOutputStream
        try {
            fos = FileOutputStream(imagePath)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
        } catch (e: FileNotFoundException) {
            Log.e("GREC", e.message, e)
        } catch (e: IOException) {
            Log.e("GREC", e.message, e)
        }
    }



}
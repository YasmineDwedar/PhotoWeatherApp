package com.example.photoweatherapp.ui.capture

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.example.photoweatherapp.R
import com.example.photoweatherapp.databinding.FragmentCaptureBinding
import com.example.photoweatherapp.ui.base.BaseFragment
import com.example.photoweatherapp.ui.photo.PhotoFragmentViewModel
import com.example.photoweatherapp.util.ImageLoader
import com.example.photoweatherapp.util.LocationProvider
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.PictureResult
import kotlinx.android.synthetic.main.fragment_capture.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class CaptureFragment : BaseFragment<FragmentCaptureBinding>(R.layout.fragment_capture),
    LocationProvider.GetLocationCoordinatesCallBack {

    private var viewModel: PhotoFragmentViewModel? = null
    private var imagePath: File? = null

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

    }

    override fun setListeners() {
        binding?.camView?.open()
        binding?.camView?.addCameraListener(object : CameraListener() {
            override fun onPictureTaken(result: PictureResult) {
                result.toBitmap(camView.width, camView.height) {
                    Log.d("TAG", "onPictureTaken: ${it}")
                    binding?.apply {
                        takeImage_group.visibility = View.GONE
                        ivCaptured.visibility = View.VISIBLE
                        ivCaptured.setImageBitmap(it)
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
                btnSharePhoto.visibility=View.INVISIBLE
                Toast.makeText(requireContext(), "share now", Toast.LENGTH_SHORT).show()
//                sharePhoto()
                //ScreenShot
                val lay = requireActivity().findViewById(R.id.root_layout) as ConstraintLayout
                val screenshot: Bitmap = getScreenShot(lay)
                Log.d("TAG", "onPictureTaken: ${screenshot}")

                saveBitmap(screenshot)
             var stringImg=   ImageLoader.BitMapToString(screenshot)
                //save it to roomDB
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
        viewModel?.networkState?.observe(viewLifecycleOwner, {
            handleError(it)
        })

        viewModel?.locationLiveData?.observe(viewLifecycleOwner, {
            hideProgress()
            binding?.weather = it
            binding?.apply {
                tvLocation.visibility = View.VISIBLE
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

//    fun store(bm: Bitmap, fileName: String?) {
//        val dirPath: String =
//            Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/Screenshots"
//        val dir = File(dirPath)
//        if (!dir.exists()) dir.mkdirs()
//        val file = File(dirPath, fileName)
//        try {
//            val fOut = FileOutputStream(file)
//            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut)
//            fOut.flush()
//            fOut.close()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }

    //    private fun shareImage(file: File) {
//        val uri: Uri = Uri.fromFile(file)
//        val intent = Intent()
//        intent.action = Intent.ACTION_SEND
//        intent.type = "image/*"
//        intent.putExtra(Intent.EXTRA_SUBJECT, "")
//        intent.putExtra(Intent.EXTRA_TEXT, "")
//        intent.putExtra(Intent.EXTRA_STREAM, uri)
//        try {
//            startActivity(Intent.createChooser(intent, "Share Screenshot"))
//        } catch (e: ActivityNotFoundException) {
//            Toast.makeText(context, "No App Available", Toast.LENGTH_SHORT).show()
//        }
//    }
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
        imagePath = File(Environment.getExternalStorageDirectory().toString() + "/screenshot.png")
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
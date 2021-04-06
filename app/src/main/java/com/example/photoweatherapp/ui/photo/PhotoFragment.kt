package com.example.photoweatherapp.ui.photo

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.HandlerThread
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.photoweatherapp.R
import com.example.photoweatherapp.databinding.FragmentPhotoBinding
import com.example.photoweatherapp.ui.base.BaseFragment
import com.example.photoweatherapp.util.CameraInteractionListener
import com.example.photoweatherapp.util.Constants
import com.example.photoweatherapp.util.LocationProvider
import kotlinx.android.synthetic.main.fragment_photo.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class PhotoFragment : BaseFragment<FragmentPhotoBinding>(R.layout.fragment_photo),
    CameraInteractionListener, LocationProvider.GetLocationCoordinatesCallBack {

    companion object {
        private const val IMAGE_FORMAT = ".jpg"
        private val TAG = "PhotoFragment"
        private const val CAMERA_BACKGROUND_THREAD_NAME = "camera_background_thread"
    }

    val REQUEST_CODE = 200
    private val cameraInteractionListener: CameraInteractionListener? = null

    //    private var cameraManager: CameraManager? =null
    private var viewModel: PhotoFragmentViewModel? = null
    private val cameraFacing: Int = CameraCharacteristics.LENS_FACING_BACK
    private var surfaceTextureListener: TextureView.SurfaceTextureListener? = null
    private var previewSize: Size? = null
    private var cameraId: String? = null
    private var backgroundHandler: Handler? = null
    private var stateCallback: CameraDevice.StateCallback? = null
    private var cameraDevice: CameraDevice? = null
    private var backgroundThread: HandlerThread? = null
    private var captureRequestBuilder: CaptureRequest.Builder? = null
    private var captureRequest: CaptureRequest? = null
    private var cameraCaptureSession: CameraCaptureSession? = null
    private var imageFile: File? = null


    private fun setCameraStateCallback() {
        stateCallback = object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                cameraDevice = camera
                createCameraPreview()
            }

            override fun onDisconnected(camera: CameraDevice) {
                camera.close()

            }

            override fun onError(camera: CameraDevice, error: Int) {
                cameraDevice = null
                camera.close()

            }

        }
    }

    private fun setTextureListener() {
        surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(
                surfaceTexture: SurfaceTexture,
                width: Int,
                height: Int
            ) {
//                setUpCamera()
                openCamera()
            }

            override fun onSurfaceTextureSizeChanged(
                surfaceTexture: SurfaceTexture,
                width: Int,
                height: Int
            ) {
                // no impl needed
            }

            override fun onSurfaceTextureDestroyed(surfaceTexture: SurfaceTexture): Boolean {
                return false
            }

            override fun onSurfaceTextureUpdated(surfaceTexture: SurfaceTexture) {
                // no impl needed
            }
        }
    }

//    private fun setUpCamera() {
//        try {
//            for (cameraId in cameraManager!!.cameraIdList) {
//                val cameraCharacteristics = cameraManager!!.getCameraCharacteristics(cameraId)
//                if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == cameraFacing) {
//                    val streamConfigurationMap = cameraCharacteristics.get(
//                        CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP
//                    )
//                    if (streamConfigurationMap != null) {
//                        previewSize =
//                            streamConfigurationMap.getOutputSizes(SurfaceTexture::class.java)[0]
//                        this.cameraId = cameraId
//                    }
//                }
//            }
//        } catch (e: CameraAccessException) {
//            e.printStackTrace()
//        }
//    }


    @SuppressLint("MissingPermission")
    fun openCamera() {
        var cameraManager = activity?.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            for (cameraId in cameraManager.cameraIdList) {
                val cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId)
                if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == cameraFacing) {
                    val streamConfigurationMap = cameraCharacteristics.get(
                        CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP
                    )
                    if (streamConfigurationMap != null) {
                        previewSize =
                            streamConfigurationMap.getOutputSizes(SurfaceTexture::class.java)[0]
                        this.cameraId = cameraId
                    }
                }
            }


            cameraManager.openCamera(cameraId!!, stateCallback!!, backgroundHandler)

        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun openBackgroundThread() {
        backgroundThread = HandlerThread(CAMERA_BACKGROUND_THREAD_NAME)
        backgroundThread?.start()
        backgroundThread?.looper?.let {
            backgroundHandler = Handler(it)
        }
    }

    private fun createImageGallery(): File {
        val storageDirectory = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val galleryFolder = File(storageDirectory, context?.resources?.getString(R.string.app_name))
        if (!galleryFolder.exists()) {
            val wasCreated = galleryFolder.mkdirs()
            if (!wasCreated) {
                Log.e("CapturedImages", "Failed to create directory")
            }
        }
        return galleryFolder
    }

    @Throws(IOException::class)
    private fun createImageFile(galleryFolder: File): File {
        val timeStamp = SimpleDateFormat("yyyy_MM_dd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "image_" + timeStamp + "_"
        return File.createTempFile(imageFileName, IMAGE_FORMAT, galleryFolder)
    }

    private fun createCameraPreview() {
        try {
            val surfaceTexture = binding?.textureView?.surfaceTexture
            surfaceTexture!!.setDefaultBufferSize(previewSize!!.width, previewSize!!.height)
            val previewSurface = Surface(surfaceTexture)
            captureRequestBuilder =
                cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            captureRequestBuilder!!.addTarget(previewSurface)

            cameraDevice?.createCaptureSession(
                listOf(previewSurface), object : CameraCaptureSession.StateCallback() {
                    override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                        if (cameraDevice == null) {
                            return
                        }
                        try {
                            captureRequest = captureRequestBuilder!!.build()
                            this@PhotoFragment.cameraCaptureSession = cameraCaptureSession
                            this@PhotoFragment.cameraCaptureSession!!.setRepeatingRequest(
                                captureRequest!!,
                                null, backgroundHandler
                            )
                        } catch (e: CameraAccessException) {
                            e.printStackTrace()
                        }
                    }

                    override fun onConfigureFailed(cameraCaptureSession: CameraCaptureSession) {
                        // no impl needed
                    }
                }, backgroundHandler
            )
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }


    fun capturePhoto() {
        lifecycleScope.launch(Dispatchers.IO) {
            var outputPhoto: FileOutputStream? = null
            var errorMessage: String? = null
            try {
                imageFile = createImageFile(createImageGallery())
                outputPhoto = FileOutputStream(imageFile)
                binding?.textureView?.bitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputPhoto)
            } catch (e: Exception) {
                errorMessage = e.localizedMessage
                Log.e(TAG, "${e.message}")
            } finally {
                try {
                    outputPhoto?.close()
                } catch (e: IOException) {
                    Log.e(TAG, "${e.message}")
                    errorMessage = e.localizedMessage
                }
                lifecycleScope.launch(Dispatchers.Main) {
                    if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED) && imageFile != null) {
                        cameraInteractionListener?.onPhotoCaptureSuccess(imageFile)
                    } else {
                        cameraInteractionListener?.onPhotoCaptureFailure(errorMessage)
                    }
                }
            }
        }
    }

    private fun closeCamera() {
        cameraDevice?.close()
        cameraCaptureSession?.close()
        cameraDevice = null
        cameraCaptureSession = null
    }

    private fun closeBackgroundThread() {
        backgroundThread?.quitSafely()
        backgroundThread = null
        backgroundHandler = null
    }


    override fun onResume() {
        super.onResume()
        openBackgroundThread()
        if (binding?.textureView!!.isAvailable) {
//            setUpCamera()
            openCamera()
        } else {
            binding?.textureView?.surfaceTextureListener = surfaceTextureListener
        }
    }

    override fun onStop() {
        super.onStop()
        closeCamera()
        closeBackgroundThread()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getCurrentLocation()
        setTextureListener()
        setCameraStateCallback()

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun setListeners() {
        binding?.apply {
            btnCapturePhoto.setOnClickListener {
                capturePhoto()
                Toast.makeText(activity, "capture photo", Toast.LENGTH_SHORT).show()
//                binding?.btnCapturePhoto.visibility=View.INVISIBLE
            }
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

    override fun onPhotoCaptureSuccess(imageFile: File?) {
        hideProgress()
    }

    override fun onPhotoCaptureFailure(errorMessage: String?) {
        hideProgress()
        Toast.makeText(requireContext(), "Process failed..", Toast.LENGTH_SHORT).show()
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
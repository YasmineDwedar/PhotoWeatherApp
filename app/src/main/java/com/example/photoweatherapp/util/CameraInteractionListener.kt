package com.example.photoweatherapp.util

import java.io.File

/**
 * Created by Yasmine on April,2021
 */

interface CameraInteractionListener {
    fun onPhotoCaptureSuccess(imageFile: File?)
    fun onPhotoCaptureFailure(errorMessage: String?)
}
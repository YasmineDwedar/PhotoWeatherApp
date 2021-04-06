package com.example.photoweatherapp.ui.photo

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.photoweatherapp.R
import com.example.photoweatherapp.databinding.FragmentPhotoBinding
import com.example.photoweatherapp.ui.base.BaseFragment


class PhotoFragment : BaseFragment<FragmentPhotoBinding>(R.layout.fragment_photo) {

    val REQUEST_CODE = 200

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun setListeners() {
        binding?.apply {
            btnCapturePhoto.setOnClickListener {
                capturePhoto()
            }
        }
    }

    override fun setObservers() {
    }

    override fun initializeViewModel() {
    }


    fun capturePhoto() {

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE && data != null) {
            binding?.ivCaptured?.setImageBitmap(data.extras?.get("data") as Bitmap)
        }
    }
}
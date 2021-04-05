package com.example.photoweatherapp.ui.photo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.photoweatherapp.R
import com.example.photoweatherapp.databinding.FragmentPhotoBinding
import com.example.photoweatherapp.ui.base.BaseFragment


class PhotoFragment : BaseFragment<FragmentPhotoBinding>(R.layout.fragment_photo) {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun setListeners() {

    }

    override fun setObservers() {
    }

    override fun initializeViewModel() {
    }


}
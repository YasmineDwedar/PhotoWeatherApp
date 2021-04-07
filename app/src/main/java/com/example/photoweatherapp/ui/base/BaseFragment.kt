package com.example.photoweatherapp.ui.base

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.photoweatherapp.R
import com.example.photoweatherapp.util.LocationProvider
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

/**
 * Created by Yasmine on April,2021
 */
abstract class BaseFragment<VDB : ViewDataBinding> constructor(val layoutID: Int) : Fragment(),
    EasyPermissions.PermissionCallbacks {

    var binding: VDB? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutID, container, false);
        val view = binding?.getRoot()
        initializeViewModel()
        setObservers()
        setListeners()
        return view

    }

    fun handleError(msg: String) {
        (activity as BaseActivity<*>).handleErrors(msg)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show();
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
        }
    }

        @AfterPermissionGranted(CAMERA_PERMISSION)
    open fun openCameraAfterPermissionGranted() {
        val perms = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (EasyPermissions.hasPermissions(requireActivity(), *perms)) {
            openTheCamera()
        } else {
            val rationale = getString(R.string.camera_permission_rationale)
            EasyPermissions.requestPermissions(this, rationale, CAMERA_PERMISSION, *perms)
        }
    }

    open fun openTheCamera() {

    }


    open fun getCurrentLocation() {

    }

    @AfterPermissionGranted(LOCATION_PERMISSIONS)
    open fun getUserCurrentLocation() {

        val locationPermissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (EasyPermissions.hasPermissions(requireContext(), *locationPermissions)) {
            getCurrentLocation()
        } else {
            val rationale = getString(R.string.location_permission_rationale)
            EasyPermissions.requestPermissions(
                this, rationale, LOCATION_PERMISSIONS, *locationPermissions
            )
        }
    }


    abstract fun setListeners()
    abstract fun setObservers()
    abstract fun initializeViewModel()


    companion object {
        private const val LOCATION_PERMISSIONS = 101
        private const val CAMERA_PERMISSION = 102
        private const val EXTERNAL_STORAGE_PERMISSIONS = 103
    }
}
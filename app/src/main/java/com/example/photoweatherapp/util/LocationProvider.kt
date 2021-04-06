package com.example.photoweatherapp.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.widget.Toast
import com.google.android.gms.location.*

/**
 * Created by Yasmine on April,2021
 */

class LocationProvider(private val context: Context,val coordinatesCallBack: GetLocationCoordinatesCallBack) {


    private var mFusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {

            locationResult?.lastLocation?.let {
                mFusedLocationClient.removeLocationUpdates(this)
                coordinatesCallBack.sendCoordinates(it.longitude,it.latitude)


            }
        }
    }


    @SuppressLint("MissingPermission")
    public fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval =LONGEST_INTERVAL
        mLocationRequest.fastestInterval = LONGEST_INTERVAL
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )

    }

    companion object {
        const val REQUEST_CHECK_SETTINGS: Int = 0
        private const val LONGEST_INTERVAL: Long = 50000
    }

    interface GetLocationCoordinatesCallBack {
        fun sendCoordinates(long: Double,  lat: Double)
    }


}
package com.example.photoweatherapp.ui.capture

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photoweatherapp.data.local.WeatherInfo
import com.example.photoweatherapp.data.repository.WeatherRepository
import com.example.photoweatherapp.models.WeatherResponse
import com.example.photoweatherapp.ui.base.BaseViewModel
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

/**
 * Created by Yasmine on April,2021
 */
class CaptureFragmentViewModel: BaseViewModel() {
    var weatherRepository=WeatherRepository()
     private var _locationLiveData = MutableLiveData<WeatherResponse>()
         val locationLiveData: LiveData<WeatherResponse>
             get() = _locationLiveData

fun getWeatherData(lat:Double,long:Double){
    viewModelScope.launch (Dispatchers.IO){
        var weatherResponse=weatherRepository.getWeatherData(lat,long)
        when(weatherResponse){
            is NetworkResponse.Success ->{
                _locationLiveData.postValue(weatherResponse.body)

            }else ->{
            weatherResponse.handleErrors()

            }
        }

    }
}


     @InternalCoroutinesApi
     fun insertWeatherInfo(weatherInfo: WeatherInfo) {
         viewModelScope.launch (Dispatchers.IO){
            weatherRepository.insertWeatherInfo(weatherInfo)


         }
     }











}
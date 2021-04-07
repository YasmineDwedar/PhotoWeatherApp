package com.example.photoweatherapp.ui.history

import androidx.lifecycle.*
import com.example.photoweatherapp.data.local.WeatherInfo
import com.example.photoweatherapp.data.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by Yasmine on April,2021
 */
@InternalCoroutinesApi
class HistoryFragmentViewModel : ViewModel() {


    var weatherRepository = WeatherRepository()
    val weatherListLiveData = liveData(Dispatchers.IO) {
        weatherRepository.getSavedInfo()?.collect {
            emit(it)
        }
    }



}
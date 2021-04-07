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

    //     private var _weatherListLiveData = MutableLiveData<List<WeatherInfo>>()
//         val weatherListLiveData: LiveData<List<WeatherInfo>>
//             get() = _weatherListLiveData
    var weatherRepository = WeatherRepository()
    val weatherListLiveData = liveData(Dispatchers.IO) {
        weatherRepository.getSavedInfo()?.collect {
            emit(it)
        }
    }


    init {
//    getSavedInfo()
//    viewModelScope.launch (Dispatchers.IO) {
//
//        val data = weatherRepository.getSavedInfo()?.collect {
//            _weatherListLiveData.postValue(it)
//        }
//    }
    }


//    @InternalCoroutinesApi
//    fun getSavedInfo(){
//        viewModelScope.launch (Dispatchers.IO){
//          _weatherListLiveData.postValue(weatherRepository.getSavedInfo())
//
//
//        }
//    }

}
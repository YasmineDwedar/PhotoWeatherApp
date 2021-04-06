package com.example.photoweatherapp.ui.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.photoweatherapp.models.ErrorResponse
import com.haroldadmin.cnradapter.NetworkResponse

/**
 * Created by Yasmine on April,2021
 */
open class BaseViewModel:ViewModel() {


        private var _networkState = MutableLiveData<String>()
        val networkState: LiveData<String>
            get() = _networkState

        fun NetworkResponse<*, ErrorResponse>.handleErrors() {
            when (this) {
                is NetworkResponse.ServerError -> _networkState.postValue(this.body?.message)
                is NetworkResponse.UnknownError -> _networkState.postValue(this.error.message)
                is NetworkResponse.NetworkError -> _networkState.postValue(this.error.message)
            }
        }
}
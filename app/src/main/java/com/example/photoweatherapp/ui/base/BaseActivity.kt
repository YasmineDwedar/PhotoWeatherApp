package com.example.photoweatherapp.ui.base

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

/**
 * Created by Yasmine on April,2021
 */


abstract class BaseActivity<VDB: ViewDataBinding>(val layoutID:Int) : AppCompatActivity() {

    var binding: VDB? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this,layoutID)
        initializeViewModel()
        setListeners()
        setObservers()
    }


    fun handleErrors(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
    override fun onDestroy() {
        super.onDestroy()
    }
    abstract fun setListeners()
    abstract fun setObservers()
    abstract fun initializeViewModel()
}
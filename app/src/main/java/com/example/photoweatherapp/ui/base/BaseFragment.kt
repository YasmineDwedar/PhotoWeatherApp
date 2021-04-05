package com.example.photoweatherapp.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

/**
 * Created by Yasmine on April,2021
 */
abstract class BaseFragment<VDB : ViewDataBinding> constructor(val layoutID: Int) : Fragment() {

    var binding: VDB? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutID, container, false);
        val view = binding?.getRoot()
        setListeners()
        return view

    }
    fun handleError(msg: String) {
        (activity as BaseActivity<*>).handleErrors(msg)
    }


    abstract fun setListeners()
    abstract fun setObservers()
    abstract fun initializeViewModel()
}
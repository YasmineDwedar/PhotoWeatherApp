package com.example.photoweatherapp.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.photoweatherapp.data.local.WeatherInfo
import com.example.photoweatherapp.databinding.WeatherItemBinding
import com.example.photoweatherapp.util.ImageLoader

/**
 * Created by Yasmine on April,2021
 */



class HistoryAdapter  : ListAdapter<WeatherInfo, HistoryAdapter.ViewHolder>(UsersDiffUtil()) {


    class UsersDiffUtil: DiffUtil.ItemCallback<WeatherInfo>() {
        override fun areItemsTheSame(oldItem: WeatherInfo, newItem: WeatherInfo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: WeatherInfo, newItem: WeatherInfo): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(WeatherItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val weatherInfo = getItem(position)

        holder.binding.apply {
            weather=weatherInfo
            ivCaptured.setImageBitmap(ImageLoader.StringToBitMap(weatherInfo.img))


        }
    }

    inner class ViewHolder(val binding: WeatherItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }



}
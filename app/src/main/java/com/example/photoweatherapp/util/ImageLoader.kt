package com.example.photoweatherapp.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

/**
 * Created by Yasmine on April,2021
 */
 object ImageLoader{
        fun StringToBitMap(encodedString: String?): Bitmap? {
            return try {
                val bitmap: Bitmap
                val encodeByte = Base64.decode(encodedString, Base64.DEFAULT)
                bitmap = BitmapFactory.decodeByteArray(
                    encodeByte, 0,
                    encodeByte.size
                )
                bitmap
            } catch (e: Exception) {
                e.message
                null
            }
        }

        fun BitMapToString(bitmap: Bitmap): String {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val b = baos.toByteArray()
            return Base64.encodeToString(b, Base64.DEFAULT)
        }
    }



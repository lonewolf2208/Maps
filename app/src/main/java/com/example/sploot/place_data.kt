package com.example.sploot

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class place_data(
    val name:String,
    val adress:String,
    val rating: Double,
    val photos: Bitmap
):Parcelable

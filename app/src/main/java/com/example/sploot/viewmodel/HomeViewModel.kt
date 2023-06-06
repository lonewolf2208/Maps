package com.example.sploot.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.libraries.places.api.model.Place

class HomeViewModel: ViewModel() {
    var latitude = MutableLiveData<Double>()
    var longitude  = MutableLiveData<Double>()
    var data_type= MutableLiveData<String>()
    var placefields = listOf(
        Place.Field.ID, Place.Field.NAME,
        Place.Field.RATING,
        Place.Field.PHOTO_METADATAS,
        Place.Field.ADDRESS)
}
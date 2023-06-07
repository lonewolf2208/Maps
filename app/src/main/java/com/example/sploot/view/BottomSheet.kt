package com.example.sploot.view

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.sploot.databinding.FragmentBottomSheetBinding
import com.example.sploot.model.place_data
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheet : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        var data  = arguments?.getParcelable<place_data>("data")
        binding.adress.text=data?.adress.toString()
        binding.placeName.text=data?.name.toString()
        binding.ratingBar.rating= data?.rating!!.toFloat()
        binding.imageView.setImageBitmap(data?.photos)
        // Inside the onMapClick method
// Create a Uri for the selected location
        // Inside the onMapClick method
// Create a Uri for the selected location
        binding.button.setOnClickListener {
            val gmmIntentUri: Uri =
                Uri.parse("google.navigation:q=" + data.lat + "," + data.long)

// Create an intent with the Uri

// Create an intent with the Uri
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)

// Set the package to "com.google.android.apps.maps" to ensure it opens in Google Maps

// Set the package to "com.google.android.apps.maps" to ensure it opens in Google Maps
            mapIntent.setPackage("com.google.android.apps.maps")


// Start the intent

// Start the intent
            startActivity(mapIntent)
        }
        return binding.root
    }


}
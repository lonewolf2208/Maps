package com.example.sploot

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.sploot.databinding.FragmentBottomSheetBinding
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
        return binding.root
    }


}
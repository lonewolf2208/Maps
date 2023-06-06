package com.example.sploot.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.sploot.R
import com.example.sploot.databinding.FragmentHomeBinding
import com.example.sploot.model.place_data
import com.example.sploot.viewmodel.HomeViewModel
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.PlaceTypes
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.*
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


class Home : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    val AUTOCOMPLETE_REQUEST_CODE = 1
    lateinit var homeViewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel=ViewModelProvider(this)[HomeViewModel::class.java]
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }
    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var binding = FragmentHomeBinding.inflate(inflater, container, false)
        Places.initialize(requireContext(), "AIzaSyA3zeQUA47kyCgI5XJFJMn6zybxb3jPqeQ")
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.
                homeViewModel.latitude.postValue(location!!.latitude)
                homeViewModel.longitude.postValue(location.longitude)

            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
            }
        homeViewModel.latitude.observe(viewLifecycleOwner
        ) {
            val mapFragment = childFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }
        binding.search.setOnClickListener {
            val fields = listOf(Place.Field.LAT_LNG, Place.Field.NAME)
            // Start the autocomplete intent.
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(requireContext())
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        }
        homeViewModel.data_type.observe(viewLifecycleOwner){
            val mapFragment = childFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }
    binding.chipGroup.setOnCheckedStateChangeListener(object : ChipGroup.OnCheckedStateChangeListener {
        override fun onCheckedChanged(group: ChipGroup, checkedIds: MutableList<Int>) {
            val chip: Chip = group.findViewById(checkedIds[0])
            homeViewModel.data_type.postValue(chip.text.toString())
        }
    })
        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(p0: GoogleMap) {
        mMap = p0

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(homeViewModel.latitude.value!!, homeViewModel.longitude.value!!)
        mMap.addMarker(
            MarkerOptions()
                .position(sydney)
                .title("Marker")
        )
        homeViewModel.data_type.observe(viewLifecycleOwner) {
            when(it)
            {
                "Restaurants"->{  p0.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        requireContext(), R.raw.restaurant));}
                "Parks"->{p0.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        requireContext(), R.raw.park));}
                "Museum"->{p0.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        requireContext(), R.raw.museum));}
                "Petrol"->{p0.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        requireContext(), R.raw.petrol));}
                else->{p0.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        requireContext(), R.raw.style))}

            }
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15F))
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.park))
        mMap.setOnPoiClickListener {
            val placeId = it.placeId.toString()
            val placesClient = Places.createClient(requireContext())
            val placeFields = homeViewModel.placefields
            val request = FetchPlaceRequest.newInstance(placeId, placeFields)
            placesClient.fetchPlace(request)
                .addOnSuccessListener { response: FetchPlaceResponse ->
                    val place = response.place
                    val metada = place.photoMetadatas
                    if (metada == null || metada.isEmpty()) {
                        return@addOnSuccessListener
                    }
                    val photoMetadata = metada.first()

                    // Get the attribution text.
                    val attributions = photoMetadata?.attributions

                    // Create a FetchPhotoRequest.
                    val photoRequest = FetchPhotoRequest.builder(photoMetadata)
                        .setMaxWidth(500) // Optional.
                        .setMaxHeight(300) // Optional.
                        .build()
                    placesClient.fetchPhoto(photoRequest)
                        .addOnSuccessListener { fetchPhotoResponse: FetchPhotoResponse ->
                            val bitmap = fetchPhotoResponse.bitmap
                            var data = place_data(place.name,place.address,
                                (place.rating ?: 0) as Double,bitmap)
                            var bundle =  Bundle()
                            bundle.putParcelable("data",data)
                            findNavController().navigate(R.id.bottomSheet,bundle)
                        }.addOnFailureListener { exception: Exception ->
                            if (exception is ApiException) {
                                Log.e("", "Place not found: " + exception.message)
                                val statusCode = exception.statusCode
                            }

                        }


                }.addOnFailureListener { exception: Exception ->
                    if (exception is ApiException) {
                        Toast.makeText(
                            requireContext(),
                            "Place not found: ${exception}",
                            Toast.LENGTH_SHORT
                        ).show()
                        val statusCode = exception.statusCode
                        TODO("Handle error with given status code")
                    }
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
//                    Toast.makeText(requireContext(),"Ok",Toast.LENGTH_LONG).show()
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        homeViewModel.latitude.postValue(place.latLng.latitude)
                        homeViewModel.longitude.postValue(place.latLng.longitude)
                        Log.i("adsasd", "Place: ${place.latLng.latitude}, ${place.latLng.longitude}")
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {

                    // TODO: Handle the error.
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
//                        Toast.makeText(requireContext(),status.statusMessage.toString(),Toast.LENGTH_LONG).show()
//                        Log.i(TAG, status.statusMessage)
                    }
                }
                Activity.RESULT_CANCELED -> {
//                    Toast.makeText(requireContext(),"Cancelled",Toast.LENGTH_LONG).show()
                    // The user canceled the operation.
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
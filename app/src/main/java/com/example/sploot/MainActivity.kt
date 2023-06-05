package com.example.sploot

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private var FINE_LOCATION=android.Manifest.permission.ACCESS_FINE_LOCATION
    private var COARSE_LOCATION=android.Manifest.permission.ACCESS_COARSE_LOCATION
    private var LOCATION_PERMISSION_REQUEST_CODE=1234
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getLocationPermission()
        setContentView(R.layout.activity_main)
    }
    private fun getLocationPermission()
    {
        var permission= arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION)
        if(ContextCompat.checkSelfPermission(this,FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {
            if(ContextCompat.checkSelfPermission(this,COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED)
            {

            }
            else
            {
                ActivityCompat.requestPermissions(this,permission,LOCATION_PERMISSION_REQUEST_CODE)
            }
        }
        else
        {
            ActivityCompat.requestPermissions(this,permission,LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

}
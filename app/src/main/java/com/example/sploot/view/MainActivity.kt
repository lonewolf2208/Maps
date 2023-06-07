package com.example.sploot.view

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.sploot.CheckNetworkConnection
import com.example.sploot.R

class MainActivity : AppCompatActivity() {
    private var FINE_LOCATION=android.Manifest.permission.ACCESS_FINE_LOCATION
    private var COARSE_LOCATION=android.Manifest.permission.ACCESS_COARSE_LOCATION
    private var LOCATION_PERMISSION_REQUEST_CODE=1234
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            getLocationPermission()
//            if(ContextCompat.checkSelfPermission(this,FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
//            {
//                getLocationPermission()
//            }
        callNetworkConnection()

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
//                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                ActivityCompat.requestPermissions(this,permission,LOCATION_PERMISSION_REQUEST_CODE)
            }
        }
        else
        {
            ActivityCompat.requestPermissions(this,permission,LOCATION_PERMISSION_REQUEST_CODE)
        }

    }
    private fun callNetworkConnection() {
        var  checkNetworkConnection = CheckNetworkConnection(application)
        checkNetworkConnection.observe(this) { isConnected ->
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
            val navController: NavController = navHostFragment.navController
            if (!isConnected) {

                navController.navigate(R.id.offlineScreen)
            }
            else
            {
                navController.navigate(R.id.fragment_Map)
            }
        }

    }

}
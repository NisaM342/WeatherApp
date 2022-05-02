package com.example.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.Constants.ELAPSED_TIME
import com.example.weatherapp.Constants.NOTIFICATION_CHANNEL_ID
import com.example.weatherapp.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.weatherapp.Constants.manifestLocationPermission
import com.example.weatherapp.ErrorDialogFragment.Companion.TAG
import com.example.weatherapp.databinding.FragmentSearchBinding
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: SearchViewModel
    private val REQUEST_CODE_COARSE_LOCATION: Int = 1234
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var latitude: Float? = null
    private var longitude: Float? = null
    private var notificationIsActive: Boolean = false
    private lateinit var serviceIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.MINUTES.toMillis(30)
            fastestInterval = TimeUnit.SECONDS.toMillis(30)
            maxWaitTime = TimeUnit.MINUTES.toMillis(1)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.locations.forEach {
                    Log.d(
                        TAG,
                        "LT123/Callback Latitude: " + it.latitude.toString() + ", Longitude: " + it.longitude.toString()
                    )
                }
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = "Search"
        binding = FragmentSearchBinding.bind(view)
        viewModel = SearchViewModel()
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        createNotificationChannel()
        serviceIntent =
            Intent(requireActivity().applicationContext, NotificationService::class.java)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                val lastLocation = p0.lastLocation
                latitude = lastLocation.latitude.toFloat()
                longitude = lastLocation.longitude.toFloat()
                notificationCondition()
                super.onLocationResult(p0)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.enableButton.observe(this) { enable ->
            binding.searchButton.isEnabled = enable
        }
        binding.searchButton.setOnClickListener {
            val action =
                SearchFragmentDirections.actionSearchFragmentToCurrentConditionsFragment(viewModel.getZipCode())
            findNavController().navigate(action)
        }
        binding.notificationButton.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    manifestLocationPermission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermission()
            } else {
                requestLocation()
            }
        }

        binding.zipCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                p0?.toString()?.let { viewModel.updateZipCode(it) }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.locationButton.setOnClickListener {
            if (checkForPermission()) {
                if (latitude != null && longitude != null) {
                    val action =
                        SearchFragmentDirections.actionSearchFragmentToCurrentConditionsFragment(
                            "",
                            latitude!!,
                            longitude!!
                        )
                    findNavController().navigate(action)
                }
                Toast.makeText(requireContext(), "Permission Already Granted", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocation() {
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            Log.d(
                TAG,
                "YM1997-1.0 Latitude: " + location.latitude.toString() + ", Longitude: " + location.longitude.toString()
            )
            latitude = location.latitude.toFloat()
            longitude = location.longitude.toFloat()
            Log.d(TAG, "YM1997-2.0 Latitude: $latitude, Longitude: $longitude")
            notificationCondition()
            if (latitude != null && longitude != null) {
                val action =
                    SearchFragmentDirections.actionSearchFragmentToCurrentConditionsFragment(
                        "",
                        latitude!!,
                        longitude!!
                    )
                findNavController().navigate(action)
            }
        }.addOnFailureListener {
            Log.d(TAG, "Failed getting current location")
        }
    }

    private fun notificationCondition() {
        if (!notificationIsActive) {
            notificationIsActive = true
            serviceIntent.putExtra(ELAPSED_TIME, 0)
            requireActivity().startService(serviceIntent)
            binding.notificationButton.text = getString(R.string.turn_notification_off)
        } else {
            notificationIsActive = false
            requireActivity().stopService(serviceIntent)
            with(NotificationManagerCompat.from(requireContext())) {
                cancel(1)
            }
            binding.notificationButton.text = getString(R.string.turn_notification_on)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                importance
            ).apply {
                description = "Weather Channel"
            }
            val notificationManager: NotificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    private fun checkForPermission(): Boolean {
        val selfPermission: Boolean = ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        if (selfPermission) {
            getLocation()
        } else {
            requestPermission()
        }
        return selfPermission
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
            AlertDialog.Builder(requireContext())
                .setMessage("We need permission to give accurate weather data")
                .setCancelable(false)
                .setPositiveButton("Ok") { dialog: DialogInterface, which: Int ->
                    ActivityCompat.requestPermissions(requireActivity(),arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_CODE_COARSE_LOCATION)
                }
                .setNegativeButton("Cancel") { dialog: DialogInterface, which: Int ->
                    dialog.dismiss()
                }
                .create().show();
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_CODE_COARSE_LOCATION)
        }
    }
}

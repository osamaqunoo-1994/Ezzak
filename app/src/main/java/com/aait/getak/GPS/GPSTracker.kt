package com.aait.getak.GPS

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log

class GPSTracker : Service, LocationListener {

    private val mContext: Context

    // flag for GPS Status
    internal var isGPSEnabled = false

    // flag for network status
    internal var isNetworkEnabled = false

    internal var canGetLocation = false


    internal var location: Location? = null
    internal var latitude: Double = 0.toDouble()
    internal var longitude: Double = 0.toDouble()


    internal lateinit var gpsTrakerListner: GPSTrakerListner

    // Declaring a Location Manager
    protected var locationManager: LocationManager? = null

    //,,
    constructor(context: Context, gpsTrakerListner: GPSTrakerListner) {
        this.mContext = context
        this.gpsTrakerListner = gpsTrakerListner
    }

    constructor(context: Context) {
        this.mContext = context
    }

    @SuppressLint("MissingPermission")
    fun getLocation(): Location? {
        try {

            locationManager = mContext
                .getSystemService(Context.LOCATION_SERVICE) as LocationManager

            locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            // getting GPS status
            isGPSEnabled = locationManager!!
                .isProviderEnabled(LocationManager.GPS_PROVIDER)

            // getting network status
            isNetworkEnabled = locationManager!!
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER)


            if (!isGPSEnabled && !isNetworkEnabled) {
                // location service disabled
                Log.e("GPS", "No GPS And Network Enabled")
            } else {
                this.canGetLocation = true

                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    locationManager!!.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this
                    )

                    if (locationManager != null) {
                        Log.e("GPS", "GPS Enabled")
                        location = locationManager!!
                            .getLastKnownLocation(LocationManager.GPS_PROVIDER)
                        updateGPSCoordinates(location)
                    }
                }
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    if (location == null) {
                        locationManager!!.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this
                        )
                        if (locationManager != null) {
                            Log.e("GPS", "Network")
                            location = locationManager!!
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                            updateGPSCoordinates(location)
                        }
                    }

                }

                // check if the dialog of start deticting location is open
                if (getLatitude() == 0.0 && getLongitude() == 0.0) {
                    Log.e("latLng", "lat:" + getLatitude() + " lng:" + getLongitude())
                    gpsTrakerListner.onStartTraker()

                }
            }
        } catch (e: Exception) {
            // e.printStackTrace();
            Log.e(
                "GPS",
                "Impossible to connect to LocationManager", e
            )
        }

        return location
    }

    fun updateGPSCoordinates(location: Location?) {
        // if the latitude and longitude dosn't equals null or dosn't equals 0.0 will send the listner
        if (location != null && location.latitude != 0.0 && location.longitude != 0.0) {
            gpsTrakerListner.onStopTracker(location.latitude, location.longitude)
            latitude = location.latitude
            longitude = location.longitude
            gpsTrakerListner.onTrakerSuccess(latitude, longitude)
        } else {
            Log.e("GPS", "Location null")
        }
    }

    fun canGetLocation(): Boolean {
        return this.canGetLocation
    }

    override fun onLocationChanged(location: Location) {
        Log.e("GPS", "Location :$location")
        updateGPSCoordinates(location)
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        Log.e("GPS", "StatusChanged$provider")

    }


    override fun onProviderDisabled(provider: String) {

    }

    override fun onProviderEnabled(provider: String) {}

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    /**
     * Function to get latitude
     */
    fun getLatitude(): Double {
        if (location != null) {
            latitude = location!!.latitude
        }

        // return latitude
        return latitude
    }

    /**
     * Function to get longitude
     */
    fun getLongitude(): Double {
        if (location != null) {
            longitude = location!!.longitude
        }
        // return longitude
        return longitude
    }

    companion object {
        // The minimum distance to change Updates in meters
        private val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 100 // 100 meters

        // The minimum time between updates in milliseconds
        private val MIN_TIME_BW_UPDATES: Long = 1000 // 1 minute
    }


}

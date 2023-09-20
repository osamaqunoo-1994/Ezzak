package com.aait.getak.ui.activities.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.appcompat.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.TextView
import com.aait.getak.R
import com.aait.getak.base.ParentActivity
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import kotlinx.android.synthetic.main.activity_map.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.io.IOException
import java.util.*

class MapActivity : ParentActivity(), OnMapReadyCallback,
    GoogleMap.OnMapClickListener, GoogleMap.OnCameraChangeListener {
    private lateinit var mMap: GoogleMap
    private val REQUEST_CODE_CH2: Int=2
    companion object {
        val RESULT_CODE = 10
    }

      var loc = MutableLiveData<String>()
    override fun onCameraChange(cameraPosition: CameraPosition?) {
        lat = cameraPosition?.target?.latitude.toString()
        lng = cameraPosition?.target?.longitude.toString()
       // addMarker(cameraPosition?.target?.latitude!!.toDouble(), cameraPosition.target?.longitude!!.toDouble())

        getAddressFromLocation(cameraPosition?.target?.latitude!!.toDouble(),cameraPosition.target?.longitude!!.toDouble(),this,GeocoderHandler())
        //sendRequestLoc(lat,lng)
    }



    override fun onMapClick(latLng: LatLng?) {

    }

    override val layoutResource: Int
        get() = R.layout.activity_map

    override fun init() {
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        //mapView.getMapAsync(this)
        var mapFragment = supportFragmentManager
                .findFragmentById(R.id.mapView) as (SupportMapFragment)
        mapFragment.getMapAsync(this)

        try {
            MapsInitializer.initialize(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }


     var tv_address2: TextView? = null

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setStyle()
        setAutoComplete()
        prepareMap()
         tv_address2 = tv_address
         mMap.setOnCameraChangeListener(this)
        setActions()

    }
    private fun setStyle() {
        try {
            val success = mMap!!.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.map_style))

            if (!success) {
                Log.e("success", "Style parsing failed.");
            }
        } catch (e: Exception ) {
            Log.e("error", "Can't find style. Error: ", e);
        }
    }

    private fun setActions() {
        btn_approve_location.onClick {
            onAgree()
        }
    }

    private lateinit var autocompleteFragment: AutocompleteSupportFragment


/*
    private fun setAutoComplete() {
        autocompleteFragment =
            fragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as AutocompleteSupportFragment


        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                //mMap.setOnCameraChangeListener(null)
                addMarker(place.latLng?.latitude!!, place.latLng!!.longitude)
                lat = place.latLng!!.latitude.toString() + ""
                lng = place.latLng!!.longitude.toString() + ""
                address = place.address!!.toString() + ""
                tv_address.text=address

            }

            override fun onError(status: Status) {
                Log.e("error", status.statusMessage)
            }
        })
    }
*/



    private fun prepareMap() {

        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                    REQUEST_CODE_CH2
                )
            } else {
                showLocation()
            }


        } else {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.alert))
            builder.setMessage(R.string.gps_msg)
            builder.setPositiveButton(
                R.string.ok
            ) { dialogInterface, i -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
            builder.setNegativeButton(
                R.string.cancel
            ) { dialogInterface, i -> dialogInterface.dismiss() }
            val alertDialog = builder.create()
            alertDialog.show()
        }
    }
    private var fusedLocationClient: FusedLocationProviderClient? = null

    @SuppressLint("MissingPermission")
    private fun showLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setPermissionsLocation {
            if (it){
                fusedLocationClient!!.lastLocation.addOnSuccessListener {
                    if (it!=null) {
                        Log.e("location", it.latitude.toString())
                        addMarker(it.latitude, it.longitude)
                        this.lat=it.latitude.toString()
                        this.lng=it.longitude.toString()
                        Log.e("dist",it.latitude.toString())
                        pin.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
    private fun setAutoComplete() {
        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).

        if (!Places.isInitialized()) {
            Places.
            initialize(applicationContext,getString(R.string.google_maps_key))
        }
        val autocompleteFragment = supportFragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as AutocompleteSupportFragment?


        // Specify the types of place data to return.
        autocompleteFragment?.setPlaceFields(listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG))


        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment?.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                lat= place.latLng?.latitude?.toString()
                lng= place.latLng?.longitude?.toString()
                address=place.address
                tv_address.text=address
                mMap.moveCamera(CameraUpdateFactory.
                newLatLngZoom(LatLng(place.latLng?.latitude!!, place.latLng?.longitude!!), 17f))

            }

            override fun onError(status: Status) {
                Log.e("places", "An error occurred: $status")
            }
        })


    }

    private var address: String? = null
    private var lat:String? = null
    private var lng:String? = null

    private fun addMarker(lat: Double, lng: Double) {
        mMap.clear()
        this.lat = lat.toString() + ""
        this.lng = lng.toString() + ""
        val markerOptions = MarkerOptions()
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.location_pointer_place))
        val latLng = LatLng(lat, lng)
        markerOptions.position(latLng)
        markerOptions.draggable(false)
       // mMap.addMarker(markerOptions)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f))
    }


    fun onAgree() {
        var intent=intent
        intent.putExtra("lat", lat)
        intent.putExtra("lng", lng)
        intent.putExtra("address",tv_address.text.toString())
        setResult(Activity.RESULT_OK, intent)
        finish()

    }
/*

    private fun sendRequestLoc(lat: String?, lng: String?) {
        showProgressDialog(getString(R.string.please_wait))

        RetroWeb.getLocationClint()
            .getLocation("$lat,$lng", Util.getLanguage(), "AIzaSyAitYKekoEDj37Vc-nuaXYZd-ZxC92g2Tw")
            .enqueue(object : Callback<AddressModel> {
                override fun onResponse(call: Call<AddressModel>, response: Response<AddressModel>) {
                    hideProgressDialog()
                    if (response.isSuccessful) {
                        if (response.body()!!.results!!.isNotEmpty()) {
                            address = response.body()!!.results?.get(0)?.formatted_address
                            tv_address.text=address
                            loc.postValue(address)

                        }
                    }
                }

                override fun onFailure(call: Call<AddressModel>, t: Throwable) {
                    hideProgressDialog()
                    Toast.makeText(this@MapActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
    }
*/

    override fun onBackPressed() {
        finish()


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty()) {
            showLocation()
        }
    }

    fun getAddressFromLocation(
      lat:Double,
      lng:Double,
     context: Context, handler: Handler?=null) {
        val thread = Thread()
        thread.run {

            val locale = Locale(mPrefs?.lang)
            var geocoder = Geocoder(context, locale)
            var result: String? = null
            try {
                val list = geocoder.getFromLocation(
                    lat, lng, 1
                )
                if (list != null && list.size > 0) {
                    val address = list[0]
                    result = address.getAddressLine(0) + ", " + address.locality
                }
                else{

                }
            } catch (e: IOException) {
                Log.e("hhhh", "Impossible to connect to Geocoder", e)
            } finally {
                var msg = Message.obtain()
                msg.target = handler
                if (result != null) {
                    msg.what = 1
                    var bundle = Bundle()
                    bundle.putString("address", result)
                    bundle.putDouble("lat",lat)
                    bundle.putDouble("lat",lng)
                    msg.data = bundle

                } else
                    msg.what = 0
                msg.sendToTarget()
            }
        }
        thread.start()
    }


      inner class GeocoderHandler : Handler() {

         @Override
         override fun handleMessage(message: Message) {

            var result=""
            when  {
                message?.what == 1 -> {
                    var bundle = message.data
                    result = bundle.getString("address").toString()

                }

            }
             loc.postValue(result)
             tv_address.text=result


         }

    }


}



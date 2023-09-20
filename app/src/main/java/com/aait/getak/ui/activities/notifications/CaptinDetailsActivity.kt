package com.aait.getak.ui.activities.notifications

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import com.aait.getak.R
import com.aait.getak.base.ParentActivity
import com.aait.getak.models.captin_model.Data
import com.aait.getak.utils.Util
import com.aait.getak.utils.toasty
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_captin_details.*
import kotlinx.android.synthetic.main.captin_card.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.json.JSONObject
import com.aait.getak.network.remote_db.SocketConnection
import com.aait.getak.utils.MapUtils
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.LatLng


class CaptinDetailsActivity : ParentActivity(), OnMapReadyCallback {
    private var marker: Marker? = null
    private var mOldlat: Double = 0.0
    private var mOldlng: Double = 0.0
    private var startPosition: LatLng? = null
    private lateinit var carMarker: MarkerOptions
    private var zoom=18f
    private var captainId: Int=0
    private var end_long: Double?=0.0
    private var end_lat: Double?=0.0
    private var end_address: String?=""
    private var start_long: Double?=0.0
    private var start_lat: Double?=0.0
    private var start_address: String?=""
    private var isCaptinHidden=true
        private lateinit var trip: Data
    private var mMap: GoogleMap?=null
    override val layoutResource: Int
        get() = R.layout.activity_captin_details



    override fun init() {
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        setToolbar(getString(R.string.my_trips))

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapView) as (SupportMapFragment)
        mapFragment.getMapAsync(this)
        captin_btn.visibility=View.VISIBLE
        setActions()
        sendRequest()

        try {
            MapsInitializer.initialize(this)
        }
        catch (e: Exception) {
            e.printStackTrace()
        }



        captin_btn.onClick {
            isCaptinHidden = !isCaptinHidden
            if (isCaptinHidden) {
                captin_lay.visibility = View.VISIBLE
                captin_btn.text=getString(R.string.hide_captin_data)
            } else {
                captin_lay.visibility = View.GONE
                captin_btn.text=getString(R.string.show_profile)
            }
        }


    }

    private fun addTrack() {
        val jsonObject = JSONObject()
        jsonObject.put("tracker_id",Gson().toJson(mPrefs!!.user?.id))
        jsonObject.put("captain_id",Gson().toJson(captainId))
        Log.e("emmit",Gson().toJson(captainId)+","+Gson().toJson(mPrefs!!.user?.id))
        SocketConnection.addEvent("addtracker",jsonObject)
    }

    private fun sendRequest(){
    addDisposable(repo.captin_details(intent.getIntExtra("order_id",0),mPrefs!!.token!!)
        .subscribeWithLoading({
            showProgressDialog()
        }
            ,{
            if (it.value=="1"){
                trip = it.data
                captainId = it.data.captain_id
                mOldlat=it.data.captain_lat!!
                mOldlng = it.data.captain_lng!!
                //create marker
                carMarker = MarkerOptions()
                startPosition = LatLng(mOldlat, mOldlng)
                carMarker.position(startPosition!!)
                carMarker.draggable(false)
                carMarker.flat(true)
                marker=mMap?.addMarker(carMarker)
                marker?.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.car2))
                marker?.setAnchor(0.5f,0.5f)
                mMap?.moveCamera(
                    CameraUpdateFactory.newCameraPosition(
                        CameraPosition.Builder()
                            .target(startPosition!!)
                            .zoom(zoom)
                            .build()
                    )
                )
                addTrack()
                setData()
               // drawSrcDist()

            }
        },{
            hideProgressDialog()
            toasty(getString(R.string.error_connection),2)
            Log.e("error123",it.message!!+","+intent.getIntExtra("order_id",0))
        },{
            hideProgressDialog()
        })
    )
}
    private fun setActions() {
        //call_lay.visibility= View.VISIBLE
        iv_call.onClick {
            setPermissionsPhone {
                Util.onCall(this@CaptinDetailsActivity,trip.phone)
            }
        }
    }

    private fun getBearing(begin: LatLng, end: LatLng): Float {
        val lat = Math.abs(begin.latitude - end.latitude)
        val lng = Math.abs(begin.longitude - end.longitude)

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return Math.toDegrees(Math.atan(lng / lat)).toFloat()
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (90 - Math.toDegrees(Math.atan(lng / lat)) + 90).toFloat()
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (Math.toDegrees(Math.atan(lng / lat)) + 180).toFloat()
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (90 - Math.toDegrees(Math.atan(lng / lat)) + 270).toFloat()
        return -1f
    }

    private interface LatLngInterpolatorNew {
        fun interpolate(fraction: Float, a: LatLng, b: LatLng): LatLng

        class LinearFixed : LatLngInterpolatorNew {
            override fun interpolate(fraction: Float, a: LatLng, b: LatLng): LatLng {
                val lat = (b.latitude - a.latitude) * fraction + a.latitude
                var lngDelta = b.longitude - a.longitude
                // Take the shortest path across the 180th meridian.
                if (Math.abs(lngDelta) > 180) {
                    lngDelta -= Math.signum(lngDelta) * 360
                }
                val lng = lngDelta * fraction + a.longitude
                return LatLng(lat, lng)
            }
        }
    }

    fun animateMarkerNew(googleMap: GoogleMap,startPosition: LatLng, destination: LatLng, marker: Marker?) {
        if (marker != null) {
            val endPosition = LatLng(destination.latitude, destination.longitude)
            val latLngInterpolator = LatLngInterpolatorNew.LinearFixed()
            val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
            valueAnimator.duration = 2000 // duration 3 second
            valueAnimator.interpolator = LinearInterpolator()
            valueAnimator.addUpdateListener { animation ->
                try {
                    val v = animation.animatedFraction
                    val newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition)

                    googleMap.moveCamera(
                        CameraUpdateFactory.newCameraPosition(
                            CameraPosition.Builder()
                                .target(newPosition)
                                .zoom(18f)
                                .build()
                        )
                    )
                    marker.rotation =
                        getBearing(startPosition, LatLng(destination.latitude, destination.longitude))

                } catch (ex: Exception) {
                    //I don't care atm..
                }
            }

            valueAnimator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                }
            })
            valueAnimator.start()
        }
    }
    override fun onMapReady(gMap: GoogleMap?) {
        mMap=gMap

        mMap?.setOnCameraMoveListener {
            zoom=mMap?.cameraPosition!!.zoom
        }
        Log.e("captain1_",captainId.toString()+",")
        SocketConnection.onListen("trackorder") {
                isConnected, data ->
                        val data = data[0] as String
                            val car = JSONObject(data)
                            captainId = car.getInt("captain_id")
                            Log.e("captain1_",captainId.toString())
                        if (car.getInt("captain_id")==captainId){
                            Log.e("captain",captainId.toString())
                            val lat = car.getDouble("lat")
                            val lng = car.getDouble("lng")
                            startPosition = LatLng(lat, lng)
                            runOnUiThread {
                                Runnable {

                                }.run{
                                    MapUtils.rotateMarker(marker!!,MapUtils.bearingBetweenLocations( LatLng(mOldlat,mOldlng), LatLng(lat, lng)))
                                    MapUtils.animateMarker(mMap!!, marker!!,LatLng(lat, lng), false,zoom)
                                    mOldlat = lat
                                    mOldlng = lng

                                    }
                                }
                            }


        }



    }

   /* override fun onStop() {
        SocketConnection.onClose()
        super.onStop()
    }*/
    override fun onDestroy() {
      //  SocketConnection.onClose()
        super.onDestroy()

    }

    private fun setData() {
        Picasso.get().load(trip.avatar).into(iv_captin)
        name.text=trip.name
        from_loc.text=trip.startAddress
        to_loc.text=trip.endAddress
        period.text=trip.expectedPeriod
        rate.rating=trip.rate!!.toFloat()
        car_model.text=trip.carBrand
        car_plate.text=trip.carNumber
        start_lat=trip.startLat
        start_long=trip.startLong
        start_address=trip.startAddress
        end_lat=trip.endLat
        end_long=trip.endLong
        end_address=trip.endAddress
    }



    private fun drawSrcDist() {
        addDisposable(repo.getRountes(
            "$start_lat,$start_long",
            "$end_lat,$end_long"
            ,getString(R.string.google_maps_key)
        ).subscribeWithLoading({

        },{
            val routes = it.routes
            val latLngList = MapUtils.getDirectionPolylines(routes)
            drawRouteOnMap(mMap!!, latLngList)

        },{
            Log.e("error",it.message!!)
        },{

        }))
    }

    private fun drawRouteOnMap(map: GoogleMap, positions: List<LatLng>) {
        val options = PolylineOptions().width(5f).color(ContextCompat.getColor(applicationContext,R.color.colorAccent)).geodesic(true)
        options.addAll(positions)
        map.addPolyline(options)
        //marker of my location
        val markerOptions = MarkerOptions()
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_green_circle))
        val latLng = LatLng(start_lat!!, start_long!!)
        markerOptions.position(latLng)
        mMap!!.addMarker(markerOptions)
        //marker of target Location
        val markerOptions2 = MarkerOptions()
        markerOptions2.icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_two))
        val latLng2 = LatLng(end_lat!!, end_long!!)
        markerOptions2.position(latLng2)
        mMap!!.addMarker(markerOptions2)
        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(positions[0].latitude, positions[0].longitude))
            .zoom(16f)
            .build()
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }







}

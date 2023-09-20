/*
package com.aait.getak.ui.fragments.testFragments

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.aait.getak.R
import com.aait.getak.base.BaseFragment
import com.aait.getak.fcm.MessageService
import com.aait.getak.listeners.OnBack
import com.aait.getak.models.captin_model.Data
import com.aait.getak.models.client_later_model.Reason
import com.aait.getak.network.remote_db.SocketConnection
import com.aait.getak.ui.dialogs.RateDialog
import com.aait.getak.utils.*
import com.aait.getak.utils.MapUtils.getDirectionPolylines
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.from_to_only.*
import kotlinx.android.synthetic.main.tracking_map.*
import kotlinx.android.synthetic.main.trip_layout.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.json.JSONObject


class TrackingFragment : BaseFragment() , OnMapReadyCallback,
    GoogleMap.OnMapClickListener,OnBack {


    private var isAcceptedRide: Boolean = false
    private var captain_id: Int=0
    private var firstTime: Boolean = true
    private  var carMarker: MarkerOptions?=null
    private  var trip: Data? = null
    private var captainId: Int=0
    private  var startPosition: LatLng? = null
    private var marker: Marker? = null

    private var myLat=0.0
    private var myLng=0.0

    private var mOldlat: Double=0.0
    private var mOldlng: Double=0.0
    private var reasons: List<Reason> = mutableListOf()
    companion object{
        private var orderId: Int=0
         var isTrackingVisible: Boolean=false
        private var mData: com.aait.getak.models.order_details_model.Data?=null
    }

    private var cap_phone: String?=""
    private var isEnableToBack: Boolean=false
    private var zoom=18f
    var address= MutableLiveData<String>()
    private val REQUEST_CODE_CH2: Int = 100
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var addressResultReceiver: LocationAddressResultReceiver

    private var mMap: GoogleMap?=null
    private var startLong:Double?=0.0
    private var startLat:Double?=0.0
    private var endLat:Double?=0.0
    private var endLng:Double?=0.0



    override fun onResume() {
        super.onResume()
        startLocationUpdates()

    }


    private fun setLocationChange() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                myLat = locationResult.locations[0].latitude
                myLng = locationResult.locations[0].longitude
                if (firstTime) {
                    mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(myLat, myLng), zoom))
                    firstTime=false
                }
            }

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
            val bundle = this.arguments;
            orderId = bundle?.getInt("order_id") ?: 0
            isAcceptedRide = bundle?.getBoolean("isAcceptedRide") ?: false

        return super.onCreateView(inflater, container, savedInstanceState)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.mapView) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        try {
            MapsInitializer.initialize(activity!!.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
    private fun startLocationUpdates() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
        fusedLocationClient.requestLocationUpdates(
            LocationRequest(),locationCallback,
            null)
    }

    override val viewId: Int
        get() = R.layout.tracking_map

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setLocationChange()

        if (orderId==0 || !isAcceptedRide) {
            riple.startRippleAnimation()
            riple.visibility = View.VISIBLE
            content.visibility = View.VISIBLE
            bottom_sheet.visibility=View.GONE
        }
        else{
            bottom_sheet.visibility=View.VISIBLE
            sendRequest(orderId.toString())
        }

        setBehaviour()
        setActions()
    }

    private fun setActions() {

        from_title_loc.onClick {
            if (startLat!=0.0 && startLong!=0.0){
            activity!!.forwardMap(startLat.toString(),startLong.toString())
            }
        }
        to_short_loc.onClick {
            if (endLat!=0.0 && endLng!=0.0){
                activity!!.forwardMap(endLat.toString(),endLng.toString())
            }
        }
        cancel_trip.onClick {

            repo.cancelOrder(orderId.toString(),token = mPrefs.token!!).subscribeWithLoading({
                showProgressDialog()
            },{
                hideProgressDialog()
                if (it.value=="1"){
                    activity?.toasty(it.msg.toString())
                    isEnableToBack=true
                    onBackPress()
                }
                else{
                    activity?.toasty(it.msg!!,2)
                }
            },{
                Log.e("error",it.message!!)
            },{
                hideProgressDialog()
            })
        }

        setPhone()


    }
    private fun setPhone() {
        call_lay.onClick {
            setPermissionsPhone {
                Util.onCall(activity!!,cap_phone)
            }
        }

        cancel_btn.onClick {
            showPopUpCat(orderId)
        }
    }

    private fun showPopUpCat(
        orderId: Int) {
        var reason: Reason?

        val myItems = reasons
        val dialog = MaterialDialog(activity!!)
        dialog.title(R.string.cancel_reason)
        dialog.positiveButton(res = R.string.confirm)

        dialog.show {
            cornerRadius(16f)
            activity!!.theme.applyStyle(R.style.AppTheme_Custom,true)
            listItemsSingleChoice(res = R.string.cancel_reason, items = myItems.map {
                it.reason.toString()
            })

            {dialog, indices, item ->
                val filter = reasons.filter {
                    item.toString() == it.reason
                }
                reason=filter[0]
                sendRequestCancel(reason,orderId)
            }
        }

    }



    private fun sendRequestCancel(
        reason: Reason? = null,
        orderId: Int)
    {
        addDisposable(repo.cancelOrder(orderId.toString(),reason?.id, mPrefs.token!!)
            .subscribeWithLoading({
                showProgressDialog()
            },{
                if (it.value=="1"){
                    activity!!.toasty(it.msg!!)
                }
                else{
                    activity!!.toasty(it.msg!!,2)
                }
            }
                ,{
                    //  popup.dismiss()
                    hideProgressDialog()
                    Log.e("error",it.message)
                    activity?.toasty(it.message!!,2)
                },{
                    hideProgressDialog()

                    isEnableToBack=true
                    onBackPress()
                })
        )
    }




    private fun setChangeTrip() {
        MessageService.orderId_.observe(this, Observer {
            orderId=it
            sendRequest(it.toString())
        })

        MessageService.state_.observe(this, Observer{
            when(MessageService.state_.value){
                    "finishSimpleOrder"->{
                        cancel_lay.visibility=View.VISIBLE
                        cancel_btn.visibility=View.VISIBLE
                        sendReqestFinished(orderId)
                    }
                "ConfirmfinishSimpleOrder"->{
                    mData?.let {
                        cancel_lay.visibility=View.VISIBLE
                        cancel_btn.visibility=View.VISIBLE
                        showRatingDialog(it)
                    }

                     }
                "startJourney"->{
                        cancel_lay.visibility=View.GONE
                        cancel_btn.visibility=View.GONE
                     }
                    "withdrawOrder"->{
                        mMap?.clear()
                        isEnableToBack=false
                        SocketConnection.onClose()
                        marker=null
                        riple.startRippleAnimation()
                        riple.visibility = View.VISIBLE
                        content.visibility = View.VISIBLE
                        bottom_sheet.visibility=View.GONE
                        cancel_lay.visibility=View.VISIBLE
                        cancel_btn.visibility=View.VISIBLE
                    }
                else->{
                    cancel_lay.visibility=View.VISIBLE
                    cancel_btn.visibility=View.VISIBLE
                }

            }
        })
    }





    private fun sendReqestFinished(orderId: Int) {
        repo.rideDetails(orderId,mPrefs.token!!).subscribeWithLoading(
            {
                showProgressDialog()
            },{
                SocketConnection.onClose()
                if (it.value=="1"){
                    mData=it.data
                    mMap?.clear()
                    drawSrcDist()
                }
            },{
                SocketConnection.onClose()
                hideProgressDialog()
                activity!!.toasty(getString(R.string.error_connection),2)
            },{
                cancel_btn.visibility=View.GONE
                cancel_lay.visibility=View.GONE
                hideProgressDialog()
                isEnableToBack=true
            }
        )

    }



    private fun drawSrcDist() {
        addDisposable(repo.getRountes(
            "${mData!!.start_lat},${mData!!.start_long}",
            "${mData!!.end_lat},${mData!!.end_long}"
            ,mPrefs.user!!.googlekey!!
        ).subscribeWithLoading({

        },{
            val routes = it.routes
            if(routes.isNotEmpty()) {
                val latLngList = getDirectionPolylines(routes)
                MapUtils.drawRouteOnMap(context!!,mMap!!,latLngList)
            }

        },{
            activity!!.toasty(getString(R.string.error_connection),2)
            //Log.e("error",it.message)
        },{

        }))
    }


    private fun showRatingDialog(data: com.aait.getak.models.order_details_model.Data) {
        RateDialog(data,{
            sendRequestRate(it)
        }) {
            isEnableToBack=true
            onBackPress()
        }.also {
            it.show(fragmentManager!!,"rate")

        }

    }


    private fun sendRequestRate(rate: Float) {
        repo.rateUser(user_id=captain_id,rating = rate,token = mPrefs.token!!)
            .subscribeWithLoading({
                showProgressDialog()
            },{

                if (it.value=="1"){
                    hideProgressDialog()

                }
                else{
                    activity!!.toasty(it.msg!!,2)

                }
            },{
               // isEnableToBack=true
               // hideProgressDialog()

                Log.e("error",it.message!!)
               activity?.toasty(getString(R.string.error_connection),2)
            },{
                isEnableToBack=true
                onBackPress()
            })
    }



    private fun sendRequest(orderId: String) {

        repo.captin_details(orderId.toInt(),mPrefs.token!!).subscribeWithLoading({
            riple.visibility=View.GONE
            riple.stopRippleAnimation()
            content.visibility=View.GONE
            bottom_sheet.visibility=View.VISIBLE
        },{
            if (it.value=="1"){
                with(it.data){
                    cap_name.text=name
                    cap_rate.text=rate.toString()
                    cap_car_brand.text=carBrand
                    cap_car_plate.text=carNumber
                    cap_phone=phone
                    if (marker==null) {
                        setMarker(it.data)
                    }
                    trip_state.visibility=View.VISIBLE
                    when (it.data.captain_status) {
                                 "captain_accept"->{
                                     trip_state.text=getString(R.string.captain_accepted)

                                 }

                                "captain_in_road"->{
                                    trip_state.text=getString(R.string.captain_in_way)
                                }


                                "captain_arrived"->{
                                    trip_state.text=getString(R.string.captain_arrived)
                                }


                                "start_journey"->{
                                    trip_state.text=getString(R.string.ride_started)
                                    cancel_btn.visibility=View.GONE
                                    cancel_lay.visibility=View.GONE
                                }

                                "captain_finished"->{
                                    trip_state.text=getString(R.string.ride_end)
                                }

                        else -> {
                            trip_state.visibility=View.GONE
                        }
                    }
                    Picasso.get().load(avatar!!).into(captain_img)
                    this@TrackingFragment.startLat = startLat
                    this@TrackingFragment.startLong = startLong
                    this@TrackingFragment.captain_id=captain_id
                    this@TrackingFragment.endLat = endLat
                    this@TrackingFragment.endLng = endLong
                    from_short_loc.text=startAddress

                    if (!endAddress.isNullOrBlank()){
                        to_short_loc.text=endAddress
                    }
                    payment_opt_lay.text=payment_type
                    if (captain_status=="started_journey") {
                        cancel_lay.visibility = View.GONE
                    }
                    else{
                        cancel_lay.visibility = View.VISIBLE
                    }
                }
            }
        },{
            isEnableToBack=true
            Log.e("error",it.message!!+",")
        },{
            isEnableToBack=false
            setSocket()
            if (reasons.isEmpty()) {
                sendRequstReassons()
            }
        })
    }

    private fun setMarker(data: Data) {
        trip = data
        captainId = data.captain_id
        mOldlat=data.captain_lat!!
        mOldlng = data.captain_lng!!
        carMarker = MarkerOptions()
        startPosition = LatLng(mOldlat, mOldlng)
        carMarker!!.position(startPosition!!)
        carMarker!!.draggable(false)
        carMarker!!.flat(true)
        marker=mMap!!.addMarker(carMarker)
        marker!!.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.car2))
        marker!!.setAnchor(0.5f,0.5f)
        mMap!!.moveCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition.Builder()
                    .target(startPosition!!)
                    .zoom(zoom)
                    .build()
            )
        )
        addTrack()

    }

    private fun addTrack() {
        val jsonObject = JSONObject()
        jsonObject.put("tracker_id", Gson().toJson(mPrefs.user?.id))
        jsonObject.put("captain_id", Gson().toJson(captainId))
        Log.e("emmit", Gson().toJson(captainId)+","+ Gson().toJson(mPrefs.user?.id))
        SocketConnection.addEvent("addtracker",jsonObject)

    }

    private fun sendRequstReassons() {
        repo.cancel(mPrefs.token!!).subscribeWithLoading({

        },{
            if (it.value=="1") {
                reasons = it.data
            }
        },{
            Log.e("erorr",it.message)
        },{

        })
    }



                    class LocationAddressResultReceiver internal constructor(val onResult :(
                    title:String,
                    result:String
                    ) -> Unit,handler: Handler?) : ResultReceiver(handler) {
                    override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
                        if (resultCode == 0) {
                            Log.d("Address", "Location null retrying")
                        }
                        if (resultCode == 1) {
                            Log.e("address", "not found")
                        }
                        val title = resultData.getString("title")
                        val currentAdd = resultData.getString("address")
                        onResult(title!!, currentAdd!!)

                    }

                }
                private fun setBehaviour() {
                    var behavior = BottomSheetBehavior.from(bottom_sheet)

                    behavior.addBottomSheetCallback(object : BottomSheetCallback() {
                        override fun onStateChanged(@NonNull bottomSheet: View, newState: Int) { // React to state change
                            if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                                ///fab.setVisibility(View.GONE)
                            } else {
                                //fab.setVisibility(View.VISIBLE)
                            }
                        }

                        override fun onSlide(@NonNull bottomSheet: View, slideOffset: Float) { // React to dragging events
                            Log.e("onSlide", "onSlide")
                        }
                    })

                    behavior.peekHeight = 600
                    behavior.isHideable = false
                }

                override fun init(view: View) {
                }


                override fun onDestroy() {
                    fusedLocationClient.removeLocationUpdates(locationCallback)
                    super.onDestroy()
                }

    override fun onStop() {
        isTrackingVisible=false
        super.onStop()
       // SocketConnection.onClose()


    }

    override fun onStart() {
        isTrackingVisible=true
        super.onStart()

    }

                private fun prepareMap() {
                    mMap!!.uiSettings.isMapToolbarEnabled = true
                    mMap!!.uiSettings.isMyLocationButtonEnabled = false


                    val isPermissionGranted = checkLocation(activity!!)
                    if (activity!!.isGpsEnabled()) {
                        if (!isPermissionGranted) {
                            ActivityCompat.requestPermissions(
                                activity!!,
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                                ),
                                REQUEST_CODE_CH2
                            )
                        } else {
                            getLocation()
                        }
                    } else {
                        activity!!.supportFragmentManager.showGpsAlert(activity!!)
                    }

                }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
                    mMap!!.apply {
                        isMyLocationEnabled = true
                        startLocationUpdates()

                    }

                }

    override fun onMapReady(mMap_: GoogleMap?) {
                  //  setMap()
                mMap=mMap_
                    prepareMap()
                    setChangeTrip()
        mMap?.setOnCameraMoveListener {
            zoom=mMap?.cameraPosition!!.zoom
        }


    }


    private fun setSocket() {
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
                activity?.runOnUiThread {
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

    override fun onMapClick(p0: LatLng?) {

    }

    override fun onBackPress(): Boolean {
        return if (isEnableToBack) {
            orderId=0
            val fm: FragmentManager = activity?.supportFragmentManager!!
            fm.popBackStack("track",FragmentManager.POP_BACK_STACK_INCLUSIVE)
            true
        } else {
            true
        }
                }


            }*/

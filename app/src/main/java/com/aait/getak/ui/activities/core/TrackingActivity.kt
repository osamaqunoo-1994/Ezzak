package com.aait.getak.ui.activities.core

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.location.LocationRequest
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import androidx.annotation.NonNull
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.aait.getak.R
import com.aait.getak.base.ParentActivity
import com.aait.getak.fcm.MessageService
import com.aait.getak.models.captin_model.Data
import com.aait.getak.models.client_later_model.Reason
import com.aait.getak.models.payment.PaymentWaysData
import com.aait.getak.network.remote_db.RetroWeb
import com.aait.getak.network.remote_db.SocketConnection
import com.aait.getak.ui.activities.splash.SplashActivity
import com.aait.getak.ui.adapters.PaymentWaysAdapter
import com.aait.getak.ui.dialogs.ChargeAmountSheet
import com.aait.getak.ui.dialogs.PaymentBottomSheet
import com.aait.getak.ui.dialogs.RateDialog
import com.aait.getak.utils.*
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.oppwa.mobile.connect.checkout.dialog.CheckoutActivity
import com.oppwa.mobile.connect.checkout.meta.CheckoutSettings
import com.oppwa.mobile.connect.exception.PaymentError
import com.oppwa.mobile.connect.provider.Connect
import com.oppwa.mobile.connect.provider.Transaction
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.from_to_only.*
import kotlinx.android.synthetic.main.my_wallet_full.*
import kotlinx.android.synthetic.main.tracking_map.*
import kotlinx.coroutines.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.json.JSONObject
import java.util.*
import java.util.concurrent.TimeUnit

class TrackingActivity : ParentActivity() , OnMapReadyCallback,
    GoogleMap.OnMapClickListener {

    private var paymentType: String?=""
    private var orderType: String?=""
//    private lateinit var urwayPayment: UrwayPayment
    private var isPaidVisa: Boolean=false
    private  var handler: Handler?  = Handler()
    private  var timer= Timer()
    private var convId: String?=""

    override val layoutResource: Int
        get() = R.layout.tracking_map



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
        var isTrackingVisible: Boolean=false
    }

    private var orderId: Int=0
    private var mData: com.aait.getak.models.order_details_model.Data?=null

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
    private var isLoadingView=false

    override fun init() {
        orderId = intent.getIntExtra("order_id",0)
        isAcceptedRide = intent.getBooleanExtra("isAcceptedRide",false)
        setMap()
        setLocationChange()

        if (orderId==0 || !isAcceptedRide){
           riple.visibility = View.VISIBLE
           content.visibility = View.VISIBLE
           riple.startRippleAnimation()
           coords.visibility=View.GONE
           bottom_sheet.visibility= View.GONE
           val layoutParams = scroll.layoutParams
            layoutParams.height= LinearLayoutCompat.LayoutParams.MATCH_PARENT
            scroll.layoutParams=layoutParams
            isLoadingView=true
            handleMessage()
        }
        else{
            coords.visibility=View.VISIBLE
            bottom_sheet.visibility= View.VISIBLE
            sendRequest(orderId.toString())
            val layoutParams = scroll.layoutParams
            layoutParams.height=850
            scroll.layoutParams=layoutParams
            riple.visibility = View.GONE
            content.visibility = View.GONE
            isLoadingView=false
            timer.cancel()
        }

        setBehaviour()
        setActions()
    }

    private fun handleMessage() {
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    if (coords.visibility==View.VISIBLE) {
                        msg_retry_lay.visibility = View.GONE
                    } else {
                        msg_retry_lay.visibility = View.VISIBLE
                    }
                }
            }
        }, TimeUnit.MINUTES.toMillis(1),TimeUnit.MINUTES.toMillis(1))

    }

    private fun sendRenew(){
        repo.renotifyCaptain(orderId,mPrefs?.token!!).subscribeWithLoading({showProgressDialog()},{
            it.msg.let { it1 ->
                hideProgressDialog()
                msg_retry_lay.visibility=View.GONE
                toasty(
                    it1
                )
            }
        },{hanldeError(it.message)},{hideProgressDialog()})
    }

    private fun hanldeError(message: String?) {
        hideProgressDialog()
        msg_retry_lay.visibility=View.GONE
        toasty(message!!,2)
    }

    private fun checkShowCaptain(){
        //zero or not accepted
        if (orderId==0) {
            riple.visibility = View.VISIBLE
            content.visibility = View.VISIBLE
            riple.startRippleAnimation()
            coords.visibility=View.GONE

            bottom_sheet.visibility= View.GONE
            val layoutParams = scroll.layoutParams
            layoutParams.height= LinearLayoutCompat.LayoutParams.MATCH_PARENT
            scroll.layoutParams=layoutParams

        }
        else{
            coords.visibility=View.VISIBLE
            bottom_sheet.visibility= View.VISIBLE
           // sendRequest(orderId.toString())
            val layoutParams = scroll.layoutParams
            layoutParams.height=850
            scroll.layoutParams=layoutParams
            riple.visibility = View.GONE
            msg_retry_lay.visibility = View.GONE

            content.visibility = View.GONE
        }
    }
    private fun setMap() {
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapView) as (SupportMapFragment)
        mapFragment.getMapAsync(this)
        try {
            MapsInitializer.initialize(this)
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }



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



    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.requestLocationUpdates(
            LocationRequest(),locationCallback,
            null)
    }



    private fun setActions() {
        retry_btn.onClick {
            sendRenew()
        }
        chat_iv.onClick {
            startActivity(Intent(applicationContext,ChatActivity::class.java).apply {
                putExtra("conv_id",convId?.toInt())
            })
        }
        from_title_loc.onClick {
            if (startLat!=0.0 && startLong!=0.0){
                forwardMap(startLat.toString(),startLong.toString())
            }
        }
        to_short_loc.onClick {
            if (endLat!=0.0 && endLng!=0.0){
                forwardMap(endLat.toString(),endLng.toString())
            }
        }
        cancel_trip.onClick {
            repo.cancelOrder(orderId.toString(),token = mPrefs?.token!!).subscribeWithLoading({
                showProgressDialog()
            },{
                hideProgressDialog()
                if (it.value=="1"){
                    toasty(it.msg.toString())
                    goHome()
                }
                else{
                    toasty(it.msg!!,2)
                }
            },{
                Log.e("error",it.message!!)
            },{
                hideProgressDialog()
            })
        }

        setPhone()

    }

    private fun goHome() {
        startActivity(Intent(this,MapPreviewActivity::class.java)).also {
            finish()
        }
    }

    private fun setPhone() {
        call_lay.onClick {
            setPermissionsPhone {
                Util.onCall(this@TrackingActivity,cap_phone)
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
        val dialog = MaterialDialog(this)
        dialog.title(R.string.cancel_reason)
        dialog.positiveButton(res = R.string.confirm)

        dialog.show {
            cornerRadius(16f)
            theme.applyStyle(R.style.AppTheme_Custom,true)
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
        addDisposable(repo.cancelOrder(orderId.toString(),reason?.id, mPrefs?.token!!)
            .subscribeWithLoading({
                showProgressDialog()
            },{
                if (it.value=="1"){
                    toasty(it.msg!!)
                }
                else{
                    toasty(it.msg!!,2)
                }
            }
                ,{
                    //  popup.dismiss()
                    hideProgressDialog()
                    it.message?.let { it1 -> Log.e("error", it1) }
                    toasty(it.message!!,2)
                },{
                    hideProgressDialog()
                    goHome()

                })
        )
    }




    private fun setChangeTrip() {
        MessageService.orderId_.observe(this, Observer {
            if (it>=orderId && orderId!=0) {
                orderId = it
                sendRequest(it.toString())
            }
        })

        MessageService.state_.observe(this, Observer{
            msg_retry_lay.visibility = View.GONE
            when(MessageService.state_.value){
                "finishSimpleOrder"->{
                    Log.i("orderDetailsLol", "sendReqestFinished: ${mData?.paymentType}")
                    sendReqestFinished(orderId)
                }
                "ConfirmfinishSimpleOrder"->{
                    mData?.let {
                        showRatingDialog(it)
                    }

                }
                "startJourney"->{
                    cancel_lay.visibility= View.GONE
                    cancel_trip.visibility= View.GONE

                }
                "captain_in_road"->{
                    trip_state.text=getString(R.string.captain_in_way)
                    if (orderType=="food") {
                        trip_state.text=getString(R.string.captain_in_way_rest)
                        expected_lay.visibility=View.GONE
                    }
                    expected_lay.visibility=View.VISIBLE

                }

                "captain_arrived"->{
                    trip_state.text=getString(R.string.captain_arrived)
                    if (orderType=="food") {
                        trip_state.text=getString(R.string.captain_arrived_rest)
                        cancel_trip.visibility= View.GONE
                        cancel_lay.visibility= View.GONE
                    }
                    expected_lay.visibility=View.GONE
                }

                "captain_finished"->{
                    trip_state.text=getString(R.string.ride_end)
                    expected_lay.visibility=View.GONE
                    if (orderType=="food") trip_state.text=getString(R.string.captain_recieved)
                }

                "withdrawOrder"->{
//                    cancel_lay.visibility= View.VISIBLE
                    cancel_trip.visibility= View.VISIBLE
                    val layoutParams = scroll.layoutParams
                    layoutParams.height= LinearLayoutCompat.LayoutParams.MATCH_PARENT
                    scroll.layoutParams=layoutParams
                    isLoadingView=true
                    mMap?.clear()
                    isEnableToBack=false
                    SocketConnection.onClose()
                    marker=null
                    riple.startRippleAnimation()
                    riple.visibility = View.VISIBLE
                    content.visibility = View.VISIBLE
                    bottom_sheet.visibility= View.GONE
                    setBehaviour(false)
//                    cancel_lay.visibility= View.VISIBLE
                    cancel_trip.visibility= View.VISIBLE
                }
                else->{
//                    cancel_lay.visibility= View.VISIBLE
                    cancel_trip.visibility= View.VISIBLE
                }

            }
        })
    }




    private fun sendReqestFinished(orderId: Int) {
        Log.i("orderDetailsLol", "sendReqestFinished: $orderId")
        repo.rideDetails(orderId,mPrefs?.token!!).subscribeWithLoading(
            {
            },{
                hideProgressDialog()
                SocketConnection.onClose()
                if (it.value=="1"){
                    //convId=it.data?.conversation_id
                    mData=it.data
                    mMap?.clear()
                    drawSrcDist()
                    Log.i("orderDetailsLol", "sendReqestFinished: ${mData?.paymentType}")
                    if (mData?.paymentType=="visa" || mData?.paymentType=="stc"){
                        isPaidVisa=true
                        //it.data?.let { order -> showRatingDialog(order) }
                        if (it.data?.total_required_price?.toFloat()!! > 0f) {
                            openChargeDialog(it.data?.id.toString(),it.data?.total_required_price!!)
                        }
                        else{
                            showRatingDialog(it.data!!)
                        }
                    }
                }
            },{
                SocketConnection.onClose()
                hideProgressDialog()
                toasty(getString(R.string.error_connection),2)
            },{
                cancel_trip.visibility= View.GONE
                cancel_lay.visibility= View.GONE
                hideProgressDialog()

            }
        )
    }
    //if payment == stc -> action code = 13 , payment == visa code=1
    private fun confirmPayment(data: com.aait.getak.models.order_details_model.Data?) {

//        paymentType=data?.paymentType
//        var actionCode="1"
//        if (data?.paymentType=="stc") actionCode="13"
//        urwayPayment=UrwayPayment()
//        urwayPayment.makepaymentService(
//            "${data?.total_required_price}", this, actionCode, "SAR", "", "trip", "${data?.id}", ""
//            , "", "", mPrefs?.user?.phone.toString() + "@ezzak.com", "", "", "", "", "SA",
//            "${data?.id}", "A", mPrefs?.user?.card_token + "", "0")

    }


    private fun drawSrcDist() {
        addDisposable(repo.getRountes(
            "${mData!!.start_lat},${mData!!.start_long}",
            "${mData!!.end_lat},${mData!!.end_long}"
                ,getString(R.string.google_maps_key)

        ).subscribeWithLoading({

        },{
            val routes = it.routes
            if(routes.isNotEmpty()) {
                val latLngList = MapUtils.getDirectionPolylines(routes)
                MapUtils.drawRouteOnMap(this,mMap!!,latLngList)
            }

        },{
            toasty(getString(R.string.error_connection),2)
            //Log.e("error",it.message)
        },{

        }))
    }


    private fun showRatingDialog(data: com.aait.getak.models.order_details_model.Data) {
        RateDialog(data,{
            sendRequestRate(it)
        }) {
        //    goHome()
        }.also {
            it.show(supportFragmentManager,"rate")

        }

    }


    private fun sendRequestRate(rate: Float) {
        repo.rateUser(user_id=captain_id,rating = rate,token = mPrefs?.token!!)
            .subscribeWithLoading({
                showProgressDialog()
            },{

                if (it.value=="1"){
                    hideProgressDialog()

                }
                else{
                    toasty(it.msg!!,2)
                }
            },{
                // isEnableToBack=true
                // hideProgressDialog()
                Log.e("error",it.message!!)
                toasty(getString(R.string.error_connection),2)
            },{
                goHome()
            })
    }



    private fun sendRequest(orderId: String) {

        repo.captin_details(orderId.toInt(),mPrefs?.token!!).subscribeWithLoading({
            riple.visibility= View.GONE
            riple.stopRippleAnimation()
            content.visibility= View.GONE

            bottom_sheet.visibility= View.VISIBLE
        },{
            if (it.value=="1"){
                with(it.data){
                    orderType=order_type
                    cap_name.text=name
                    convId=conversation_id
                    cap_rate.text=rate.toString()
                    cap_car_brand.text=carBrand
                    cap_car_plate.text=carNumber
                    cap_car_color.text =carColor
                    cap_phone=phone
                    if (marker==null) {
                        setMarker(it.data)
                    }
                    if (notes.isNullOrEmpty()){
                        notes_lay.visibility=View.GONE
                    }
                    else{
                        notes_lay.visibility=View.VISIBLE
                        notes_tv.text=notes
                    }
                    paymentType = payment_type
                    when (paymentType) {
                        "cash" -> {
                            iv_cash.setImageResource(R.mipmap.moneyy)
                            payment_opt_lay.text=getString(R.string.cash)
                        }
                        "stc" -> {
                            iv_cash.setImageResource(R.mipmap.stcpay)
                            payment_opt_lay.text=getString(R.string.stc_payment)
                        }
                        else -> {
                            iv_cash.setImageResource(R.mipmap.visa)
                            payment_opt_lay.text=getString(R.string.elect_payment)
                        }
                    }

                    trip_state.visibility= View.VISIBLE
                    checkShowCaptain()
                    when (it.data.captain_status) {
                        "captain_accept"->{
                            trip_state.text=getString(R.string.captain_accepted)
                            expected_lay.visibility=View.VISIBLE
                            expected_tv.text=expectedPeriod
                            isLoadingView=false
                        }

                        "captain_in_road"->{
                            trip_state.text=getString(R.string.captain_in_way)
                            if (order_type=="food") trip_state.text=getString(R.string.captain_in_way_rest)
                            expected_lay.visibility=View.VISIBLE
                            expected_tv.text=expectedPeriod
                        }


                        "captain_arrived"->{
                            trip_state.text=getString(R.string.captain_arrived)
                            if (order_type=="food") {
                                trip_state.text=getString(R.string.captain_arrived_rest)
                                cancel_trip.visibility= View.GONE
                                cancel_lay.visibility= View.GONE
                            }
                            expected_lay.visibility=View.GONE
                            expected_tv.text=expectedPeriod
                        }


                        "start_journey"->{
                            trip_state.text=getString(R.string.ride_started)
                            cancel_trip.visibility= View.GONE
                            cancel_lay.visibility= View.GONE
                            if (order_type=="food") trip_state.text=getString(R.string.captain_accept_order)
                            expected_lay.visibility=View.GONE
                            expected_tv.text=expectedPeriod
                        }

                        "captain_finished"->{
                            trip_state.text=getString(R.string.ride_end)
                            expected_lay.visibility=View.GONE
                            expected_tv.text=expectedPeriod
                            if (order_type=="food") trip_state.text=getString(R.string.captain_recieved)
                        }
                        "captain_finished_no_payment"->{
                            sendReqestFinished(orderId.toInt())
//                            if(paymentType == "visa"){
//                                if (it.data.total_required_price?.toFloat()!! > 0f) {
//                                    openChargeDialog(it.data.id.toString(),it.data.total_required_price!!)
//                                }
//                            }
                        }
                        else -> {
                            trip_state.visibility= View.GONE
                            expected_lay.visibility=View.GONE
                            expected_tv.text=expectedPeriod
                            if (order_type=="food") trip_state.text=getString(R.string.captain_finished_rest)
                        }
                    }
                    Picasso.get().load(avatar!!).into(captain_img)
                    this@TrackingActivity.startLat = startLat
                    this@TrackingActivity.startLong = startLong
                    this@TrackingActivity.captain_id=captain_id
                    this@TrackingActivity.endLat = endLat
                    this@TrackingActivity.endLng = endLong
                    from_short_loc.text=startAddress

                    if (!endAddress.isNullOrBlank()){
                        to_short_loc.text=endAddress
                    }
                    share_lay.setOnClickListener {
                        shareUrl("${RetroWeb.BASEURL}shareOrder/${orderId}")
                    }
                }
            }
        },{
            Log.e("error",it.message!!+",")

        },{
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
        jsonObject.put("tracker_id", Gson().toJson(mPrefs?.user?.id))
        jsonObject.put("captain_id", Gson().toJson(captainId))
        Log.e("emmit", Gson().toJson(captainId)+","+ Gson().toJson(mPrefs?.user?.id))
        SocketConnection.addEvent("addtracker",jsonObject)

    }

    private fun sendRequstReassons() {
        repo.cancel(mPrefs?.token!!).subscribeWithLoading({

        },{
            if (it.value=="1") {
                reasons = it.data
            }
        },{
            it.message?.let { it1 -> Log.e("erorr", it1) }
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
    private fun setBehaviour(show:Boolean?=true) {
        var behavior = BottomSheetBehavior.from(bottom_sheet)

        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
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

        /*behavior.peekHeight = (height-(height.toFloat()*0.3)).toInt()*/
        if (SplashActivity.dpHeight>800){
            behavior.peekHeight = 600
        }
        else{
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            val height: Int = displayMetrics.heightPixels
            behavior.peekHeight = (height-(height.toFloat()*0.4)).toInt()
        }
        if (show==false) {
            behavior.peekHeight = 0
        }
        behavior.isHideable = false
    }


    override fun onDestroy() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        MessageService.state_.removeObservers(this)
        MessageService.orderId_.removeObservers(this)
        scope.cancel()
        super.onDestroy()
    }

    override fun onStop() {
        isTrackingVisible=false
        timer.cancel()
        super.onStop()
        SocketConnection.onClose()

    }

    override fun onStart() {
        isTrackingVisible=true
        super.onStart()
        SocketConnection.connect()

    }

    private fun prepareMap() {
        mMap!!.uiSettings.isMapToolbarEnabled = true
        mMap!!.uiSettings.isMyLocationButtonEnabled = false


        val isPermissionGranted = checkLocation(this)
        if (isGpsEnabled()) {
            if (!isPermissionGranted) {
                ActivityCompat.requestPermissions(
                    this,
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
            supportFragmentManager.showGpsAlert(this)
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
       // setupAnim()
        mMap=mMap_
        prepareMap()

        setChangeTrip()
        mMap?.setOnCameraMoveListener {
            zoom=mMap?.cameraPosition!!.zoom
        }


    }


    val scope = CoroutineScope(Dispatchers.IO+ Job())

    private fun setSocket() {

        SocketConnection.onListen("trackorder") {
                isConnected, data ->
            val data = data[0] as String
            val car = JSONObject(data)
            var captainId = car.getInt("captain_id")
            Log.e("captain1_",captainId.toString())
            if (car.getInt("captain_id")==this@TrackingActivity.captainId){
                Log.e("captain2",captainId.toString())
                val lat = car.getDouble("lat")
                val lng = car.getDouble("lng")
                startPosition = LatLng(lat, lng)
                scope.launch(Dispatchers.Main){
                        MapUtils.rotateMarker(marker!!,
                            MapUtils.bearingBetweenLocations(LatLng(mOldlat,mOldlng),LatLng(lat, lng)))
                        MapUtils.animateMarker(mMap!!, marker!!, LatLng(lat, lng), false,zoom)
                        mOldlat = lat
                        mOldlng = lng
                    }
            }


        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i("onActivityResult", "onActivityResult: $requestCode")
        when (requestCode){
            2-> {
                if (data != null) {
                    val message = data.getStringExtra("MESSAGE")
                    val jsonObj = JSONObject(message)
                    val tranId = jsonObj.get("TranId").toString()
                    val amount = jsonObj.get("amount").toString()
                    val result = jsonObj.get("result").toString()
                    val payId = jsonObj.get("payId").toString()
                    sendRequestConfirmPayment(tranId, amount, payId)
                    //Log.e("amount",jsonObj.toString()+"{"+payId+","+amount+"}")
                }
                mData?.let { showRatingDialog(it) }
            }
        }
        when(resultCode){
            CheckoutActivity.RESULT_OK -> {
                /* transaction completed */
                val transaction: Transaction = data!!.getParcelableExtra(CheckoutActivity.CHECKOUT_RESULT_TRANSACTION)!!
                /* resource path if needed */
                val resourcePath =
                    data.getStringExtra(CheckoutActivity.CHECKOUT_RESULT_RESOURCE_PATH)
                resourcePath?.let { hyperResult(it) }
            }

            CheckoutActivity.RESULT_CANCELED -> {
                /* shopper cancelled the checkout process */
            }

            CheckoutActivity.RESULT_ERROR -> {
                /* error occurred */
                val error: PaymentError = data!!.getParcelableExtra(CheckoutActivity.CHECKOUT_RESULT_ERROR)!!
            }
        }

    }

    private fun sendRequestConfirmPayment(
        tranId: String,
        amount: String,
        payId: String
    ) {
        var cardBrand="VISA"
        if (paymentType=="stc") cardBrand="STCPAY"

        repo.confirmPayment(payId,tranId,orderId.toString(),amount,cardBrand,mPrefs?.token!!).subscribeWithLoading(
            {},{handleData(it.data)},{handleError(it.message)},{}
        )
    }

    private fun handleError(message: String?) {
        Log.e("error",message.toString())
    }

    private fun handleData(data: String?) {
        Log.e("success",data.toString())
    }

    override fun onMapClick(p0: LatLng?) {

    }

    private fun shareUrl(txt:String) {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
            shareIntent.putExtra(Intent.EXTRA_TEXT, txt)
            startActivity(Intent.createChooser(shareIntent, ""))
        } catch (e: java.lang.Exception) {
            //e.toString();
        }
    }


    private fun openChargeDialog(orderId: String,totalPrice:String){
        addDisposable(repo.paymentWays().subscribeWithLoading({
            //   btn.setProgress(100f)
            // btn.revertAnimation()
            showProgressDialog()
        },{

            if (it.value=="1"){
                hideProgressDialog()
                val dialog = PaymentBottomSheet(it.data){
                    paymentType = it
                    hyperIndex(it,totalPrice,orderId)
                }
                dialog.show(supportFragmentManager,"chargeDialog")
            }
            else{
                toasty(it.msg,2)
            }
        },{
            // isEnableToBack=true
            // hideProgressDialog()
            Log.e("error",it.message!!)
            toasty(getString(R.string.error_connection),2)
        },{
        }))
    }

    private fun hyperIndex(
        type:String,amount:String,orderId:String
    ) {
        val newType = if(type == "MASTER") "VISA" else type
        addDisposable(repo.hyperIndex(orderId,newType,amount).subscribeWithLoading({
            //   btn.setProgress(100f)
            // btn.revertAnimation()
            showProgressDialog()
        },{
            hideProgressDialog()
            if (it.value=="1"){
                it.data?.let { it1 -> openCheckOut(type,it1) }
            }
            else{
                it.msg?.let { it1 -> toasty(it1,2) }
            }
        },{
            // isEnableToBack=true
            // hideProgressDialog()
            Log.e("error",it.message!!)
            toasty(getString(R.string.error_connection),2)
        },{
        }))
    }

    private fun openCheckOut(type:String,checkoutId:String){
        val paymentBrands = hashSetOf(type)

        val checkoutSettings = CheckoutSettings(checkoutId, paymentBrands, Connect.ProviderMode.LIVE)
        checkoutSettings.shopperResultUrl = "ezzakclienttrip://result"
        val intent = checkoutSettings.createCheckoutActivityIntent(this)
        paymentType = if(type == "MASTER") "VISA" else type
        startActivityForResult(intent, CheckoutActivity.REQUEST_CODE_CHECKOUT)
    }


    private fun hyperResult(
        resourcePath:String
    ) {
        Log.i("hyperResult", "hyperResult: $resourcePath")
        addDisposable(repo.hyperResult(orderId.toString(),paymentType.toString(),resourcePath).subscribeWithLoading({
            //   btn.setProgress(100f)
            // btn.revertAnimation()
            showProgressDialog()
        },{
            hideProgressDialog()

            if (it.value=="1"){
                it.msg?.let { it1 -> toasty(it1,1) }
                finishAffinity()
                startActivity(Intent(this,MapPreviewActivity::class.java))
            }
            else{
                it.msg?.let { it1 -> toasty(it1,2) }
            }
        },{
            // isEnableToBack=true
            // hideProgressDialog()
            Log.e("error",it.message!!)
            toasty(getString(R.string.error_connection),2)
        },{
        }))
    }
}

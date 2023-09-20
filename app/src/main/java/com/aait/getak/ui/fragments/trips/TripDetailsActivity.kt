package com.aait.getak.ui.fragments.trips

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.aait.getak.R
import com.aait.getak.base.ParentActivity
import com.aait.getak.models.client_later_model.Reason
import com.aait.getak.models.route_model.Routes
import com.aait.getak.ui.activities.core.MapPreviewActivity
import com.aait.getak.utils.toasty
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_details.cap_img
import kotlinx.android.synthetic.main.activity_details.capting_name_tv
import kotlinx.android.synthetic.main.activity_details.car_model
import kotlinx.android.synthetic.main.activity_details.car_plate
import kotlinx.android.synthetic.main.activity_details.cost
import kotlinx.android.synthetic.main.activity_details.iv_arrow
import kotlinx.android.synthetic.main.activity_details.mapView
import kotlinx.android.synthetic.main.activity_details.price_details_lay
import kotlinx.android.synthetic.main.activity_details.rate_bar
import kotlinx.android.synthetic.main.activity_details.view
import kotlinx.android.synthetic.main.activity_trip_details.*
import kotlinx.android.synthetic.main.activity_trip_details.cancel_lay
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.ArrayList

class TripDetailsActivity: ParentActivity() , OnMapReadyCallback {

    private var orderId: Int=0
    private  var reasons: List<Reason> = mutableListOf()
    private var mMap: GoogleMap? = null
    private var from_lat: Double = 0.0
    private var from_lng: Double = 0.0
    private var to_lat: Double = 0.0
    private var to_lng: Double = 0.0

    override fun onMapReady(gMap: GoogleMap?) {
        mMap=gMap
        orderId=intent.getIntExtra("order_id",0)
        sendRequest(orderId)

    }

    var showPriceDetails=false

    override val layoutResource: Int
        get() = R.layout.activity_trip_details

    override fun init() {

        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapView) as (SupportMapFragment)
        mapFragment.getMapAsync(this)
        try {
            MapsInitializer.initialize(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        setActions()
    }

    private fun setActions() {
        iv_arrow.onClick {
            showPriceDetails=!showPriceDetails
            if (showPriceDetails){
                iv_arrow.animate().rotation(180f).start()
                price_details_lay.visibility= View.VISIBLE
                view.visibility= View.GONE
            }
            else{
                iv_arrow.animate().rotation(0f).start()
                price_details_lay.visibility= View.GONE
                view.visibility= View.VISIBLE
            }
        }
        cancel_lay.onClick {
            showPopUpCat(orderId)
        }
    }



    @SuppressLint("SetTextI18n")
    private fun sendRequest(orderId: Int) {
        addDisposable(repo.laterOdrderDetails(orderId,mPrefs!!.token!!)
            .subscribeWithLoading({
                showProgressDialog()
            },{
                if (it.value=="1"){
                    with(it.data){
                        if (payment_type=="cash"){
                            payment_iv.setImageResource(R.mipmap.moneyy)
                            payment_tv.text=getString(R.string.cash)
                        }
                        else if (payment_type=="stc"){
                            payment_iv.setImageResource(R.mipmap.stcpay)
                            payment_tv.text=getString(R.string.stc_payment)
                        }
                        else{
                            payment_iv.setImageResource(R.mipmap.visa)
                            payment_tv.text=getString(R.string.elect_payment)
                        }
                        setToolbar(date+" "+time)
                        cost.text=expected_price+" "+currency
                        rate_bar.rating=rate.toFloat()
                        car_model.text=car_brand
                        car_plate.text=car_number
                        from_lat=start_lat.toDouble()
                        from_lng=start_long.toDouble()
                        to_lat=end_lat.toDouble()
                        to_lng=end_long.toDouble()
                        capting_name_tv.text=name
                        if (avatar.isNotBlank()) {
                            Picasso.get().load(avatar).into(cap_img)
                        }
                        drawSrcDist()
                    }

                }
                else{
                    toasty(it.msg,2)
                }
            },{
                hideProgressDialog()
                toasty(getString(R.string.error_connection),2)
                it.message?.let { it1 -> Log.e("error", it1) }
            },{
                hideProgressDialog()
                sendRequstReassons()
            })
        )
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


    private fun sendRequestCancel(
        reason: Reason? = null,
        orderId: Int
    ) {
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

    private fun goHome() {
        startActivity(Intent(this,
            MapPreviewActivity::class.java))
        finish()
    }

    private fun drawRouteOnMap(map: GoogleMap, positions: List<LatLng>) {
        val options = PolylineOptions().width(5f).color(ContextCompat.getColor(applicationContext,R.color.colorAccent)).geodesic(true)
        options.addAll(positions)
        val polyline = map.addPolyline(options)
        //marker of my location
        drawMarkers(map)
    }


    private fun drawMarkers(map: GoogleMap) {
        val builder = LatLngBounds.Builder()
        val markerOptions = MarkerOptions()
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_green_circle))
        val latLng = LatLng(from_lat, from_lng)
        markerOptions.position(latLng)
        mMap!!.addMarker(markerOptions)
        //marker of target Location
        if (to_lat>0.0 && to_lng>0.0) {
            val markerOptions2 = MarkerOptions()
            markerOptions2.icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_two))
            val latLng2 = LatLng(to_lat, to_lng)
            markerOptions2.position(latLng2)
            mMap!!.addMarker(markerOptions2)
            builder.include(markerOptions2.position)
        }

        builder.include(markerOptions.position)

        val bounds = builder.build()

        val padding = 50 // offset from edges of the map in pixels
        val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)
        map.animateCamera(cu)


/*
        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(to_lat, to_lng))
            .zoom(Constant.ZOOM)
            .build()
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
*/
    }

    private fun getDirectionPolylines(routes: MutableList<Routes>): List<LatLng> {
        val directionList = ArrayList<LatLng>()
        for (route in routes) {
            val legs = route.legs
            for (leg in legs) {
                val steps = leg.steps
                for (step in steps) {
                    val polyline = step.polyline
                    val points = polyline.points
                    val singlePolyline = decodePoly(points)
                    for (direction in singlePolyline) {
                        directionList.add(direction)
                    }
                }
            }
        }
        return directionList
    }
    private fun decodePoly(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val p = LatLng(
                lat.toDouble() / 1E5,
                lng.toDouble() / 1E5
            )
            poly.add(p)
        }
        return poly
    }

    private fun drawSrcDist() {
        addDisposable(repo.getRountes(
            "$from_lat,$from_lng",
            "$to_lat,$to_lng"
            ,getString(R.string.google_maps_key)
        ).subscribeWithLoading({

        },{
            val routes = it.routes
            if(routes.isNotEmpty()) {
                val latLngList = getDirectionPolylines(routes)
                drawRouteOnMap(mMap!!, latLngList)
            }
            else{
                drawMarkers(mMap!!)
            }
        },{
            //  toasty(getString(R.string.error_connection),2)
            //Log.e("error",it.message)
        },{

        }))
    }

}

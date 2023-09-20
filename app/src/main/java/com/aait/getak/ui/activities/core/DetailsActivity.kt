package com.aait.getak.ui.activities.core

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.aait.getak.R
import com.aait.getak.base.ParentActivity
import com.aait.getak.models.route_model.Routes
import com.aait.getak.utils.toasty
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_details.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*


class DetailsActivity : ParentActivity() , OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    private var from_lat: Double = 0.0
    private var from_lng: Double = 0.0
    private var to_lat: Double = 0.0
    private var to_lng: Double = 0.0

    override fun onMapReady(gMap: GoogleMap?) {
        mMap=gMap
        val orderId=intent.getIntExtra("order_id",0)
        sendRequest(orderId)

    }

    var showPriceDetails=false

    override val layoutResource: Int
        get() = R.layout.activity_details

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
                view.visibility=View.GONE
            }
            else{
                iv_arrow.animate().rotation(0f).start()
                price_details_lay.visibility= View.GONE
                view.visibility=View.VISIBLE
            }
        }
    }



    @SuppressLint("SetTextI18n")
    private fun sendRequest(orderId: Int) {
        addDisposable(repo.rideDetails(orderId,mPrefs!!.token!!)
            .subscribeWithLoading({
                showProgressDialog()
            },{
                if (it.value=="1"){
                    with(it.data!!){
                        setToolbar(date?:"")
                        cost.text=(total!!-coupon_discount).toString()+" "+currency
                        time_dist.text=distance
                        starting_tv.text=initial.toString()+" "+currency
                        moving_tv.text= "$moving $currency"
                        amount_charged_tv.text=(total!!-coupon_discount).toString()+" "+currency
                        waiting_tv.text=waiting+" "+currency
                        vat_tv.text=vat?: "0 $currency"
                        subtotal_tv.text=(total).toString()+" "+currency
                        rate_bar.rating=captin_rate!!.toFloat()
                        this@DetailsActivity.car_model.text=car_model
                        this@DetailsActivity.car_plate.text=car_plate
                        this@DetailsActivity.from_lat=start_lat
                        this@DetailsActivity.from_lng=start_long
                        this@DetailsActivity.to_lat=end_lat
                        this@DetailsActivity.to_lng=end_long
                        if (paid_balance!="0"){
                            wallet_amount.text=paid_balance+" "+currency
                        }
                        else{
                            wallet_lay.visibility=View.GONE
                        }
                        if (paid_cash!="0"){
                            cash_amount.text=paid_cash+" "+currency
                        }
                        else{
                            cash_lay.visibility=View.GONE
                        }
                        if (paid_visa!="0"){
                            credit_amount.text=paid_visa+" "+currency
                        }
                        else{
                            visa_lay.visibility=View.GONE
                        }
                        if (pocket!="0"){
                            pocket_balance.text= String.format(getString(R.string.add_pocket),pocket+" "+currency)
                        }
                        else{
                            pocket_lay.visibility=View.GONE
                        }
                        if (coupon_discount!=0f){
                            promo_tv.text=coupon_discount.toString()+" "+currency
                        }
                        else{
                            promo_lay.visibility=View.GONE
                        }
                        if (rush_hour_percentage != "0"){
                            rush_lay.visibility=View.VISIBLE
                            rush_tv.text=rush_hour_percentage.toString()
                        }
                        else{
                            rush_lay.visibility=View.GONE
                        }
                        drawSrcDist()
                        capting_name_tv.text=captin_name
                        Picasso.get().load(captin_img).into(cap_img)
                        time_dist.text= String.format(getString(R.string.distance_msg,distance,period))


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
            })
        )
    }


    private fun drawRouteOnMap(map: GoogleMap, positions: List<LatLng>) {
        val options = PolylineOptions().width(5f).color(ContextCompat.getColor(applicationContext,R.color.colorAccent)).geodesic(true)
        options.addAll(positions)
        val polyline = map.addPolyline(options)
        //marker of my location
        drawMarkers(map)
    }

    private fun drawMarkers(map: GoogleMap) {
        val markerOptions = MarkerOptions()
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_green_circle))
        val latLng = LatLng(from_lat, from_lng)
        markerOptions.position(latLng)
        mMap!!.addMarker(markerOptions)
        //marker of target Location
        val markerOptions2 = MarkerOptions()
        markerOptions2.icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_two))
        val latLng2 = LatLng(to_lat, to_lng)
        markerOptions2.position(latLng2)
        mMap!!.addMarker(markerOptions2)

        val builder = LatLngBounds.Builder()
        builder.include(markerOptions.position)
        builder.include(markerOptions2.position)
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

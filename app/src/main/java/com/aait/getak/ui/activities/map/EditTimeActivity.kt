package com.aait.getak.ui.activities.map

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import androidx.core.content.ContextCompat
import com.aait.getak.R
import com.aait.getak.base.ParentActivity
import com.aait.getak.models.client_later_model.Order
import com.aait.getak.models.route_model.Routes
import com.aait.getak.ui.dialogs.DateDialog
import com.aait.getak.ui.dialogs.TimeDialog
import com.aait.getak.utils.Constant
import com.aait.getak.utils.Util
import com.aait.getak.utils.toasty
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_edit_time.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*

class EditTimeActivity : ParentActivity(), OnMapReadyCallback {
    override fun onMapReady(gMap: GoogleMap?) {
        mMap=gMap
        drawSrcDist()
    }

    private lateinit var order: Order
    private var mMap: GoogleMap? = null
    override val layoutResource: Int
        get() = R.layout.activity_edit_time

    override fun init() {
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        setToolbar(getString(R.string.my_orders))
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapView) as (SupportMapFragment)
        mapFragment.getMapAsync(this)

        setLays()

        order = intent.getSerializableExtra("order") as Order
        setData()
        try {
            MapsInitializer.initialize(this)
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setLays() {
        date.isFocusable = false
        date.isClickable = true
        time.isFocusable = false
        time.isClickable = true
        date.onClick {
            DateDialog { year, month,day ->
                date.setText("$year-$month-$day")
            }.show(this@EditTimeActivity.supportFragmentManager, "datePicker")
        }

        time.onClick {
            TimeDialog { hr, min ->
                time.setText("$hr:$min")

            }.show(this@EditTimeActivity.supportFragmentManager, "timePicker")
        }

        next_btn.onClick {
            if (Util.checkError(date, this@EditTimeActivity, getString(R.string.error_date_format))
                && Util.checkError(time, this@EditTimeActivity, getString(R.string.error_time_format))
            ){
                sendRequestEdit(order.id,date.text.toString(),time.text.toString(),notes.text.toString())
            }
        }
    }

    private fun sendRequestEdit(id: Int?, date: String, time: String, notes: String?) {
        addDisposable(repo.updateClientOrder(id.toString(),date,time,notes,mPrefs!!.token!!)
            .subscribeWithLoading({
                showProgressDialog()
            },{
                if(it.value=="1"){
                    toasty(it.msg!!)
                }
                else{
                    toasty(it.msg!!,2)
                }
            },{
                hideProgressDialog()
                toasty(it.message!!,2)
            },{
                hideProgressDialog()
                onBackPressed()
            })
        )
    }

    private fun setData() {
        from_loc.text=order.startAddress
        to_loc.text=order.endAddress

    }
    private fun drawSrcDist() {
        addDisposable(repo.getRountes(
            "${order.startLat},${order.startLong}",
            "${order.endLat},${order.endLong}"
            ,getString(R.string.google_maps_key)
        ).subscribeWithLoading({

        },{
            val routes = it.routes
            val latLngList = getDirectionPolylines(routes)
            drawRouteOnMap(mMap!!, latLngList)


        },{
            it.message?.let { it1 -> Log.e("error", it1) }
        },{
            showRevealItem()
        }))
    }

    @SuppressLint("NewApi")
    private fun showRevealItem() {
        var y=later_lay.height/2
        var x=later_lay.width/2
        val anim= ViewAnimationUtils.createCircularReveal(later_lay,x,y,0F,Math.hypot(x.toDouble(),y.toDouble()).toFloat())
        later_lay.visibility= View.VISIBLE
        anim.start()
    }

    private fun drawRouteOnMap(map: GoogleMap, positions: List<LatLng>) {
        val options = PolylineOptions().width(5f).color(ContextCompat.getColor(applicationContext,R.color.colorAccent)).geodesic(true)
        options.addAll(positions)
        map.addPolyline(options)
        //marker of my location
        val markerOptions = MarkerOptions()
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_green_circle))
        val latLng = LatLng(order.startLat!!, order.startLong!!)
        markerOptions.position(latLng)
        mMap!!.addMarker(markerOptions)
        //marker of target Location
        val markerOptions2 = MarkerOptions()
        markerOptions2.icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_two))
        val latLng2 = LatLng(order.endLat!!, order.endLong!!)
        markerOptions2.position(latLng2)
        mMap!!.addMarker(markerOptions2)
        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(positions[positions.lastIndex].latitude, positions[positions.lastIndex].longitude))
            .zoom(Constant.ZOOM)
            .build()
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
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


}

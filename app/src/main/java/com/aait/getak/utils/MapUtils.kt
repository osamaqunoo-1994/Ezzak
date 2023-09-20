package com.aait.getak.utils

import android.animation.ValueAnimator
import android.content.Context
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnStart
import androidx.core.content.ContextCompat
import com.aait.getak.R
import com.aait.getak.models.route_model.Routes
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import java.util.*
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin


object MapUtils {

    private var isMarkerRotating=false

    fun animateMarker(
        googleMap: GoogleMap, marker: Marker,toPosition: LatLng,
        hideMarker: Boolean,zoom:Float?=0f
    ) {
        val handler = Handler()
        val start = SystemClock.uptimeMillis()
        val proj = googleMap.projection
        val startPoint = proj.toScreenLocation(marker.position)
        val startLatLng = proj.fromScreenLocation(startPoint)
        val duration: Long = 500

        val interpolator = LinearInterpolator()

        handler.post(object : Runnable {
            override fun run() {
                val elapsed = SystemClock.uptimeMillis() - start
                val t = interpolator.getInterpolation(elapsed.toFloat() / duration)
                val lng = t * toPosition.longitude + (1 - t) * startLatLng.longitude
                val lat = t * toPosition.latitude + (1 - t) * startLatLng.latitude
                marker.position = LatLng(lat, lng)
                val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
                if (zoom!=0f){
                valueAnimator.duration = 2000 // duration 3 second
                valueAnimator.interpolator = LinearInterpolator()
                valueAnimator.addUpdateListener { animation ->
                        googleMap.moveCamera(
                            CameraUpdateFactory.newCameraPosition(
                                CameraPosition.Builder()
                                    .target(LatLng(marker.position.latitude,marker.position.longitude))
                                    .zoom(zoom!!)
                                    .build()
                            )
                        )
                }
                valueAnimator.start()
            }
                else{
                    valueAnimator.duration = 2000 // duration 3 second
                    valueAnimator.interpolator = LinearInterpolator()
                    valueAnimator.start()
                    valueAnimator.doOnStart {
                        marker.remove()
                    }
                }
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16)
                } else {
                    marker.isVisible = !hideMarker
                }
            }
        })
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





     fun getDirectionPolylines(routes: MutableList<Routes>): List<LatLng> {
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



    fun bearingBetweenLocations(latLng1: LatLng, latLng2: LatLng): Float {
        val PI = 3.14159
        val lat1 = latLng1.latitude * PI / 180
        val long1 = latLng1.longitude * PI / 180
        val lat2 = latLng2.latitude * PI / 180
        val long2 = latLng2.longitude * PI / 180

        val dLon = long2 - long1

        val y = sin(dLon) * cos(lat2)
        val x = cos(lat1) * sin(lat2) - (sin(lat1)
                * cos(lat2) * cos(dLon))

        var brng = atan2(y, x)

        brng = Math.toDegrees(brng)
        brng = (brng + 360) % 360
        return brng.toFloat()
    }

     fun rotateMarker(marker: Marker, toRotation: Float) {
        if (!isMarkerRotating) {
            val handler = Handler()
            val start = SystemClock.uptimeMillis()
            val startRotation = marker.rotation
            val duration: Long = 1000

            val interpolator = LinearInterpolator()

            handler.post(object : Runnable {
                override fun run() {
                    isMarkerRotating = true
                    val elapsed = SystemClock.uptimeMillis() - start
                    val t = interpolator.getInterpolation(elapsed.toFloat() / duration)
                    val rot = t * toRotation + (1 - t) * startRotation
                    marker.rotation = if (-rot > 180) rot / 2 else rot
                    Log.e("sig",rot.toString())
                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16)
                    } else {
                        isMarkerRotating = false
                    }
                }
            })
        }
         else{
            Log.e("marker","no rotate")
        }
    }

     fun drawRouteOnMap(context: Context,map: GoogleMap, positions: List<LatLng>) {
        val options = PolylineOptions().width(5f).color(
            ContextCompat.getColor(context,
                R.color.colorAccent)).geodesic(true)
        options.addAll(positions)
        val polyline = map.addPolyline(options)
        //marker of my location
        drawMarkers(map,positions[0].latitude,positions[0].longitude,positions.last().latitude,positions.last().longitude)
    }


    private fun drawMarkers(mMap: GoogleMap,from_lat:Double,from_lng:Double,to_lat:Double,to_lng:Double) {
        val builder = LatLngBounds.Builder()
        val markerOptions = MarkerOptions()
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_green_circle))
        val latLng = LatLng(from_lat, from_lng)
        markerOptions.position(latLng)
        mMap.addMarker(markerOptions)
        //marker of target Location
        if (to_lat>0.0 && to_lng>0.0) {
            val markerOptions2 = MarkerOptions()
            markerOptions2.icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_two))
            val latLng2 = LatLng(to_lat, to_lng)
            markerOptions2.position(latLng2)
            mMap.addMarker(markerOptions2)
            builder.include(markerOptions2.position)
        }

        builder.include(markerOptions.position)

        val bounds = builder.build()

        val padding = 50 // offset from edges of the map in pixels
        val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)
        mMap.animateCamera(cu)


/*
        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(to_lat, to_lng))
            .zoom(Constant.ZOOM)
            .build()
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
*/
    }



}

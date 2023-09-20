package com.aait.getak.ui.activities.map

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Resources
import android.util.Log
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.aait.getak.R
import com.aait.getak.base.ParentActivity
import com.aait.getak.models.wholde_places_model.Place
import com.aait.getak.models.wholde_places_model.SavedPlace
import com.aait.getak.utils.toasty
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.jakewharton.rxbinding2.widget.RxTextView
import kotlinx.android.synthetic.main.activity_address_details.*
import kotlinx.android.synthetic.main.toolbar_normal.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class AddressDetailsActivity : ParentActivity(), OnMapReadyCallback {
    private var infav: Boolean?=false
    private var placeId: String?=""
    private lateinit var savedPlace: SavedPlace
    private lateinit var place: Place
    private var mMap: GoogleMap?=null
    private var lat=""
    private var lng=""
    private var name:String? = null

    override fun onMapReady(gMap: GoogleMap?) {
        mMap = gMap
        setStyle()
        if (intent.getBooleanExtra("isNearest",false)) {
            place = intent.getSerializableExtra("item") as Place
            placeId = place.placeId
            header.text=place.name
            address.text=place.vicinity
            infav = place.infav
            if (infav!!){
                etNamePlace.setText(place.name)
                btn_save.text=getString(R.string.unsave_remove)
            }
            addMarker(place.lat!!, place.lng!!)
        }
        else if (intent.getBooleanExtra("from_home",false)){
            lat=intent.getDoubleExtra("lat",0.0).toString()
            lng=intent.getDoubleExtra("lng",0.0).toString()
            address.text=intent.getStringExtra("address")
            header.text=intent.getStringExtra("name")
            addMarker(lat.toDouble(), lng.toDouble())
        }
        else{
            savedPlace = intent.getSerializableExtra("item") as SavedPlace
            header.text=savedPlace.name
            address.text=savedPlace.address
            placeId=savedPlace.placeId
            infav = savedPlace.infav
            if (infav!!){
                etNamePlace.setText(savedPlace.name)
                btn_save.text=getString(R.string.unsave_remove)
            }
            addMarker(savedPlace.lat!!, savedPlace.long!!)
        }
    }

    override val layoutResource: Int
        get() = R.layout.activity_address_details

    override fun init() {
        map.onCreate(savedInstanceState)
        map.onResume()
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as (SupportMapFragment)
        mapFragment.getMapAsync(this)
        try {
            MapsInitializer.initialize(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        setHeader()

        if (!checkValiation(etNamePlace)){
        btn_save.onClick {
            if (intent.getBooleanExtra("from_home", false)) {
                repo.savePlace(
                    lat,
                    lng,
                    etNamePlace.text.toString(),
                    address.text.toString(),
                    null,
                    mPrefs!!.token!!
                )
                    .subscribeWithLoading({
                        showProgressDialog()
                    }, {
                        hideProgressDialog()
                        if (it.value == "1") {
                            it.msg?.let { it1 ->
                                toasty(it1)
                                setResult(Activity.RESULT_OK)
                            }
                            onBackPressed()
                        } else {
                            it.msg?.let { it1 -> toasty(it1, 2) }
                        }
                    }
                        , {
                            hideProgressDialog()
                            toasty(it.message!!, 2)
                        }
                        , {

                        }
                    )
            } else {
                repo.savePlace(
                    lat,
                    lng,
                    etNamePlace.text.toString(),
                    address.text.toString(),
                    placeId!!,
                    mPrefs!!.token!!
                )
                    .subscribeWithLoading({
                        showProgressDialog()
                    }, {
                        hideProgressDialog()
                        if (it.value == "1") {
                            it.msg?.let { it1 -> toasty(it1) }
                            onBackPressed()
                        } else {
                            it.msg?.let { it1 -> toasty(it1, 2) }
                        }
                    }
                        , {
                            hideProgressDialog()
                            toasty(it.message!!, 2)
                        }
                        , {

                        }
                    )

            }
        }
        }
    }

    private fun setStyle() {
        try {
            val success = mMap!!.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.map_style))

            if (!success) {
                Log.e("success", "Style parsing failed.");
            }
        } catch (e: Resources.NotFoundException ) {
            Log.e("error", "Can't find style. Error: ", e);
        }
    }

    @SuppressLint("CheckResult")
    fun checkValiation(editText: EditText): Boolean {
        var isValid=false
        RxTextView.textChanges(editText)
            .map { inputText -> inputText.trim().isNotBlank()}
            .distinctUntilChanged()
            .subscribe {
                if (!it){
                    btn_save.isEnabled=false
                    btn_save.backgroundTintList= ContextCompat.getColorStateList(applicationContext, android.R.color.darker_gray)
                }
                else {
                    btn_save.isEnabled = true
                    btn_save.backgroundTintList =  ContextCompat.getColorStateList(applicationContext, R.color.colorAccent)


                }
                isValid=it
            }
        return isValid
    }

    private fun setHeader() {
        title_toolbar.text=getString(R.string.place_details)
        iv_back.onClick {
            onBackPressed()
        }
    }

    private fun addMarker(lat: Double, lng: Double) {
        this.lat = lat.toString() + ""
        this.lng = lng.toString() + ""
        val markerOptions = MarkerOptions()
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_green_circle))
        val latLng = LatLng(lat, lng)
        markerOptions.position(latLng)
        markerOptions.draggable(false)
        mMap!!.addMarker(markerOptions)
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f))
    }




}

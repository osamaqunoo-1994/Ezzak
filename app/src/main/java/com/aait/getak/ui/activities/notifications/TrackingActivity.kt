package com.aait.getak.ui.activities.notifications
import android.util.Log
import com.aait.getak.R
import com.aait.getak.base.ParentActivity
import com.aait.getak.utils.toasty
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.activity_tracking.*

class TrackingActivity : ParentActivity(), OnMapReadyCallback {
    private var mMap: GoogleMap? = null
    private var end_long: Double?=0.0
    private var end_lat: Double?=0.0
    private var end_address: String?=""
    private var start_long: Double?=0.0
    private var start_lat: Double?=0.0
    private var start_address: String?=""

    override fun onMapReady(gMap: GoogleMap?) {
        mMap=gMap
    }


    override val layoutResource: Int
        get() = R.layout.activity_tracking

    override fun init() {
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        setToolbar(getString(R.string.my_trips))
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapView) as (SupportMapFragment)
        mapFragment.getMapAsync(this)
        try {
            MapsInitializer.initialize(this)
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        setToolbar(getString(R.string.tracking_ride))
        sendRequestDetails(intent.getIntExtra("order_id",0))
    }

    private fun sendRequestDetails(orderId: Int) {
        addDisposable(repo.rideDetails(orderId,mPrefs!!.token!!).subscribeWithLoading({
        },{
            hideProgressDialog()
            if (it.value=="1"){
                    start_lat=it.data!!.start_lat
                    start_long=it.data!!.start_long
                    start_address=it.data!!.start_address
                    end_lat=it.data!!.end_lat
                    end_long=it.data!!.end_long
                    end_address=it.data!!.end_address

            }
            else{
                toasty(getString(R.string.error_connection),2)
            }
        },{
            hideProgressDialog()
            Log.e("error",it.message!!)
            toasty(getString(R.string.error_connection),2)
        },{
            hideProgressDialog()
        }))
    }




}

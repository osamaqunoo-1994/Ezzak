package com.aait.getak.ui.activities.map

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.aait.getak.R
import com.aait.getak.base.ParentActivity
import com.aait.getak.ui.adapters.MostOrdersAdapter
import com.aait.getak.ui.adapters.NearestPlacesAdapter
import com.aait.getak.ui.adapters.SavedAdapter
import com.aait.getak.ui.dialogs.DataDialog
import com.aait.getak.ui.fragments.bottom_nav.HomeFragment
import com.aait.getak.utils.Util.onPrintLog
import com.aait.getak.utils.toasty
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import kotlinx.android.synthetic.main.activity_search_places.*


class SearchPlacesActivity : ParentActivity() {
    companion object {
        val RESULT_SEARCH: Int = 22
    }

    private var lat = 0.toDouble()
    private var lng = 0.toDouble()

    private var mNearesAdapter = NearestPlacesAdapter({ pos, item, img ->
        DataDialog {
            img.setImageResource(R.mipmap.heart)
            removeItem(pos, 1)
            sendRequestUnsave(item.lat, item.lng, item.placeId, item.name, item.vicinity)
        }.also {
            it.show(supportFragmentManager, "asd")
        }


    }, { lat, lng, header, address ->
        intent.putExtra("lat", lat)
        intent.putExtra("lng", lng)
        intent.putExtra("address", address)
        intent.putExtra("name", header)
        setResult(RESULT_SEARCH, intent)
        finish()

    })
    var mSavedAdapter = SavedAdapter({ pos, item, img ->
        DataDialog {
            img.setImageResource(R.mipmap.heart)
            removeItem(pos, 2)
            sendRequestUnsave(item.lat, item.long, item.placeId, item.name, item.address)
        }.also {
            it.show(supportFragmentManager, "asd")
        }
    }, { lat, lng, name, address ->
        intent.putExtra("lat", lat)
        intent.putExtra("lng", lng)
        intent.putExtra("address", address)
        intent.putExtra("name", name)
        setResult(RESULT_SEARCH, intent)
        finish()

    })

    private fun removeItem(pos: Int, numb: Int) {
        when (numb) {
            1 -> mNearesAdapter.removeItem(pos)

            2 -> mSavedAdapter.removeItem(pos)

            else -> mSearchAdapter.removeItem(pos)
        }
    }


    var mSearchAdapter = NearestPlacesAdapter({ pos, item, img ->
        DataDialog {
            img.setImageResource(R.mipmap.heart)
            removeItem(pos, 3)
            sendRequestUnsave(item.lat, item.lng, item.placeId, item.name, item.vicinity)
        }.also {
            it.show(supportFragmentManager, "asd")
        }


    }, { lat, lng, name, address ->
        intent.putExtra("lat", lat)
        intent.putExtra("lng", lng)
        intent.putExtra("address", address)
        intent.putExtra("name", name)
        setResult(RESULT_SEARCH, intent)
        finish()

    })

    var mAdapter = MostOrdersAdapter({ pos, item, img ->
        DataDialog {
//            img.setImageResource(R.mipmap.heart)
//            removeItem(pos, 3)
//            sendRequestUnsave(item.lat,item.long,item.placeId,item.name,item.vicinity)
        }.also {
            it.show(supportFragmentManager, "asd")
        }


    }, { lat, lng, name, address ->
        intent.putExtra("lat", lat)
        intent.putExtra("lng", lng)
        intent.putExtra("address", address)
        intent.putExtra("name", name)
        setResult(RESULT_SEARCH, intent)
        finish()

    })

    private fun sendRequestUnsave(
        lat: Double?,
        lng: Double?,
        placeId: String?,
        name: String?,
        address: String?
    ) {

        repo.savePlace(lat.toString(), lng.toString(), name, address!!, placeId!!, mPrefs!!.token!!)
            .subscribeWithLoading({

            }, {
                if (it.value != "1") {
                    toasty(it.msg!!, 2)
                }
            }, {
                toasty(getString(R.string.error_connection), 2)
                it.message?.let { it1 -> Log.e("error", it1) }
            }, {

            })
    }


    override val layoutResource: Int
        get() = R.layout.activity_search_places

    override fun onResume() {
        super.onResume()
        sendRequest()
    }

    override fun init() {
        intent.getBooleanExtra("from", false)
        lat = intent.getDoubleExtra("lat", 0.toDouble())
        lng = intent.getDoubleExtra("lng", 0.toDouble())
        setAutoComplete()
        val from = intent.getBooleanExtra("from", false)
        setRecyclers()
        if (!from) {
            rec_nears.visibility = View.GONE
            near_by_txt.visibility = View.GONE
        }


    }


    private fun setAutoComplete() {
        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.google_maps_key))
        }

        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?

        val radiusDegrees = 0.23

        val center = LatLng(
            lat,
            lng
        )

        val northEast = LatLng(center.latitude + radiusDegrees, center.longitude + radiusDegrees)
        val southWest = LatLng(center.latitude - radiusDegrees, center.longitude - radiusDegrees)
        if (HomeFragment.serviceIn.equals("mycity", true)) {
            /*val serviceIn = HomeFragment.serviceIn
            Log.e("service",serviceIn)
            autocompleteFragment!!.setLocationRestriction(
                RectangularBounds.newInstance(
                    southWest,
                    northEast
                )
            )*/
            val serviceIn = HomeFragment.serviceIn
        } else {
            val serviceIn = HomeFragment.serviceIn
            Log.e("service", serviceIn)
        }

// Specify the types of place data to return.
        autocompleteFragment?.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG
            )
        )


// Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment?.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                intent.putExtra("lat", place.latLng?.latitude)
                intent.putExtra("lng", place.latLng?.longitude)
                intent.putExtra("address", place.address)
                intent.putExtra("name", place.name)
                setResult(RESULT_SEARCH, intent)
                finish()
            }

            override fun onError(status: Status) {
                Log.e("places", "An error occurred: $status")
            }
        })


    }

    private fun setSearch() {
        /*RxTextView.textChanges(search)
            .skip(1)
            .map {
                if (it.isBlank()) {
                    sendRequest()
                } else {
                    performSearch(it.toString())
                }
            }
            .debounce(800, TimeUnit.MILLISECONDS)
            .subscribe()*/

    }


    private fun setRecyclers() {
        rec_nears.layoutManager = LinearLayoutManager(this)
        rec_saved.layoutManager = LinearLayoutManager(this)
        rec_search_results.layoutManager = LinearLayoutManager(this)

        rec_nears.adapter = mNearesAdapter
        rec_saved.adapter = mSavedAdapter
        rv_most_order.adapter = mAdapter
        rec_search_results.adapter = mSearchAdapter

    }

    private fun sendRequest() {
        repo.getPlaces(lat.toString(), lng.toString(), mPrefs!!.token!!)
            .subscribeWithLoading({
                onStartLoad()
            }, {
                if (it.value == "1") {
                    val mostPlaces = it.wholePlaces!!.end_addresses +
                            it.wholePlaces!!.start_addresses

                    if (it.wholePlaces!!.nearestPlaces.isEmpty()) {
                        near_by_txt.visibility = View.GONE
                        rec_nears.visibility = View.GONE
                    }
                    if (it.wholePlaces!!.savedPlaces.isEmpty()) {
                        saved_txt.visibility = View.GONE
                        rec_saved.visibility = View.GONE
                    }

                    if (mostPlaces.isNotEmpty()) {
                        tv_most_order.visibility = View.VISIBLE
                        rv_most_order.visibility = View.VISIBLE
                    }
                    onPrintLog(mostPlaces)
                    val nearestPlaces = it.wholePlaces!!.nearestPlaces
                    mAdapter.swapData(mostPlaces)
                    rv_most_order.adapter = mAdapter
                    mNearesAdapter.swapData(nearestPlaces)
                    mSavedAdapter.swapData(it.wholePlaces!!.savedPlaces)


                } else {
                    toasty(it.msg!!, 2)
                }
            }, {
                toasty(getString(R.string.error_connection), 2)
                it.message?.let { it1 -> Log.e("Error", it1) }
                onStopLoad()
            }, {
                onStopLoad()
            })
    }

    private fun onStartLoad() {
        progress_wheel.visibility = View.VISIBLE
        search_lay.visibility = View.VISIBLE
        place_holder.visibility = View.GONE
        rec_search_results.visibility = View.GONE

    }

    private fun onStopLoad() {
        progress_wheel.visibility = View.VISIBLE
        search_lay.visibility = View.GONE
        rec_search_results.visibility = View.GONE

    }

    override fun onBackPressed() {

        super.onBackPressed()
    }
}

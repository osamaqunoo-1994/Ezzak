package com.aait.getak.ui.activities.food

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aait.getak.R
import com.aait.getak.base.ParentActivity
import com.aait.getak.ui.adapters.StoresAdapter
import com.aait.getak.utils.Util
import com.aait.getak.utils.toasty
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.processors.PublishProcessor
import kotlinx.android.synthetic.main.activity_restaurants.*
import kotlinx.android.synthetic.main.activity_search_places.rec_nears
import java.util.concurrent.TimeUnit

class RestaurantsActivity : ParentActivity() {
   companion object MyLocation{
         var userLong: Double = 0.0
         var userLat: Double = 0.0
    }

    override val layoutResource: Int
        get() = R.layout.activity_restaurants

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var linearManger: LinearLayoutManager
    var loading = MutableLiveData<Boolean>()
    private var isFirst=true
    private var nextPageToken=""
    private var pageNumber = 1
    private val VISIBLE_THRESHOLD = 2
    var paginator= PublishProcessor.create<String>()


    private var data = mutableListOf<com.aait.getak.models.Place>()

    private val mAdapter= StoresAdapter()


    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }


    @SuppressLint("MissingPermission")
    private fun setLocationChange() {
        setPermissionsLocation {
            if (it) {
                fusedLocationClient.lastLocation.addOnSuccessListener {location->
                    if (location!=null) {
                        userLat = location.latitude
                        userLong = location.longitude
                        if (isFirst) {
                            sendRequest()
                            isFirst = false
                        }
                    }
                }
            }
        }
    }

    private fun initRec() {
        linearManger= LinearLayoutManager(this)
        rec_nears.layoutManager=linearManger
        rec_nears.adapter=mAdapter
    }

    private fun setUpLoadMoreListener() {
        rec_nears.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = linearManger.itemCount
                val lastVisibleItem = linearManger
                    .findLastVisibleItemPosition()
                Log.e("total",totalItemCount.toString()+","+(lastVisibleItem+VISIBLE_THRESHOLD).toString())
                if (!loading.value!! && dy>0 && totalItemCount <= lastVisibleItem + VISIBLE_THRESHOLD) {
                    if (nextPageToken.isEmpty()) {
                        pageNumber++
                        paginator.onNext(pageNumber.toString())
                    }
                    else{
                        paginator.onNext(nextPageToken)
                    }
                    loading.value = true
                }

            }
        })
        loading.observe(this, Observer {
            if (it) {
                load_progress.visibility = View.VISIBLE
            }
            else{
                load_progress.visibility = View.INVISIBLE
            }
        })
    }



    override fun init() {
        startLocationUpdates()
        setLocationChange()
        initRec()
        setUpLoadMoreListener()
        setSearch()
    }

    @SuppressLint("CheckResult")
    private fun setSearch() {
        RxTextView.textChanges(et_search)
            .skip(1)
            .debounce(800, TimeUnit.MILLISECONDS)
            .subscribeOn(io.reactivex.schedulers.Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{
                if (it.isNotBlank()) {
                    pageNumber=1
                    nextPageToken=""
                    performSearch(it.toString())
                }
                else{
                    paginator = PublishProcessor.create()
                    pageNumber=1
                    nextPageToken=""
                    data.clear()
                    sendRequest()
                }

            }
    }

    private fun performSearch(key: String) {
        if (Util.isNetworkAvailable(this)) {
            paginator = PublishProcessor.create()
            val disposable = paginator
                .onBackpressureDrop()
                .concatMap { pageNumber ->
                    return@concatMap repo.searchPlaces(
                        userLat.toString(),
                        userLong.toString(),
                        key,
                        pageNumber.toString(),
                        mPrefs!!.token!!
                    )
                        .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())

                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    data.clear()
                    progress_search.visibility = View.VISIBLE
                    mAdapter.notifyDataSetChanged()
                }
                .doOnNext {
                    loading.value = true
                }
                .doOnError {
                    progress_search.visibility = View.GONE
                }
                .subscribe { resp ->
                    progress_search.visibility = View.GONE
                    if (resp.value == "1") {
                        nextPageToken=resp.data.next_page_token ?: ""
                        if (resp.data.places!!.isNotEmpty()) {
                            progress_lay.visibility = View.GONE
                            empty_lay.visibility = View.GONE
                            error_lay.visibility = View.GONE
                            data.addAll(resp.data.specialPlaces!!)
                            data.addAll(resp.data.places)
                            mAdapter.swapData(data)
                            loading.postValue(false)
                        }
                        else {
                            loading.postValue(false)
                            if (data.isEmpty()) {
                                error_lay.visibility = View.GONE
                                progress_lay.visibility = View.GONE
                                empty_lay.visibility = View.VISIBLE
                            }
                        }
                    }
                    else {
                        loading.postValue(false)
                    }

                }

            addDisposable(disposable)
            if (nextPageToken.isNotBlank())paginator.onNext(nextPageToken) else paginator.onNext(pageNumber.toString())
        }
        else{
            toasty(getString(R.string.error_connection),2)
        }
    }

    private fun sendRequest() {
        if (Util.isNetworkAvailable(this)) {
            val disposable = paginator
                .onBackpressureDrop()
                .concatMap { pageNumber ->
                    return@concatMap repo.nearStores(
                        userLat.toString(),
                        userLong.toString(),
                        pageNumber,
                        mPrefs!!.token!!
                    )
                        .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())

                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    progress_lay.visibility = View.VISIBLE
                    empty_lay.visibility = View.GONE
                    error_lay.visibility = View.GONE
                }
                .doOnNext {
                    loading.value = true
                }
                .doOnError {
//                Log.e("error",it.message)
                    progress_lay.visibility = View.GONE
                    empty_lay.visibility = View.GONE
                    error_lay.visibility = View.VISIBLE

                }
                .subscribe { resp ->
                    if (resp.value == "1") {
                        nextPageToken=resp.data.next_page_token ?: ""
                        if (resp.data.places!!.isNotEmpty()) {
                            progress_lay.visibility = View.GONE
                            empty_lay.visibility = View.GONE
                            error_lay.visibility = View.GONE
                            data.addAll(resp.data.specialPlaces!!)
                            data.addAll(resp.data.places)
                            mAdapter.swapData(data)
                            loading.postValue(false)
                        } else {
                            loading.postValue(false)
                            if (data.isEmpty()) {
                                error_lay.visibility = View.GONE
                                progress_lay.visibility = View.GONE
                                empty_lay.visibility = View.VISIBLE
                            }
                        }
                    } else {
                        error_lay.visibility = View.VISIBLE
                        progress_lay.visibility = View.GONE
                        empty_lay.visibility = View.GONE
                        loading.postValue(false)
                    }

                }

            addDisposable(disposable)
            if (nextPageToken.isNotBlank())paginator.onNext(nextPageToken) else paginator.onNext(pageNumber.toString())
        }
        else{
            toasty(getString(R.string.error_connection),2)
        }

    }
}
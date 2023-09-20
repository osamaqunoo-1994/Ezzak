/*
package com.aait.getak.ui.fragments.bottom_nav

import android.os.Bundle
import android.util.Log
import android.util.MutableBoolean
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aait.getak.R
import com.aait.getak.base.BaseFragment
import com.aait.getak.models.notification_model.Data
import com.aait.getak.models.notification_model.NotificationModel
import com.aait.getak.network.remote_db.RetroWeb
import com.aait.getak.ui.activities.core.MainActivity
import com.aait.getak.ui.adapters.NotificationAdapter
import com.aait.getak.utils.toasty
import com.google.gson.JsonParseException
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.pager_recycler.*
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast

import retrofit2.HttpException
import retrofit2.Response

class NotificationsFragment :BaseFragment(){
    private val compositeDisposable = CompositeDisposable()
    private val paginator = PublishProcessor.create<Int>()
    var loading = MutableLiveData<Boolean>()
    private var pageNumber = 1
    private val VISIBLE_THRESHOLD = 1

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: NotificationAdapter
    private  var lst: MutableList<Data> = mutableListOf()
    private val data:MutableList<Data> = mutableListOf()
    override val viewId: Int
        get() = R.layout.pager_recycler

    override fun init(view: View) {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        MainActivity.notifNum.value="0"
        adapter = NotificationAdapter(activity!!){id,pos->
            sendRequestDelete(id,pos)
        }

        layoutManager = LinearLayoutManager(activity)
        main_recycler.layoutManager=layoutManager
        main_recycler.adapter=adapter
        subscribeForData()
        setUpLoadMoreListener()
        onSwipe {
        //    subscribeForData()
            mSwipe?.isEnabled=false
        }


    }

    private fun sendRequestDelete(id: Int, pos: Int) {
        adapter.onDeleteUi(pos)
        addDisposable(repo.deleteNotif(id,mPrefs.token!!).
            subscribeWithLoading({
                hideProgressDialog()
            if (it.value=="1"){
                    
            }
            else{
                activity!!.toasty(it.msg!!,2)
            }
        },{
            Log.e("error",it.message)
        }
        ))
    }

    private fun setUpLoadMoreListener() {
        main_recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager
                    .findLastVisibleItemPosition()
                Log.e("total",totalItemCount.toString()+","+(lastVisibleItem+VISIBLE_THRESHOLD).toString())
                if (!loading.value!! && totalItemCount <= lastVisibleItem + VISIBLE_THRESHOLD) {
                    pageNumber++
                    paginator.onNext(pageNumber)
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

    private fun subscribeForData() {
        val disposable = paginator
            .onBackpressureDrop()
            .concatMap { pageNumber -> getNotifications(pageNumber) }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe{
                show_progress()
            }
            .doOnNext {
                loading.value = true
            }
            .doOnError { throwable ->
                show_error()
                if (throwable is Throwable) {
                    val response = (throwable as HttpException).response()
                } else if (throwable is JsonParseException) {
                } else {
                    try {
                        Log.e("ezzz", "asd")
                    } catch (ex:Exception) {
                        Log.e("ezzz", ex.message)
                    }
                }
            }.subscribe {
                    resp ->
                if (resp.isSuccessful) {
                    if (resp.body()!!.value=="1") {
                        show_success_results()
                        if (resp.body()!!.data.isNotEmpty()) {
                            lst = resp.body()!!.data as MutableList<Data>
                                data.addAll(resp.body()!!.data)
                                adapter.swapData(data)
                                adapter.notifyDataSetChanged()
                                loading.postValue(false)

                        }
                        else{
                            loading.postValue(false)
                            if (data.isEmpty()){
                                show_empty()
                            }
                        }
                    } else {
                        show_error()
                        toast(resp.body()!!.msg)
                    }

                }
            }
            compositeDisposable.add(disposable)
            paginator.onNext(pageNumber)

    }

    private fun getNotifications(pageNumber:Int): Flowable<Response<NotificationModel>> {
        return RetroWeb.serviceApi.notifications(pageNumber,mPrefs.token!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}*/

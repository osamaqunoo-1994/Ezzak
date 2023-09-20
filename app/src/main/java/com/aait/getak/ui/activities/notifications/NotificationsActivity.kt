package com.aait.getak.ui.activities.notifications

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aait.getak.R
import com.aait.getak.base.ParentActivity
import com.aait.getak.models.notifications_api_model.Notification
import com.aait.getak.models.notifications_api_model.SystemModel
import com.aait.getak.network.remote_db.RetroWeb
import com.aait.getak.ui.adapters.NotificationsAdapter
import com.aait.getak.utils.Util
import com.aait.getak.utils.toasty
import com.google.gson.JsonParseException
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_notifications.*
import kotlinx.android.synthetic.main.toolbar_normal.*
import retrofit2.Response

class NotificationsActivity: ParentActivity() {
    override val layoutResource: Int
        get() = R.layout.activity_notifications

    private val compositeDisposable = CompositeDisposable()
    private var paginator = PublishProcessor.create<Int>()
    var loading = MutableLiveData<Boolean>()
    private var pageNumber = 1
    private val VISIBLE_THRESHOLD = 1

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: NotificationsAdapter
    private  var lst: MutableList<Notification> = mutableListOf()
    private val data:MutableList<Notification> = mutableListOf()

    override fun init() {
        setToolbar(getString(R.string.notifications))

        adapter =
            NotificationsAdapter({ pos, item ->
                adapter.removeItem(pos, item)
                sendDeleteRequest(item)
            }) {

            }
        iv_back.setOnClickListener {
            onBackPressed()
        }
        layoutManager = LinearLayoutManager(this)
        main_recycler.layoutManager=layoutManager
        main_recycler.adapter=adapter
        subscribeForData()
        setUpLoadMoreListener()

        swipe?.setOnRefreshListener {
            //swipe.isRefreshing=false
            pageNumber=1
            paginator = PublishProcessor.create()
            data.clear()
            lst.clear()
            swipe.isNestedScrollingEnabled=false
            swipe.isRefreshing=false
            subscribeForData()
        }
    }

    @SuppressLint("CheckResult")
    private fun sendDeleteRequest(item: Notification) {
        RetroWeb.serviceApi.delete_notif(item.id!!,(mPrefs?.token!!))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.value=="1"){
                }
                else{
                    toasty(it.msg!!,2)
                }
            }
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
                showProgress()
            }
            .doOnNext {
                loading.value = true
            }
            .doOnError { throwable ->
                showError()
                if (throwable is Throwable) {
                    throwable.message?.let { Log.e("exc", it) }
                } else if (throwable is JsonParseException) {
                } else {
                    try { } catch (ex:Exception) {
                        ex.message?.let { Log.e("ezzz", it) }
                    }
                }
            }.subscribe {
                    resp ->
                if (resp.isSuccessful) {
                    if (resp.body()!!.value=="1") {
                        show_successResults()
                        if (resp.body()!!.data!!.isNotEmpty()) {
                            lst = resp.body()!!.data as MutableList<Notification>
                            data.addAll(resp.body()!!.data!!)
                            adapter.swapData(data)
                            adapter.notifyDataSetChanged()
                            loading.postValue(false)

                        }
                        else{
                            loading.postValue(false)
                            if (data.isEmpty()){
                                showEmpty()
                            }
                        }
                    } else {
                        showError()
                    }

                }
            }
        compositeDisposable.add(disposable)
        paginator.onNext(pageNumber)

    }
    private  fun showProgress() {
        lay_no_internet.visibility= View.GONE
        lay_empty.visibility= View.GONE
        lay_progress.visibility= View.VISIBLE
        main_recycler.visibility= View.GONE
    }

    private fun show_successResults() {
        lay_no_internet.visibility= View.GONE
        lay_empty.visibility= View.GONE
        lay_progress.visibility= View.GONE
        main_recycler.visibility= View.VISIBLE
    }

    private fun showError() {
        lay_no_internet.visibility= View.VISIBLE
        lay_empty.visibility= View.GONE
        lay_progress.visibility= View.GONE
        main_recycler.visibility= View.GONE
    }
    internal fun showEmpty() {
        lay_no_internet.visibility= View.GONE
        lay_empty.visibility= View.VISIBLE
        lay_progress.visibility= View.GONE
        main_recycler.visibility= View.GONE
    }
    private fun getNotifications(pageNumber:Int): Flowable<Response<SystemModel>> {
        return RetroWeb.serviceApi.notifications(pageNumber, mPrefs?.token!!, Util.language)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}
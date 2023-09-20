package com.aait.getak.ui.fragments.trips

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.aait.getak.R
import com.aait.getak.base.BaseFragment
import com.aait.getak.ui.adapters.PrevAdapter
import com.aait.getak.utils.toasty
import kotlinx.android.synthetic.main.base_recycler.*

class NestedRecordFragment :BaseFragment(){
    private var isFuture: Boolean=false
    private lateinit var mAdapter: PrevAdapter
    private var year: String?=""
    private var month: String?=""
    override val viewId: Int
        get() = R.layout.nested_records



    override fun init(view: View) {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
         year = arguments!!.getString("year")
         month = arguments!!.getString("month")
        isFuture=arguments!!.getBoolean("isFuture")
        return super.onCreateView(inflater, container, savedInstanceState)

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recycler_base.layoutManager=LinearLayoutManager(activity)
        mAdapter = PrevAdapter()

        recycler_base.adapter=mAdapter
        noItemsMsg.text=getString(R.string.no_previous_trips)
        getTrips()
        mSwipe!!.isEnabled=false
        mSwipe!!.isRefreshing=false
    }

    private fun getTrips() {
        if (isFuture) {
            sendRequestSchduled()
        } else {
           sendRequestHistory()

        }
    }

    private fun sendRequestHistory() {
        addDisposable(
            repo.getPreviousTrips(mPrefs.token!!)
                .subscribeWithLoadRec({ resp ->
                    if (resp.value == "1") {
                        if (resp.data.isEmpty()) {
                            show_empty()
                        } else {
                            isLoading.observe(this, Observer {
                                if (it) {
                                    show_progress()
                                } else {
                                    Log.e("data", "success")
                                    mAdapter.swapData(resp.data)
                                    show_success_results()

                                }
                            })
                        }
                    } else {
                        activity!!.toasty(resp.msg!!, 2)
                        show_error()
                    }
                }, {
                    it.message?.let { it1 -> Log.e("error", it1) }
                    show_error()
                })
        )
    }

    private fun sendRequestSchduled() {
        addDisposable(
            repo.laterOrders(mPrefs.token!!)
                .subscribeWithLoadRec({ resp ->
                    if (resp.value == "1") {
                        if (resp.data.isEmpty()) {
                            show_empty()
                        } else {
                            isLoading.observe(this, Observer {
                                if (it) {
                                    show_progress()
                                } else {
                                    Log.e("data", "success")
                                    mAdapter.swapData(resp.data)
                                    show_success_results()

                                }
                            })
                        }
                    } else {
                        activity!!.toasty(resp.msg!!, 2)
                        show_error()
                    }
                }, {
                    it.message?.let { it1 -> Log.e("error", it1) }
                    show_error()
                })
        )
    }
}
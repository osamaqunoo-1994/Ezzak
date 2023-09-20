package com.aait.getak.ui.activities.notifications

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.util.Pair
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.aait.getak.R
import com.aait.getak.base.ParentActivity
import com.aait.getak.ui.adapters.NotificationAdapter
import com.aait.getak.utils.toasty
import kotlinx.android.synthetic.main.activity_notification_details.*
import kotlinx.android.synthetic.main.base_recycler.*

class OffersActivity : ParentActivity() {
    private lateinit var mAdapter: NotificationAdapter
    override val layoutResource: Int
        get() = R.layout.activity_offers

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun init() {
        setToolbar(getString(R.string.offers))
        setRecycler()
        sendRequest()
        onSwipe {
            sendRequest()
        }
    }

    private fun sendRequest() {
        repo.offers(mPrefs!!.token!!).subscribeWithLoading({
            show_progress()

        },{
            if (it.value=="1"){
                if (it.data.isEmpty()){
                    show_empty()
                }
                else{
                    show_success_results()
                    mAdapter.swapData(it.data)
                }
            }
            else{
             show_error()
            }
        },{
            toasty(getString(R.string.error_connection),2)
            show_error()
        },{

        })
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setRecycler() {
        recycler_base.layoutManager = LinearLayoutManager(this)
        mAdapter=NotificationAdapter(this,{
            id: Int, pos: Int ->
        },{
            val intent = Intent(applicationContext, NotificationDetailsActivity::class.java)
            intent.putExtra("trip",it)


            val options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                Pair.create(details, "img")

            )
            startActivity(intent,options.toBundle())
        })
        recycler_base.adapter=mAdapter
    }


}

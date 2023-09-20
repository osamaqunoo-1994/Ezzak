package com.aait.getak.ui.activities.notifications

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton
import com.aait.getak.R
import com.aait.getak.base.ParentActivity
import com.aait.getak.ui.activities.core.MapPreviewActivity
import com.aait.getak.ui.adapters.BidsAdapter
import com.aait.getak.utils.toasty
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import kotlinx.android.synthetic.main.activity_bids.*
import kotlinx.android.synthetic.main.empty_lay.*


class BidsActivity : ParentActivity() {
    private var orderId: Int=0
    private lateinit var mAdapter: BidsAdapter
    override val layoutResource: Int
        get() = R.layout.activity_bids





    override fun init() {
        setToolbar(getString(R.string.offers))
        orderId = intent.getIntExtra("order_id", 0)
        rec_bids.layoutManager=LinearLayoutManager(this)
        mAdapter=BidsAdapter{data,btn->
            sendRequestSelect(data.bidId,btn)
        }
        rec_bids.adapter=mAdapter
        sendRequest()

    }

    private fun sendRequestSelect(
        bidId: Int?,
        btn: CircularProgressButton
    ) {
        btn.showProgress()
        Handler().postDelayed({
            val bm = BitmapFactory.decodeResource(resources, R.drawable.done)
            btn.hideProgress(R.string.offer_accepted)
            btn.doneLoadingAnimation(ContextCompat.getColor(this,R.color.colorPrimary),bm)
        },3000)
        addDisposable(repo.selectBid(bidId!!,mPrefs!!.token!!).subscribeWithLoading({
            btn.showProgress()
        },{
            if (it.value=="1"){

            }
        },{
            //val bm = BitmapFactory.decodeResource(resources, R.drawable.)
            //btn.doneLoadingAnimation(ContextCompat.getColor(this,R.color.colorAccent),bm)
            btn.revertAnimation()
            //btn.hideProgress(R.string.offer_accepted)
        },{
            val bm = BitmapFactory.decodeResource(resources, R.drawable.done)
            btn.hideProgress(R.string.offer_accepted)
           goHome()
        }))
    }

    private fun goHome() {
        val intent = Intent(applicationContext, MapPreviewActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun sendRequest() {
        addDisposable(repo.bids(orderId,mPrefs!!.token!!).subscribeWithLoading({
            showProgressDialog()
        },{
            if (it.value=="1"){
                if (it.data?.bids?.isEmpty()!!){
                    empty_lay.visibility= View.VISIBLE
                    rec_bids.visibility=View.GONE
                }
                else{
                    empty_lay.visibility= View.GONE
                    rec_bids.visibility=View.VISIBLE
                    mAdapter.swapData(it.data?.bids!!)
                   /* from_loc.text=it.data!!.order!!.startAddress
                    to_loc.text=it.data!!.order!!.endAddress*/
                }
            }
            else{
                toasty(it.msg!!,2)
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

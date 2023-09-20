package com.aait.getak.ui.activities.food

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.aait.getak.R
import com.aait.getak.base.ParentActivity
import com.aait.getak.models.Place
import com.aait.getak.models.expected_distance_model.Data
import com.aait.getak.ui.activities.core.TrackingActivity
import com.aait.getak.ui.activities.map.MapActivity
import com.aait.getak.ui.fragments.bottom_nav.MyWallet
import com.aait.getak.ui.fragments.homeFragments.FirstFragment
import com.aait.getak.utils.Util
import com.aait.getak.utils.toasty
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_order.*
import kotlinx.android.synthetic.main.item_subcat_search_adapter.*
import kotlinx.android.synthetic.main.price_plan.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class OrderActivity : ParentActivity() {

    private lateinit var ridePayType: String
    private var mData: Data? = null

         var distLat: String?=""
         var distLng: String?=""
         var distLoc: String?=""



    private var place: Place?=null

    override val layoutResource: Int
        get() = R.layout.activity_order

    companion object{
        val REQUEST_CODE=4000
    }

    override fun init() {
        place = intent.getSerializableExtra("restaurant") as Place
        tv_name.text=place?.name
        tv_address.text=place?.vicinity
        tv_dist.text=place?.distance
        Picasso.get().load(place?.icon).into(iv_item)
        setPaymentType()
        setStoredPayment()
        setChangePayment()
        setActions()
    }

    private fun setStoredPayment() {
        when (mPrefs!!.selectedPayment) {
            FirstFragment.PaymentType.visa -> {
                payment_type_iv.setImageResource(R.mipmap.visa)
                payment_type_tv.text=getString(R.string.elect_payment)

            }
            FirstFragment.PaymentType.stc -> {
                payment_type_iv.setImageResource(R.mipmap.stcpay)
                payment_type_tv.text=getString(R.string.stc_payment)
            }
            else -> {
                payment_type_iv.setImageResource(R.mipmap.moneyy)
                payment_type_tv.text=getString(R.string.cash)
            }
        }
    }

    private fun setChangePayment() {

        FirstFragment.changePayment.observe(this, Observer {
            when (it) {
                "cash" -> {
                    payment_type_iv.setImageResource(R.mipmap.moneyy)
                    payment_type_tv.text=getString(R.string.cash)
                    mPrefs?.selectedPayment=FirstFragment.PaymentType.cash
                }
                "stc" -> {
                    payment_type_iv.setImageResource(R.mipmap.stcpay)
                    payment_type_tv.text=getString(R.string.stc_payment)
                    mPrefs?.selectedPayment=FirstFragment.PaymentType.stc
                }
                else -> {
                    payment_type_iv.setImageResource(R.mipmap.visa)
                    payment_type_tv.text=getString(R.string.elect_payment)
                    mPrefs?.selectedPayment=FirstFragment.PaymentType.visa
                }
            }
        }
        )
    }

    private fun setPaymentType() {
        top_car_lay.visibility=View.GONE
        above_sep.visibility=View.GONE
        sep_promo.visibility=View.GONE
        balance_shimmer.visibility=View.GONE
        promo_hint.visibility=View.GONE
        rest.text=FirstFragment.myBalance
        payment_lay.onClick {
            MyWallet(repo,rest.text.toString(),FirstFragment.useBalanceFirst) {
                sendRequestChange(it)
            }.also {
                it.show(supportFragmentManager,"frag")
            }
        }
    }


    private fun sendRequestChange(isChecked:Boolean){

        repo.useBalance(isChecked,mPrefs?.token!!).subscribeWithLoading({
        },{
            if (it.value=="1"){
                FirstFragment.useBalanceFirst = it.data?.useBalanceFirst?.toBoolean()!!
            }
            else{
                toasty(it.msg!!,2)
            }
        },{
            //   hideProgressDialog()
            toasty(getString(R.string.error_connection),2)
            Log.e("error",it.message!!)
        },{
            // hideProgressDialog()
        })
    }

    private fun setActions() {
        et_dist.onClick {
            startActivityForResult(
                Intent(applicationContext
                    ,MapActivity::class.java), REQUEST_CODE
            )
        }
        order_btn.onClick {
            if (Util.checkError(et_dist,applicationContext) && Util.checkError(etExpected,applicationContext)
                && Util.checkError(etOrder,applicationContext)
            ){
                sendRequestOrder(mData)
            }
        }
    }

    private fun sendRequestOrder(data: Data?) {
        ridePayType = when (mPrefs!!.selectedPayment) {
            "visa" -> {
                FirstFragment.PaymentType.visa
            }
            "cash" -> {
                FirstFragment.PaymentType.cash
            }
            else -> {
                FirstFragment.PaymentType.stc
            }
        }

        repo.createOrder(place?.place_id.toString(),place?.reference.toString(),place?.name.toString(),
            place?.vicinity.toString(),place?.lat.toString(),place?.lng.toString(),et_dist.text.toString(),distLat.toString(),distLng.toString()
        ,mData?.distance.toString(),data?.time.toString(),data?.expected_price.toString(),ridePayType,etOrder.text.toString()
        ,mPrefs?.token!!
            ).subscribeWithLoading({showProgressDialog()},{
            hideProgressDialog()
            if (it.value=="1") {
                handleData(it.data)
            }
            else{
                showError(it.msg)
            }
        },{
            hideProgressDialog()
            showError(it.message!!)
        },{})
    }

    private fun handleData(data: com.aait.getak.models.order_details_model.Data?) {
        if (data!=null){
            Log.e("success","1")
            startActivity(Intent(this, TrackingActivity::class.java).apply {
                putExtra("order_id",data?.order_id!!)
                putExtra("isAcceptedRide",false)
            })
            finish()
        }
        else{
            Log.e("success","0")
        }
    }

    private fun showError(msg:String){
            toasty(msg,2)
        }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode== REQUEST_CODE){
                distLat=data?.getStringExtra("lat")
                distLng=data?.getStringExtra("lng")
                distLoc=data?.getStringExtra("address")
                et_dist.setText(distLoc)
                sendRequest(distLat,distLng)

            }
        }
    }

    private fun sendRequest(distLat: String?, distLng: String?) {
        repo.expectedPrice(place?.lat.toString(),place?.lng.toString(),distLat!!,distLng!!,mPrefs?.token!!)
            .subscribeWithLoading({
                progress_expected.visibility= View.VISIBLE
                order_btn.isEnabled=false
                order_btn.alpha=0.5f

            },{
                progress_expected.visibility= View.INVISIBLE
                order_btn.isEnabled=true
                order_btn.alpha=1f
                mData = it.data
                etExpected.setText(getString(R.string.expected_price)+it.data?.expected_price.toString()+" "+it.data?.currency)
            },{
                progress_expected.visibility= View.INVISIBLE
                order_btn.isEnabled=true
                order_btn.alpha=1f
            },{})
    }
}
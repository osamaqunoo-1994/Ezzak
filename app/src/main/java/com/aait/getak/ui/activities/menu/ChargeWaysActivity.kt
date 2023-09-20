package com.aait.getak.ui.activities.menu

import android.content.Intent
import android.util.Log
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton
import com.aait.getak.R
import com.aait.getak.base.ParentActivity
import com.aait.getak.models.payment.PaymentWaysData
import com.aait.getak.models.payment.PaymentWaysModel
import com.aait.getak.ui.adapters.PaymentWaysAdapter
import com.aait.getak.ui.dialogs.BottomSheetCard
import com.aait.getak.ui.dialogs.ChargeAmountSheet
import com.aait.getak.utils.Constant
import com.aait.getak.utils.Util
import com.aait.getak.utils.toasty
import com.oppwa.mobile.connect.checkout.dialog.CheckoutActivity
import com.oppwa.mobile.connect.checkout.meta.CheckoutSettings
import com.oppwa.mobile.connect.exception.PaymentError
import com.oppwa.mobile.connect.provider.Connect
import com.oppwa.mobile.connect.provider.Transaction
import com.oppwa.mobile.connect.provider.TransactionType
import com.oppwa.mobile.connect.threeds.OppThreeDSService
import com.oppwa.mobile.connect.threeds.constant.TransactionMode
import kotlinx.android.synthetic.main.my_wallet.check_balance
import kotlinx.android.synthetic.main.my_wallet_full.*
import org.jetbrains.anko.sdk27.coroutines.onCheckedChange
import org.jetbrains.anko.sdk27.coroutines.onClick

class ChargeWaysActivity : ParentActivity() {
    private lateinit var card: BottomSheetCard

    override val layoutResource: Int
        get() = R.layout.my_wallet_full

    private var paymentType:String =""

    override fun init() {
        setToolbar(getString(R.string.charge))
        setActions()
        getPaymentWays()
    }

    private fun setActions() {
        charge_lay.onClick {
            Util.openUrl(this@ChargeWaysActivity, Constant.PAYMENT_URL+"/"+mPrefs?.user?.id)
        }

        check_balance.onCheckedChange { buttonView, isChecked ->
            addDisposable(repo.useBalance(isChecked,mPrefs?.token!!).subscribeWithLoading({
            },{
                if (it.value=="1"){

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
            }))
        }

        cupon.onClick {
             card= BottomSheetCard(getString(R.string.shipping_cupon)){ code, btn->
                sendRequestCard(code,btn)
            }
            card.show(supportFragmentManager,"wallet")

        }
    }

    private fun sendRequestCard(
        code: String,
        btn: CircularProgressButton
    ) {
        addDisposable(repo.chargeCode(code,mPrefs!!.token!!).subscribeWithLoading({
         //   btn.setProgress(100f)
           // btn.revertAnimation()
            card.isCancelable=false
        },{

            if(it.value=="1"){
                toasty(it.msg!!)
                card.dismiss()
            }
            else{
                toasty(it.msg!!,2)
            }

        },{
            card.isCancelable=true
          //  btn.initialCorner=16f
           // btn.finalCorner=16f
            Log.e("error",it.message!!)
            toasty(getString(R.string.error_connection),2)
           // btn.setProgress(100f)
           // btn.revertAnimation()

        },{
           // btn.initialCorner=16f
            //btn.finalCorner=16f
            //btn.revertAnimation()

            card.isCancelable=true
        }))
    }

    private fun getPaymentWays(
    ) {
        addDisposable(repo.paymentWays().subscribeWithLoading({
            //   btn.setProgress(100f)
            // btn.revertAnimation()
            showProgressDialog()
        },{

            if (it.value=="1"){
                hideProgressDialog()
                setPaymentAdapter(it.data)
            }
            else{
                toasty(it.msg,2)
            }
        },{
            // isEnableToBack=true
            // hideProgressDialog()
            Log.e("error",it.message!!)
            toasty(getString(R.string.error_connection),2)
        },{
        }))
    }

    private fun setPaymentAdapter(paymentWaysList:MutableList<PaymentWaysData>){
        val removedAppleValue = paymentWaysList.find { it.type?.contains("APPLE") == true }
        paymentWaysList.remove(removedAppleValue)
        val paymentWaysAdapter = PaymentWaysAdapter(this,paymentWaysList){
            openChargeDialog(it)
        }
        rv_payment_ways.adapter = paymentWaysAdapter
    }

    private fun openChargeDialog(type:String){
        val dialog = ChargeAmountSheet(){
            hyperIndex(type,it)
        }
        dialog.show(supportFragmentManager,"chargeDialog")
    }

    private fun hyperIndex(
        type:String,amount:String
    ) {
        val newType = if(type == "MASTER") "VISA" else type
        addDisposable(repo.hyperIndex(newType,amount).subscribeWithLoading({
            //   btn.setProgress(100f)
            // btn.revertAnimation()
            showProgressDialog()
        },{
            hideProgressDialog()
            if (it.value=="1"){
                it.data?.let { it1 -> openCheckOut(type,it1) }
            }
            else{
                it.msg?.let { it1 -> toasty(it1,2) }
            }
        },{
            // isEnableToBack=true
            // hideProgressDialog()
            Log.e("error",it.message!!)
            toasty(getString(R.string.error_connection),2)
        },{
        }))
    }

    private fun openCheckOut(type:String,checkoutId:String){
        val paymentBrands2 = hashSetOf(type)
        val paymentBrands = listOf(type)
        OppThreeDSService.getInstance().initialize(
            applicationContext,
            TransactionMode.LIVE,
            paymentBrands)
        val checkoutSettings = CheckoutSettings(checkoutId, paymentBrands2, Connect.ProviderMode.LIVE)
        checkoutSettings.shopperResultUrl = "ezzakclient://result"
        val intent = checkoutSettings.createCheckoutActivityIntent(this)
        paymentType = if(type == "MASTER") "VISA" else type
        startActivityForResult(intent, CheckoutActivity.REQUEST_CODE_CHECKOUT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i("hyperResult", "onActivityResult: $resultCode")
        when (resultCode) {
            CheckoutActivity.RESULT_OK -> {
                /* transaction completed */
                val transaction: Transaction = data!!.getParcelableExtra(CheckoutActivity.CHECKOUT_RESULT_TRANSACTION)!!

                /* resource path if needed */
                val resourcePath =
                    data.getStringExtra(CheckoutActivity.CHECKOUT_RESULT_RESOURCE_PATH)

                resourcePath?.let { hyperResult(it) }
            }

            CheckoutActivity.RESULT_CANCELED -> {
                /* shopper cancelled the checkout process */
            }

            CheckoutActivity.RESULT_ERROR -> {
                /* error occurred */
                val error: PaymentError = data!!.getParcelableExtra(CheckoutActivity.CHECKOUT_RESULT_ERROR)!!
                Log.i("hyperResult", "onActivityResult: ${error.errorCode}")
            }
        }
    }

    private fun hyperResult(
        resourcePath:String
    ) {
        Log.i("hyperResult", "hyperResult: $resourcePath")
        addDisposable(repo.hyperResult(paymentType,resourcePath).subscribeWithLoading({
            //   btn.setProgress(100f)
            // btn.revertAnimation()
            showProgressDialog()
        },{
            hideProgressDialog()

            if (it.value=="1"){
                it.msg?.let { it1 -> toasty(it1,1) }
            }
            else{
                it.msg?.let { it1 -> toasty(it1,2) }
            }
        },{
            // isEnableToBack=true
            // hideProgressDialog()
            Log.e("error",it.message!!)
            toasty(getString(R.string.error_connection),2)
        },{
        }))
    }
}

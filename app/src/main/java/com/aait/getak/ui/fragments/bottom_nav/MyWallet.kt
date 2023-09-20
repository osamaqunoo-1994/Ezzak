package com.aait.getak.ui.fragments.bottom_nav

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.text.Html
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.TextView
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.aait.getak.R
import com.aait.getak.network.repository.other_repos.RemoteRepos
import com.aait.getak.ui.activities.core.MapPreviewActivity
import com.aait.getak.ui.activities.menu.ChargeWaysActivity
import com.aait.getak.ui.dialogs.SuccessDialog
import com.aait.getak.ui.fragments.homeFragments.FirstFragment
import com.aait.getak.ui.fragments.homeFragments.FirstFragment.Companion.changePayment
import com.aait.getak.utils.GlobalPreferences
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.switchmaterial.SwitchMaterial
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.my_wallet.view.*
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onCheckedChange
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject
import java.util.*


class MyWallet(val repo:RemoteRepos, val price:String, val isChecked:Boolean,
               private val hasPaymentOnline:Boolean = false
               , val onCheckChange:(checked:Boolean)->Unit): BottomSheetDialogFragment() {

    private var progressDialog: ACProgressFlower? = null
    private lateinit var mPrefs: GlobalPreferences
    private var mBalance: TextView?=null
    private val compositeDisposable = CompositeDisposable()

companion object{
    var isShowDialog=true
}

    override fun setupDialog(dialog: Dialog, style: Int) {
        val view = View.inflate(context, R.layout.my_wallet, null)
        if (dialog.window != null) {
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        }
        dialog.setContentView(view)
        mPrefs = GlobalPreferences(activity!!)
        mPrefs.lang?.let { resources.setLanguage(it) }
        val switch: SwitchMaterial = view.find(R.id.check_balance)
         mBalance = view.find(R.id.blance)

        switch.isChecked=isChecked
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mBalance?.text=Html.fromHtml(activity?.resources?.getString(R.string.use_mycharge)?.let {
                String.format(
                    it, price)
            }, Html.FROM_HTML_MODE_LEGACY)
        }
        else{
            mBalance?.text=
                Html.fromHtml(activity?.resources?.getString(R.string.use_mycharge)?.let {
                    String.format(
                        it, price)
                })
        }
        if(hasPaymentOnline){
            view.markerـelect.visibility = View.VISIBLE
            view.elect_payment_lay.visibility = View.VISIBLE
        }
        //set checks markers
        when (mPrefs.selectedPayment) {
            FirstFragment.PaymentType.visa -> {
                view.markerـelect.visibility=View.VISIBLE
                view.marker.visibility=View.GONE
                view.marker_stc.visibility=View.GONE
            }
            FirstFragment.PaymentType.cash -> {
                view.marker.visibility=View.VISIBLE
                view.markerـelect.visibility=View.GONE
                view.marker_stc.visibility=View.GONE
            }
            else -> {
                view.marker.visibility=View.GONE
                view.markerـelect.visibility=View.GONE
                view.marker_stc.visibility=View.VISIBLE
            }
        }

        //action listeners
        view.elect_payment_lay.onClick {
//            if (mPrefs.user?.card_token.isNullOrBlank()){
//                registerCreditCard()
//
//            }
//            else {
//                showMarkerOnly(view)
//            }
            view.marker.visibility=View.GONE
            view.markerـelect.visibility=View.VISIBLE
            view.marker_stc.visibility=View.GONE
            mPrefs.selectedPayment=FirstFragment.PaymentType.visa
            changePayment.value=FirstFragment.PaymentType.visa
        }


        view.cash_lay.onClick {
                view.marker.visibility=View.VISIBLE
                view.markerـelect.visibility=View.GONE
                view.marker_stc.visibility=View.GONE
                mPrefs.selectedPayment=FirstFragment.PaymentType.cash
                changePayment.value=FirstFragment.PaymentType.cash

            }


        view.stc_lay.onClick {
            view.marker.visibility=View.GONE
            view.marker_stc.visibility=View.VISIBLE
            view.markerـelect.visibility=View.GONE
            changePayment.value=FirstFragment.PaymentType.stc
            mPrefs.selectedPayment=FirstFragment.PaymentType.stc
        }


        switch.onCheckedChange { buttonView, isChecked ->
                onCheckChange(isChecked)
        }
        view.wallet_lay.onClick {
            startActivity(Intent(activity,ChargeWaysActivity::class.java))
        }

        MapPreviewActivity.isRegisteredVisa.observe(activity!!, androidx.lifecycle.Observer {
            mPrefs.selectedPayment=FirstFragment.PaymentType.visa
            if (it){
                view.marker.visibility=View.GONE
                view.marker_stc.visibility=View.GONE
                view.markerـelect.visibility=View.VISIBLE
                showSuccessDialog()
            }

        })

    }

    private fun showMarkerOnly(view: View) {
        view.marker.visibility=View.GONE
        view.marker_stc.visibility=View.GONE
        view.markerـelect.visibility=View.VISIBLE
    }

    private fun registerCreditCard() {
        isShowDialog = true
//        urwayPayment.makepaymentService(
//            "1.00", activity, "12", "SAR", "", "", "", ""
//            , "", "", mPrefs.user?.phone.toString() + "@getak.com", "", "", "", "", "SA",
//            "1411", "A", UUID.randomUUID().toString() + "", "0"
//        )
    }

    private fun showSuccessDialog() {
        if (isShowDialog) {
            val dialog = SuccessDialog()
            activity?.supportFragmentManager?.let { dialog.show(it, "congrats") }
        }
    }

    override fun onResume() {
        super.onResume()
        sendRequest()
    }
    fun showProgressDialog(message: String?=null) {
        progressDialog = ACProgressFlower.Builder(activity)
            .direction(ACProgressConstant.DIRECT_CLOCKWISE)
            .themeColor(Color.GRAY)
            .fadeColor(Color.WHITE)
            .build()
        progressDialog!!.show()
    }

    fun hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog!!.hide()
            progressDialog!!.dismiss()
        }
    }

    @SuppressLint("CheckResult")
    private fun sendRequest() {
        compositeDisposable.add(
          repo.getCarTypes("0.0","0.0","0.0","0.0", GlobalPreferences(activity!!).token!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                it.message?.let { it1 -> Log.e("error", it1) }
            }
            .subscribe {
                if (it!=null) {
                    it.data?.balance?.let { it1 -> Log.e("balance", it1) }
                    if (activity!=null) {
                        mBalance?.text = Html.fromHtml(
                            String.format(
                                activity?.resources!!.getString(R.string.use_mycharge),
                                it.data?.balance
                            )
                        )
                    }
                }

            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data !=null) {
            if (requestCode == 2) {
                val message = data.getStringExtra("MESSAGE")
                val jsonObj =
                    JSONObject(message)
                val tranId = jsonObj.get("TranId").toString()
                val amount = jsonObj.get("amount").toString()
                val result = jsonObj.get("result").toString()
                val payId = jsonObj.get("payId").toString()
                val cardToken = jsonObj.get("cardToken").toString()
                Log.e("money_1", "$cardToken,$result$amount$payId$tranId")

                mPrefs.token?.let {
                    compositeDisposable.add(
                    repo.saveCreditCard(mPrefs.user?.id!!,cardToken, it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        showProgressDialog()
                    }

                    .doOnError {
                        hideProgressDialog()
                        it.message?.let { it1 -> Log.e("error", it1) }
                        toast(getString(R.string.error_connection))
                    }
                    .subscribe {
                        hideProgressDialog()
                        if (it.value=="1"){
                            Log.e("user",it.data.toString())
                        }
                    })
                }

            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        compositeDisposable.clear()
    }
    fun Resources.setLanguage(lang:String){
// Change locale settings in the app.
        val dm = this.displayMetrics
        val conf = this.configuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            conf.setLocale( Locale(lang))
        } // API 17+ only.
        this.updateConfiguration(conf, dm)
    }
}
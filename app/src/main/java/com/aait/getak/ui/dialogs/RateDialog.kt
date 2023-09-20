package com.aait.getak.ui.dialogs

import android.content.DialogInterface
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.aait.getak.R
import kotlinx.android.synthetic.main.rate_dialog.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import com.aait.getak.models.order_details_model.Data
import com.aait.getak.utils.GlobalPreferences
import com.squareup.picasso.Picasso
import java.util.*

class RateDialog(val data: Data,val onRate:(rate:Float)->Unit,val onCancel:()->Unit) : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.rate_dialog, container, false)
        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )


        }
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


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        GlobalPreferences(activity!!).apply {
            lang?.let {
                resources.setLanguage(it)
                Log.e("lang",it)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            name.text=
                Html.fromHtml(activity?.resources?.getString(R.string.ride_review)?.let {
                    String.format(
                        it,"${data.captin_name}")
                }, Html.FROM_HTML_MODE_LEGACY)
        }
        else{
            name.text=
                Html.fromHtml(activity?.resources?.getString(R.string.ride_review)?.let {
                    String.format(
                        it,"${data.captin_name}")
                })
        }
        rate.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            if (fromUser){
                captin_btn.alpha=1.0f
                captin_btn.isEnabled=true
            }
            else{
                captin_btn.alpha=0.5f
                captin_btn.isEnabled=false
            }
        }
        period.text=data.date
        //rate.rating=data.captin_rate!!

        if (data.paymentType=="visa"){
            money_iv.setImageResource(R.mipmap.visa)
//            cash_type.text=activity?.resources?.getString(R.string.elect_payment)
        }
        else if (data.paymentType=="stc"){
            money_iv.setImageResource(R.mipmap.stcpay)
//            cash_type.text=activity?.resources?.getString(R.string.stc_payment)
        }
        else{
            money_iv.setImageResource(R.mipmap.moneyy)
//            cash_type.text=activity?.resources?.getString(R.string.cash)
        }
        ride_price.text= data.total_required_price +" "+data.currency
        vat_price.text=data.vat.toString()+" "+data.currency
        ride_discount.text=data.coupon_discount.toString()+" "+data.currency
        Picasso.get().load(data.captin_img).into(iv_captin)
        captin_btn.onClick {
            dialog?.dismiss()
            onRate(rate.rating)
        }
    }



    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        dismiss()
        onCancel()


    }
}
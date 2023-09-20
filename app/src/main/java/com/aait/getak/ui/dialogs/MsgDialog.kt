package com.aait.getak.ui.dialogs

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.aait.getak.R
import com.aait.getak.ui.activities.menu.ChargeWaysActivity
import com.aait.getak.ui.activities.menu.MyDebtChargeWaysActivity
import kotlinx.android.synthetic.main.dialog_msg.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class MsgDialog(private val amount:String,private val chargedAmount:String,private val onPay:()->(Unit),private val onDismiss:()->(Unit)): DialogFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_msg, container, false)
        // Set transparent background and no title
//        if (dialog != null && dialog!!.window != null) {
//            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
//
//        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tv_price?.text = amount
        ok_btn.onClick {
            dismiss()
            onPay.invoke()
            sendRequest()
        }

        cancel_btn.onClick {
            onDismiss.invoke()
            dismiss()
        }
    }

    private fun sendRequest() {
        startActivity(Intent(activity, MyDebtChargeWaysActivity::class.java).putExtra("price",chargedAmount))
    }


}
package com.aait.getak.ui.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.animation.doOnEnd
import androidx.fragment.app.DialogFragment
import com.aait.getak.R
import com.aait.getak.ui.fragments.bottom_nav.MyWallet.Companion.isShowDialog
import kotlinx.android.synthetic.main.congrats_dialog.*

class SuccessDialog: DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.congrats_dialog, container, false)
        // Set transparent background and no title
        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        lotti_iv.addAnimatorUpdateListener {
            it.doOnEnd {
                isShowDialog=false
                dialog?.dismiss()
                dialog?.cancel()
                //Log.e("dialog","finish animation")

            }
        }
    }
}
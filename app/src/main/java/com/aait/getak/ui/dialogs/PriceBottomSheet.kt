package com.aait.getak.ui.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.RadioButton
import com.aait.getak.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onCheckedChange
import org.jetbrains.anko.sdk27.coroutines.onClick

class PriceBottomSheet(val onClick:(priceType:Int)->Unit): BottomSheetDialogFragment() {
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        var type=1
        val view = View.inflate(context, R.layout.bottom_price, null)
        if (dialog.window != null) {
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)

        }
        dialog.setContentView(view)
        val offer_radio=view.find<RadioButton>(R.id.offer_price)
        val share_radio=view.find<RadioButton>(R.id.share_price)
        val nextBtn=view.find<MaterialButton>(R.id.next_price_btn)
        val skipBtn=view.find<MaterialButton>(R.id.skip_price_btn)
        offer_radio.isChecked=true

        share_radio.onCheckedChange { buttonView, isChecked ->
            //share radio
            type=1
            onClick(type)
        }

        offer_radio.onCheckedChange { buttonView, isChecked ->
            //offer radio
            type=2
            onClick(type)
        }
        nextBtn.onClick {
            onClick(type)
        }
        skipBtn.onClick {
            type=0
            onClick(type)
        }
    }
}
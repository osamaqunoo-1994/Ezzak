package com.aait.getak.ui.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import com.aait.getak.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onClick

class BottomSheetPhotos: BottomSheetDialogFragment() {
    var onOpenClick: ((Int) -> Unit)? = null
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }


    fun onOpenListerner(onOpenClick:(choose:Int) -> Unit){
        this.onOpenClick=onOpenClick
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        val view = View.inflate(context, R.layout.bottom_sheet_photo, null)
        if (dialog.window != null) {
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)

        }
        dialog.setContentView(view)
       // val btn: CircularProgressButton = view.find(R.id.activate)
        val cam=view.find<MaterialButton>(R.id.camera)
        val gal=view.find<MaterialButton>(R.id.photo)
        cam.onClick {
            onOpenClick?.let { state -> state(1)
            dismiss()
            }
        }
        gal.onClick {
            onOpenClick?.let { state->  state(2) }
            dismiss()
        }
    }
}
/*
package com.aait.getak.Ui.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.aait.getak.R

class PopDialog: DialogFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_pop, container, false)
        // Set transparent background and no title
        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)

        }
        is_owner = arguments!!.getBoolean("is_owner")
        if (is_owner){
            owner=getString(R.string.project_owner)
        }
        else{
            owner=getString(R.string.investor)
        }


        return view
    }

    private var owner: String=""
    private var is_owner: Boolean=false
    var onConfirmCallBack: ((Boolean) -> Unit)? = null
    fun confirmCallBackListener( onConfirmCallBack: ((Boolean) -> Unit)?){
        this.onConfirmCallBack=onConfirmCallBack
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        msg.text=getString(R.string.confirm_msg_pop,owner)
        ok_btn.onClick {
            onConfirmCallBack!!.invoke(true)

        }

        cancel_btn.onClick {
            dismiss()
        }
    }



}
*/

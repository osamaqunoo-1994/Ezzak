package com.aait.getak.ui.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.view.inputmethod.EditorInfo
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton
import com.aait.getak.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onEditorAction

class BottomSheetCard(val hint:String?=null,val onMoney:(money:String,btn: CircularProgressButton)->Unit): BottomSheetDialogFragment() {


    override fun setupDialog(dialog: Dialog, style: Int) {
        val view = View.inflate(context, R.layout.bottom_sheet_card, null)
        if (dialog.window != null) {
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        }

        dialog.setContentView(view)
        val btn: CircularProgressButton = view.find(R.id.activate)
        val etshiping: TextInputEditText = view.find(R.id.etshiping)
        val shipingLay: TextInputLayout = view.find(R.id.shipingLay)
        shipingLay.hint=hint
        etshiping.hint=hint
        etshiping.onEditorAction { v, actionId, event ->
            if (actionId== EditorInfo.IME_ACTION_DONE){
                onMoney(etshiping.text.toString(),btn)
            }
            else if (actionId== EditorInfo.IME_ACTION_NEXT){
                onMoney(etshiping.text.toString(),btn)
            }
            dismiss()
        }
        
    }

    private fun sendRequest(btn: CircularProgressButton,shiping_txt:String) {
        this@BottomSheetCard.isCancelable=false
        onMoney(shiping_txt,btn)
        /*this@BottomSheetCard.isCancelable=false
        RetroWeb.getClient().add_charge(shiping_txt,GlobalPreferences(context!!).token!!)
                            .enqueue(object : Callback<ChargeModel> {
                                override fun onFailure(call: Call<ChargeModel>, t: Throwable) {
                                    this@BottomSheetCard.isCancelable=true
                                    Log.e("error",t.message)
                                    toast(getString(R.string.error_connection))
                                    btn.setProgress(100f)
                                    btn.revertAnimation()

                                }

                                override fun onResponse(call: Call<ChargeModel>, response: Response<ChargeModel>) {
                                    this@BottomSheetCard.isCancelable=true
                                    btn.revertAnimation()
                                    activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                                    if (response.body()!!.value=="1"){
                                            toast(getText(R.string.charged_successfully))
                                        onMoney(response.body()!!.data.balance!!)
                                        this@BottomSheetCard.dismiss()
                                        }
                                    else{
                                            toast(response.body()!!.msg.toString())
                                        }
                                }
                            })*/

        }


}
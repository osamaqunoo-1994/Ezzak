package com.aait.getak.ui.activities.settings
import android.content.Intent
import android.util.Log
import com.aait.getak.R
import com.aait.getak.base.ParentActivity
import com.aait.getak.ui.activities.menu.LanguageActivity
import com.aait.getak.utils.Util
import com.aait.getak.utils.toasty
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.sdk27.coroutines.onCheckedChange
import org.jetbrains.anko.sdk27.coroutines.onClick

class SettingsActivity : ParentActivity() {
    private var isNotify: Boolean = true
    private var lang: String = Util.language
    override val layoutResource: Int
        get() = R.layout.activity_settings

    override fun init() {
        setToolbar(getString(R.string.settings))

        lang_lay.onClick {
            val intent = Intent(this@SettingsActivity, LanguageActivity::class.java)
            intent.putExtra("lang",lang)
            startActivity(intent)
        }
        switch_.onCheckedChange { buttonView, isChecked ->
            FirebaseMessaging.getInstance().token.addOnCompleteListener {
                if (it.isSuccessful) {
                    val deviceId = it.result
                    sendRequest(isChecked,deviceId)
                }
            }
        }
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful) {
                val deviceId = it.result
                sendRequestDevice(deviceId)
            }
        }
    }

    private fun sendRequestDevice(deviceId:String) {
        addDisposable(repo.settings(mPrefs!!.token!!,deviceId).subscribeWithLoading({
            showProgressDialog()
        },{
            if (it.value=="1"){
                isNotify=it.data!!.ordersNotify!!.toBoolean()

                    switch_.isChecked=isNotify

            }
            else{
                toasty(it.msg!!,2)
            }
        },{
            hideProgressDialog()
            toasty(getString(R.string.error_connection),2)
            Log.e("error",it.message!!)
        },{
            hideProgressDialog()

        }))
    }

    private fun sendRequest(checked: Boolean,deviceId: String) {
        addDisposable(repo.updateNotif(deviceId = deviceId,checked.toString(),mPrefs!!.token!!)
            .subscribeWithLoading({
              //  showProgressDialog()
            },{
                if (it.value=="1"){
                    toasty(it.data!!)
                }
                else{
                    toasty(it.msg!!,2)
                }
            },
                {
                hideProgressDialog()
                toasty(getString(R.string.error_connection),2)
               // Log.e("error",it.message!!)
            },{
               hideProgressDialog()
            })
        )
    }




}

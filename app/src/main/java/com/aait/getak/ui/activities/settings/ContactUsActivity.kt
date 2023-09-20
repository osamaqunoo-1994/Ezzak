package com.aait.getak.ui.activities.settings

import android.util.Log
import com.aait.getak.R
import com.aait.getak.base.ParentActivity
import com.aait.getak.utils.Util
import com.aait.getak.utils.toasty
import kotlinx.android.synthetic.main.activity_contact_us.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class ContactUsActivity : ParentActivity() {
    override val layoutResource: Int
        get() = R.layout.activity_contact_us

    override fun init() {
        setToolbar(getString(R.string.contact_us))
        sendRequest()
        setActions()
    }

    private fun setActions() {
        mail_lay!!.onClick {
            Util.openMail(this@ContactUsActivity,mail.text.toString())
        }
        phone_lay.onClick {
            setPermissionsPhone {
                Util.onCall(this@ContactUsActivity,phone.text.toString())
            }

        }

        send_btn.onClick {
                if (Util.checkError(etContact,this@ContactUsActivity)){
                    sendRequestMsg()
                }
        }

    }

    private fun sendRequestMsg() {
        addDisposable(repo.contactMSg(etContact.text.toString(),mPrefs!!.token!!).subscribeWithLoading({
            showProgressDialog()
        },{
            if (it.value=="1"){
                toasty(it.msg.toString())
                etContact.setText("")
            }
            else{
                toasty(it.msg!!,2)
            }
        },{
            hideProgressDialog()
            toasty (getString(R.string.error_connection),2)
            Log.e("error",it.message!!)
        },{
            hideProgressDialog()
        }))
    }

    private fun sendRequest() {
        addDisposable(repo.contactUs(mPrefs!!.token!!).
            subscribeWithLoading({
                showProgressDialog()
            },{
                if (it.value=="1"){
                    mail.text=it.data!!.email
                    phone.text=it.data!!.phone

                }
                else{
                    toasty(it.msg!!)
                }
            },{
                hideProgressDialog()
                toasty(it.message!!)
            },{
                hideProgressDialog()
            }))
    }


}

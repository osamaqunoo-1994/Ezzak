package com.aait.getak.ui.activities.notifications

import android.content.Intent
import android.util.Log
import com.aait.getak.R
import com.aait.getak.base.ParentActivity
import com.aait.getak.ui.activities.core.MapPreviewActivity
import com.aait.getak.utils.toasty
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_rate.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class RateActivity : ParentActivity() {
    private var avatar: String?=""
    private var userId: Int=0
    override val layoutResource: Int
        get() = R.layout.activity_rate

    override fun init() {
        setToolbar(getString(R.string.rate_captin))
        userId = intent.getIntExtra("user_id", 0)
        avatar = intent.getStringExtra("avatar")
        Picasso.get().load(avatar).into(avatar_iv)
        send_btn.onClick {
            sendRequest()
        }
    }

    private fun sendRequest() {
        addDisposable(repo.rateUser(userId,rate_bar.rating,etNotes.text.toString(),block_captin.isChecked,mPrefs!!.token!!)
            .subscribeWithLoading({
                showProgressDialog()
            },{
                if (it.value=="1"){
                    val intent = Intent(this, MapPreviewActivity::class.java)
                    startActivity(intent)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    finish()
                }
                else{
                    toasty(it.msg!!,2)
                }
            },{
                hideProgressDialog()
                Log.e("Error",it.message!!)
                toasty(getString(R.string.error_connection),2)
            },{
                hideProgressDialog()
            })
        )
    }

}

package com.aait.getak.ui.activities.notifications

import android.content.Intent
import android.util.Log
import com.aait.getak.R
import com.aait.getak.base.ParentActivity
import com.aait.getak.utils.toasty
import kotlinx.android.synthetic.main.activity_bill.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class BillActivity : ParentActivity() {
    private var userId: Int?=0
    private var avatar: String?=""
    override val layoutResource: Int
        get() = R.layout.activity_bill

    override fun init() {
        setToolbar(getString(R.string.total_cost))
        sendRequest()
        setActions()
    }

    private fun setActions() {
        rate_captin_btn.onClick {
            val intent = Intent(this@BillActivity, RateActivity::class.java)
            intent.putExtra("user_id",userId)
            intent.putExtra("avatar",avatar)
            startActivity(intent)

        }
    }

    private fun sendRequest(){
        addDisposable(repo.showBill(intent.getIntExtra("order_id",0),mPrefs!!.token!!)
            .subscribeWithLoading({
                showProgressDialog()
            },{
                if (it.value=="1"){
                    totally.text=it.data?.price
                    avatar=it.data?.avatar
                    userId=it.data?.user_id
                }
                else{
                    toasty(it.msg!!,2)
                }
            },{
                Log.e("error",it.message!!)
                toasty(getString(R.string.error_connection),2)
            },{
                hideProgressDialog()
            })
        )

    }
}

package com.aait.getak.ui.activities.menu

import android.content.Intent
import android.util.Log
import com.aait.getak.R
import com.aait.getak.base.ParentActivity
import kotlinx.android.synthetic.main.activity_my_charge.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class MyChargeActivity : ParentActivity() {
    override val layoutResource: Int
        get() = R.layout.activity_my_charge

    override fun onResume() {
        super.onResume()
        sendrequestBalance()
    }
    override fun init() {
        setToolbar(getString(R.string.wallet))
        charge_btn.onClick {
            startActivity(Intent(applicationContext,ChargeWaysActivity::class.java))
            //Intent(applicationContext,)
        }
        transfer_charge_btn.onClick {
            startActivity(Intent(applicationContext,TransferChargeActivity::class.java))

        }
    }

    private fun sendrequestBalance() {
        repo.getBalance(mPrefs!!.token!!).subscribeWithLoading({
            showProgressDialog()
        },{
            if (it.value=="1"){

            }
        },{
            hideProgressDialog()
            Log.e("error",it.message!!)
        },{
            hideProgressDialog()
        })
    }

}

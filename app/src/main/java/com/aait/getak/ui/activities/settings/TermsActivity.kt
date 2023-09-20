package com.aait.getak.ui.activities.settings

import android.util.Log
import com.aait.getak.R
import com.aait.getak.base.ParentActivity
import com.aait.getak.utils.toasty
import kotlinx.android.synthetic.main.activity_terms.*

class TermsActivity : ParentActivity() {
    override val layoutResource: Int
        get() = R.layout.activity_terms

    override fun init() {
        setToolbar(getString(R.string.terms))
        sendRequest()
    }

    private fun sendRequest() {
        addDisposable(repo.terms(/*mPrefs!!.token!!*/)
            .subscribeWithLoading({
                showProgressDialog()
            },{
                if (it.value=="1"){
                    terms.text=it.data!!.terms
                }
                else{
                    toasty(getString(R.string.error_connection),2)
                }
            },{
                hideProgressDialog()
                toasty(getString(R.string.error_connection),2)
                Log.e("error",it.message!!)
            },{
               hideProgressDialog()
            }))
    }

}

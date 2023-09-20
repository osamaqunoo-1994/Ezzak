package com.aait.getak.ui.fragments.bottom_nav

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import com.aait.getak.R
import com.aait.getak.base.BaseFragment
import com.aait.getak.utils.toasty
import kotlinx.android.synthetic.main.my_points.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class MyPoints:BaseFragment() {
    override val viewId: Int
        get() = R.layout.my_points
    override fun init(view: View) {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sendRequest()
        setActions()
    }

    private fun setActions() {
        exchange_btn.onClick {
            addDisposable(repo.replace_charge(mPrefs.token!!).subscribeWithLoading({
                showProgressDialog()
            },{
                if (it.value=="1"){
                    activity!!.toasty(it.msg!!)
                    points.text="0"
                }
                else{
                    activity!!.toasty(it.msg!!,2)
                }
            }
            ,{
                    hideProgressDialog()
                    activity!!.toasty(getString(R.string.error_connection),2)
                    Log.e("error",it.message!!)
                }
            ,{
                    hideProgressDialog()
                }
            ))
        }
    }

    @SuppressLint("NewApi")
    private fun sendRequest() {
        repo.points(mPrefs.token!!).subscribeWithLoading({
            if (it.value=="1"){
                points.text=it.data!!.points
            }
            else{
                activity!!.toasty(it.msg!!,2)
            }
        },{
            Log.e("error",it.message!!)
            activity!!.toasty(getString(R.string.error_connection),2)
        })
    }
}
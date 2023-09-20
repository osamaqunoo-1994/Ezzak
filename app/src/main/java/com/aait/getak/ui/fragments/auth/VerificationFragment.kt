package com.aait.getak.ui.fragments.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.aait.getak.R
import com.aait.getak.base.BaseFragment
import com.aait.getak.models.register_model.RegisterModel
import com.aait.getak.network.remote_db.Resource
import com.aait.getak.ui.activities.core.MapPreviewActivity
import com.aait.getak.ui.view_model.RegisterViewModel
import com.aait.getak.utils.toasty
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import com.facebook.HttpMethod
import com.facebook.login.LoginManager
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_verification.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.koin.android.viewmodel.ext.android.viewModel

class VerificationFragment:BaseFragment() {
    private val viewModel: RegisterViewModel by viewModel()
    override val viewId: Int
        get() = R.layout.fragment_verification

    override fun init(view: View) {
        context!!.hideKeyboard(view)
    }

    @SuppressLint("CheckResult")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val user = mPrefs.user
        if (user != null){
            phoneNum.text = mPrefs.user!!.phone
        }

        setActions()
        setState()

    }

    @SuppressLint("CheckResult")
    private fun setActions() {
        phone_edit.onClick {
            activity!!.v_pager.currentItem=0
        }

        viewModel.checkPattern(firstPinView)!!.subscribe {
            if (it){
                sendRequest()
            }
        }

        resendCode_btn.onClick {
               sendRequestResend()
        }
    }

    private fun sendRequestResend() {
        repo.resendCode(mPrefs.token!!).subscribeWithLoading({
            showProgressDialog()
        },{
            hideProgressDialog()
            if (it.value=="1"){
                mPrefs.token="Bearer ${it.data!!.token}"
                activity!!.toasty(getString(R.string.code_sent))
            }
        },{
            hideProgressDialog()
            Log.e("error",it.message!!)
        },{

        })
    }


    private fun setState(){
        viewModel.states!!.observe(this, Observer {
            when{

                it?.status == Resource.Status.SUCCESS -> {
                    hideProgressDialog()
                    val model = it.data as RegisterModel
                    if (model.value=="1") {
                        mPrefs.storeUser(model.data!!)
                        mPrefs.token="Bearer ${model.data!!.token}"
                        if (!model.data?.card_token.isNullOrBlank()) mPrefs.isPaidVisa=true
                        if (model.data!!.is_registered){
                            goHome()
                        }
                        else {
                            goNext()
                        }
                    }
                    else{
                        activity!!.toasty(model.msg!!,2)
                        Log.e("error",model.msg!!)
                    }
                }
                it?.status== Resource.Status.ERROR ->{
                    hideProgressDialog()
                    activity!!.toasty(it.message!!,2)
                    Log.e("error",it.message)
                }
                it?.status== Resource.Status.LOADING_FIRST ->{
                    showProgressDialog()
                }
            }
        })
    }

    private fun goHome(){
    logout()
    mPrefs.storeSession(true)
    val intent = Intent(activity, MapPreviewActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
    activity!!.finish()
    activity!!.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)

    }
    private fun goNext() {
    activity!!.v_pager.currentItem=2

    }
    private fun logout() {

        GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, GraphRequest.Callback {
                graphResponse: GraphResponse? -> LoginManager.getInstance().logOut()
        }
        ).executeAsync()

    }

    private fun sendRequest() {
       // toast(firstPinView.text.toString()+","+mPrefs.token!!)
        //Log.e("token",mPrefs.token!!)
        viewModel.sendVerification(firstPinView.text.toString(),mPrefs.token!!)
      /*  repo.verification(firstPinView.text.toString(),mPrefs.token!!).subscribeWithLoading({
            showProgressDialog()
        },{
            hideProgressDialog()
            states!!.value=Resource.success(it)
        },{
            hideProgressDialog()
            states!!.value= Resource.error(it.message!!,it)
        },{
            Log.e("completed","done")
        })*/


    }

}
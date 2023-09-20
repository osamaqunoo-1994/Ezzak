package com.aait.getak.ui.fragments.auth

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import com.aait.getak.R
import com.aait.getak.base.BaseFragment
import com.aait.getak.ui.activities.auth.RegisterActivity
import com.aait.getak.ui.view_model.RegisterViewModel
import com.aait.getak.utils.toasty
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_mail.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.koin.android.viewmodel.ext.android.viewModel

class MailFragment: BaseFragment() {
    private val viewModel: RegisterViewModel by viewModel()
    override val viewId: Int
        get() = R.layout.fragment_mail

    override fun init(view: View) {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity!!.hideKeyboard()

        var styledText = Html.fromHtml(getString(R.string.continue_face))
        if (mPrefs.isLogin){
            face_login.visibility=View.GONE
            skip_btn.visibility=View.INVISIBLE
        }

        face_login.text=styledText
        checkMail()
        continue_btn.onClick {
            if (mPrefs.isLogin){
                sendRequestEdit(etMail.text.toString())
            }
            else {
                goNext()
            }
        }
        skip_btn.onClick {
            goNext()
        }
    }

    private fun sendRequestEdit(mail:String?) {
        repo.editProfile(mail= mail!!,token = mPrefs.token!!).subscribeWithLoading({
            showProgressDialog()
        },{
            if (it.value=="1"){
                it.data?.let { user -> mPrefs.storeUser(user)
                    mPrefs.token="Bearer "+user.token
                }
                activity!!.toasty(getString(R.string.updated_successfully))
            }
            else{
                activity!!.toasty(it.msg!!,2)
            }
        },{
            hideProgressDialog()
            Log.e("error",it.message!!)
        },{
            hideProgressDialog()
        })
    }

    private fun goNext() {
        RegisterActivity.email=etMail.text.toString()
        activity!!.v_pager.currentItem=3
    }

    @SuppressLint("CheckResult")
    private fun checkMail():Boolean{
        var isMailCorrect=false
        viewModel.checkMail(etMail)!!.subscribe {
            if (!it) {
                continue_btn.disable()
               // error_txt_mail.visibility=View.VISIBLE
            } else {
                continue_btn.enable()
                isMailCorrect=true
             //   error_txt_mail.visibility=View.GONE
            }
            //activity!!.v_pager.setPagingEnabled(it)
        }
        return isMailCorrect
    }

}
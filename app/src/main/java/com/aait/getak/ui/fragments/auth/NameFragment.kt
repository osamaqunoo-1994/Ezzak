package com.aait.getak.ui.fragments.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.Observer
import com.aait.getak.R
import com.aait.getak.base.BaseFragment
import com.aait.getak.models.register_model.RegisterModel
import com.aait.getak.network.remote_db.Resource
import com.aait.getak.ui.activities.auth.RegisterActivity
import com.aait.getak.ui.activities.core.MapPreviewActivity
import com.aait.getak.ui.activities.settings.TermsActivity
import com.aait.getak.ui.view_model.RegisterViewModel
import com.aait.getak.utils.toasty
import com.facebook.login.LoginManager
import kotlinx.android.synthetic.main.fragment_name.*
import kotlinx.android.synthetic.main.fragment_name.continue_btn
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.toast
import org.koin.android.viewmodel.ext.android.viewModel

class NameFragment: BaseFragment() {
    private val viewModel: RegisterViewModel by viewModel()
    override val viewId: Int
        get() = R.layout.fragment_name

    override fun init(view: View) {
        context!!.hideKeyboard(view)
    }
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (context!=null){
            if (isVisibleToUser){
                activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            }
            else{
                activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            }
        }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        checkNameTerms()
        setState()
        if (mPrefs.isLogin) {
            terms_lay.visibility = View.GONE
        }
        continue_btn.onClick {
            if (mPrefs.isLogin){

                sendRequestEdit(etFName.text.toString())
            }
            else {
                terms_lay.visibility=View.VISIBLE
                sendRequest()
            }
            }
        terms.onClick {
            startActivity(Intent(activity,TermsActivity::class.java))
        }
    }

    private fun sendRequestEdit(name:String?) {
            repo.editProfile(name=name!!,token = mPrefs.token!!).subscribeWithLoading({
                showProgressDialog()
            },{
                if (it.value=="1"){
                    it.data?.let { user -> mPrefs.storeUser(user)
                        mPrefs.token="Bearer "+user.token
                        activity!!.toasty(getString(R.string.updated_successfully))


                    }
                    activity?.onBackPressed()

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

    private fun goHome() {
        mPrefs.storeSession(true)
        LoginManager.getInstance().logOut()
        val intent = Intent(activity!!, MapPreviewActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        activity!!.finish()
        activity!!.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }


    private fun setState(){
        viewModel.states!!.observe(this, Observer {
            when{

                it?.status == Resource.Status.SUCCESS -> {
                    hideProgressDialog()
                    val model = it.data as RegisterModel
                    if (model.value=="1") {
                        mPrefs.storeUser(model.data!!)
                        mPrefs.token="Bearer "+model.data!!.token
                        if (!model.data?.card_token.isNullOrBlank()) mPrefs.isPaidVisa=true
                        goHome()
                    }
                    else{
                        activity!!.toasty(model.msg!!,2)
                    }
                }
                it?.status== Resource.Status.ERROR ->{
                    hideProgressDialog()
                    activity!!.toasty(it.message!!,2)
                }
                it?.status== Resource.Status.LOADING_FIRST ->{
                    showProgressDialog()
                }
            }
        })
    }

    private fun sendRequest() {
        viewModel.register(RegisterActivity.phone,RegisterActivity.country_iso,
            RegisterActivity.email,RegisterActivity.password,etFName.text.toString()+" "+etLName.text.toString(),
            RegisterActivity.friend_code,RegisterActivity.socialId,mPrefs.token!!)
        }
    @SuppressLint("CheckResult")
    private fun checkNameTerms():Boolean{
        var isNameEmpty=true
        val checkAllTerms = viewModel.checkAllTerms(etFName, etLName, check_terms)
        checkAllTerms!!.subscribe {
            //all accepted
            if (it==0){
                 isNameEmpty=false
                continue_btn.enable()
            }
            //terms not accepted
            else if (it==1){
               // activity!!.v_pager.setPagingEnabled(false)
                toast(getString(R.string.accept_terms))
                continue_btn.disable()
            }
            //first
            else if (it==2){
                //activity!!.v_pager.setPagingEnabled(false)
                continue_btn.disable()

            }
            //second
            else if (it==3){
                //activity!!.v_pager.setPagingEnabled(false)
                continue_btn.disable()
            }
            //both not accepted
            else if (it==4){
             //   activity!!.v_pager.setPagingEnabled(false)
                continue_btn.disable()
            }
            //all not accepted
            else if (it==5){
              //  activity!!.v_pager.setPagingEnabled(false)
                continue_btn.disable()
            }
            //all accepted
            else{
               // activity!!.v_pager.setPagingEnabled(false)
                continue_btn.disable()
                isNameEmpty=false
            }
        }
        return isNameEmpty
    }
}
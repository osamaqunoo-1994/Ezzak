package com.aait.getak.ui.fragments.forget

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.aait.getak.R
import com.aait.getak.base.BaseFragment
import com.aait.getak.models.register_model.RegisterModel
import com.aait.getak.network.remote_db.Resource
import com.aait.getak.ui.view_model.RegisterViewModel
import com.aait.getak.utils.Util
import com.aait.getak.utils.toasty
import kotlinx.android.synthetic.main.activity_forget.*
import kotlinx.android.synthetic.main.fragment_phone_forget.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.koin.android.viewmodel.ext.android.viewModel

class PhoneForgetFragment:BaseFragment() {
    private val viewModel: RegisterViewModel by viewModel()

    override val viewId: Int
        get() = R.layout.fragment_phone_forget

    override fun init(view: View) {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        continue_btn.onClick {
            if (Util.checkPhone(etPhone,etPhoneLay,getString(R.string.error_phone))){
                sendRequest()
            }
        }
        setState()

    }

    private fun sendRequest() {
        viewModel.forgetPass(etPhone.text.toString())
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
                        goNext()
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

    private fun goNext() {
        activity!!.v_pager.currentItem=1
    }

}
package com.aait.getak.ui.activities.settings

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.aait.getak.R
import com.aait.getak.base.ParentActivity
import com.aait.getak.models.register_model.RegisterModel
import com.aait.getak.network.remote_db.Resource
import com.aait.getak.utils.toasty
import com.chaos.view.PinView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_verification.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class ActivationPhone : ParentActivity() {
    private var phone: String?=""
    private var iso: String?=""
    override val layoutResource: Int
        get() = R.layout.fragment_verification
    override var states: MutableLiveData<Resource<Any>>? = MutableLiveData()

    override fun init() {
        phone = intent.getStringExtra("phone")
        iso = intent.getStringExtra("iso")
        setActions()
       // setState()
    }
    @SuppressLint("CheckResult")
    private fun setActions() {
        phone_edit.onClick {
            v_pager.currentItem=0
        }

        checkPattern(firstPinView)!!.subscribe {
            if (it){
                sendRequest()
            }
        }

        resendCode_btn.onClick {
            sendRequestResend()
        }
    }

    private fun sendRequestResend() {
        repo.resendCode(mPrefs?.token!!).subscribeWithLoading({
            showProgressDialog()
        },{
            hideProgressDialog()
            if (it.value=="1"){
                mPrefs?.token="Bearer ${it.data!!.token}"
                toasty(getString(R.string.code_sent))
            }
        },{
            hideProgressDialog()
            Log.e("error",it.message!!)
        },{

        })
    }

    //check pattern
    fun checkPattern(pattern: PinView): Observable<Boolean>?{
        return RxTextView.textChanges(pattern)
                .map { inputText -> inputText.trim().isNotBlank() && inputText.trim().length > 3}
                .distinctUntilChanged()
    }

    private fun setState(){
        states!!.observe(this, Observer {
            when{

                it?.status == Resource.Status.SUCCESS -> {
                    hideProgressDialog()
                    val model = it.data as RegisterModel
                    if (model.value=="1") {
                        mPrefs?.storeUser(model.data!!)
                        mPrefs?.token="Bearer ${model.data!!.token}"

                    }
                    else{
                        toasty(model.msg!!,2)
                        Log.e("error",model.msg!!)
                    }
                }
                it?.status== Resource.Status.ERROR ->{
                    hideProgressDialog()
                    toasty(it.message!!,2)
                    Log.e("error",it.message)
                }
                it?.status== Resource.Status.LOADING_FIRST ->{
                    showProgressDialog()
                }
            }
        })
    }

    private fun sendRequest() {
        sendVerification(firstPinView.text.toString(),mPrefs?.token!!)
    }

    fun sendVerification(code:String,header:String){
        addDisposable(repo.activationPhone(code,phone!!,iso!!,header).subscribeWithLoading({
            showProgressDialog()
        },{
            hideProgressDialog()
            if (it.value=="1") {
                mPrefs?.storeUser(it.data!!)
                mPrefs?.token = "Bearer ${it.data?.token}"
                startActivity(Intent(applicationContext, MySettingsActivity::class.java))
                finish()
            }
            else{
                it.msg?.let { it1 -> toasty(it1,2) }
            }

        },{
            hideProgressDialog()
            it.message?.let { it1 -> toasty(it1,2) }
        },{

        })


        )
    }

}
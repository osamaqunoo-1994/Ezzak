package com.aait.getak.ui.activities.auth

import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import com.aait.getak.R

import com.aait.getak.base.ParentActivity
import com.aait.getak.models.register_model.RegisterModel
import com.aait.getak.network.remote_db.Resource
import com.aait.getak.ui.activities.core.MapPreviewActivity
import com.aait.getak.ui.activities.settings.ContactUsActivity
import com.aait.getak.ui.view_model.RegisterViewModel
import com.aait.getak.utils.toasty
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast
import org.koin.android.viewmodel.ext.android.viewModel

class LoginActivity : ParentActivity() {
    private val viewModel: RegisterViewModel by viewModel()
    override val layoutResource: Int
        get() = R.layout.activity_login

    override fun init() {
        setStates()
    }

    private fun setStates() {
        viewModel.states!!.observe(this, Observer {
            when{

                it?.status == Resource.Status.SUCCESS -> {
                    hideProgressDialog()
                    val model = it.data as RegisterModel

                    if (model.value=="1") {
                        if (model.data!!.active=="active") {
                            mPrefs!!.storeUser(model.data!!)
                            mPrefs!!.storeSession(true)
                            mPrefs!!.token = "Bearer "+model.data!!.token
                            goNext()
                        }
                        else if(model.data!!.active=="pending"){
                            mPrefs!!.storeUser(model.data!!)
                            mPrefs!!.token = "Bearer "+model.data!!.token
                            toast(getString(R.string.user_not_active))

                            val intent = Intent(applicationContext, RegisterActivity::class.java)
                            intent.putExtra("fromLogin",true)
                            startActivity(intent)
                        }
                        else{
                            val intent = Intent(applicationContext, ContactUsActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    else{
                        toasty(model.msg!!,2)
                    }
                }
                it?.status== Resource.Status.ERROR ->{
                    hideProgressDialog()
                    toasty(it.message!!,2)
                }
                it?.status== Resource.Status.LOADING_FIRST ->{
                    showProgressDialog()
                }
            }
        })
    }

    private fun goNext() {
        val intent = Intent(this, MapPreviewActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }


    fun onRegister(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    fun onLogin(view: View) {
        viewModel.signIn(etPhone.text.toString(),etPass.text.toString())
    }

    fun onForget(view: View) {
        val intent = Intent(this, ForgetActivity::class.java)
        startActivity(intent)

    }
}


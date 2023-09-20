package com.aait.getak.ui.fragments.auth

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.Observer
import com.aait.getak.R
import com.aait.getak.base.BaseFragment
import com.aait.getak.models.register_model.RegisterModel
import com.aait.getak.network.remote_db.Resource
import com.aait.getak.ui.activities.auth.RegisterActivity
import com.aait.getak.ui.dialogs.BottomSheetCard
import com.aait.getak.ui.view_model.RegisterViewModel
import com.aait.getak.utils.toasty
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_pass.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.koin.android.viewmodel.ext.android.viewModel

class PasswordFragment:BaseFragment() {
    private val viewModel: RegisterViewModel by viewModel()
    override val viewId: Int
        get() = R.layout.fragment_pass

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
        setState()
        checkPass()
        invitation_code_tv.onClick {
            val bottomSheetCard = BottomSheetCard(getString(R.string.enter_invitation_code)){code,btn->
                RegisterActivity.friend_code=code

            }
            bottomSheetCard.show(fragmentManager!!,"asd")
        }
        continue_btn.onClick {
                goNext()
        }
    }

    private fun goNext() {
        RegisterActivity.password=etPass.text.toString()
        activity!!.v_pager.currentItem=4
    }

    @SuppressLint("CheckResult")
    private fun checkPass():Boolean{
        var isPassCorrect=false
        viewModel.checkPass(etPass)!!.subscribe {
            if (!it) {
             //   error_txt_pass.visibility=View.VISIBLE
               // error_txt_pass.text=getString(R.string.check_pass)
                continue_btn.disable()
                /*passFragment*/
            }

            else {
               // error_txt_pass.visibility=View.GONE
                continue_btn.enable()
            }
            isPassCorrect=it
            //activity!!.v_pager.setPagingEnabled(it)
        }
        return isPassCorrect
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
}
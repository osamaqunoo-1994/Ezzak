package com.aait.getak.ui.fragments.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.aait.getak.R
import com.aait.getak.base.BaseFragment
import com.aait.getak.models.countries_model.Country
import com.aait.getak.ui.activities.auth.RegisterActivity
import com.aait.getak.ui.activities.settings.TermsActivity
import com.aait.getak.ui.view_model.RegisterViewModel
import com.aait.getak.utils.Constant
import com.aait.getak.utils.TextDrawable
import com.aait.getak.utils.Util
import com.aait.getak.utils.toasty
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register_phone.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.koin.android.viewmodel.ext.android.viewModel


class PhoneFragment : BaseFragment() {


    private var countryIso: String?=""
    var country_id_: Int?=0
    private  var codes: List<Country> = ArrayList<Country>()

    private val viewModel: RegisterViewModel by viewModel()
    override val viewId: Int
        get() = R.layout.activity_register_phone

    override fun init(view: View) {
        context!!.hideKeyboard(view)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        countryIso=country_code.selectedCountryNameCode
        RegisterActivity.country_iso=countryIso!!
        setViews()
        setActions()
        checkPhone()
    }

    private fun setActions() {
        continue_btn.onClick {
            sendRequest()
        }

        tv_condition_terms.onClick {

            Util.openUrl(context!!,"${Constant.TERMS_URL}")

        }

        country_code.setOnCountryChangeListener {
            countryIso=country_code.selectedCountryNameCode
            RegisterActivity.country_iso=countryIso!!

            etPhone.setCompoundDrawables(
                TextDrawable(etPhone, country_code.selectedCountryCode + " "),
                null,
                null,
                null)
        }
    }


    private fun setViews() {
        continue_btn.disable()
        etPhone.setCompoundDrawables(
            TextDrawable(
                etPhone,
                country_code.selectedCountryCode + " "
            ), null, null, null
        )
    }


    private fun sendRequest() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful) {
                val deviceId=it.result
                addDisposable(repo.sigIn(etPhone.text.toString(),countryIso!!,RegisterActivity.socialId, deviceId = deviceId)
                    .subscribeWithLoading({
                        showProgressDialog()
                    },{
                        hideProgressDialog()
                        if (it.value=="1"){
                            val data1 = it.data
                            mPrefs.storeUser(data1!!)
                            mPrefs.token="Bearer ${data1.token}"
                            if (!it.data?.card_token.isNullOrBlank()) mPrefs.isPaidVisa=true
                            goNext()
                        }
                        else{
                            activity?.toasty(it.msg!!,2)
                        }
                    },{
                        hideProgressDialog()
                        activity!!.toasty(it.message!!,2)
                    },{

                    })
                )
            }
        }

    }



    @SuppressLint("CheckResult")
    private fun checkPhone():Boolean{
        var isCorrect=false
        viewModel.checkPhone(etPhone)!!.subscribe {
            if (!it) {
                continue_btn.disable()

            }
            else {
                error_txt.visibility=View.GONE
                isCorrect=true
                continue_btn.enable()
            }
            //activity!!.v_pager.setPagingEnabled(it)
        }
        return isCorrect
    }

    private fun goNext() {
        Log.e("code",RegisterActivity.country_iso)
        activity!!.v_pager.currentItem=1
    }



}



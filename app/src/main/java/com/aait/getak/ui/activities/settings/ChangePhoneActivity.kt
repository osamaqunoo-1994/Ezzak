package com.aait.getak.ui.activities.settings

import android.annotation.SuppressLint
import android.view.View
import android.widget.EditText
import com.aait.getak.R
import com.aait.getak.base.ParentActivity
import com.aait.getak.models.register_model.RegisterModel
import com.aait.getak.ui.activities.auth.RegisterActivity
import com.aait.getak.utils.TextDrawable
import com.aait.getak.utils.Util
import com.aait.getak.utils.toasty
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_register_phone.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class ChangePhoneActivity : ParentActivity() {
    override val layoutResource: Int
        get() = R.layout.activity_register_phone

    private var countryIso: String?=""

    override fun init() {
        countryIso=country_code.selectedCountryNameCode
        setViews()
        setActions()
        checkPhone()
    }

    @SuppressLint("CheckResult")
    private fun checkPhone():Boolean{
        var isCorrect=false
        checkPhone(etPhone)!!.subscribe {
            if (!it) {
                continue_btn.disable()

            }
            else {
                error_txt.visibility= View.GONE
                isCorrect=true
                continue_btn.enable()
            }
            //activity!!.v_pager.setPagingEnabled(it)
        }
        return isCorrect
    }

    private fun setActions() {
        continue_btn.onClick {
            sendRequest()
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

    private fun sendRequest() {
        mPrefs?.token?.let { repo.editProfile(phone = etPhone.text.toString(),token = mPrefs?.token!!)
            .subscribeWithLoading({showProgressDialog()},{handleData(it)},{handleError(it.message)},{})
        }
    }

    private fun handleData(it: RegisterModel) {
        hideProgressDialog()
        if (it.value=="1") {
            it.data?.let {
                    it1 -> mPrefs?.storeUser(it1)
                    mPrefs?.token="Bearer "+it1.token

            }
            toasty(getString(R.string.updated_successfully))
            finish()
        }
        else{
            it.msg?.let { it1 -> toasty(it1,2) }
        }

    }

    private fun handleError(message: String?) {
        hideProgressDialog()
        message?.let { toasty(it,2) }
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

    fun checkPhone(editText: EditText): Observable<Boolean>? {
        return RxTextView.textChanges(editText)
            .map { inputText -> Util.isPhone(inputText.toString())}
            .distinctUntilChanged()

    }
}
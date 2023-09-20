package com.aait.getak.ui.activities.settings

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.util.Log
import android.view.View
import com.aait.getak.R
import com.aait.getak.base.ParentActivity
import com.aait.getak.models.ResetModel
import com.aait.getak.models.register_model.UserModel
import com.aait.getak.network.remote_db.RetroWeb
import com.aait.getak.ui.activities.splash.SplashActivity
import com.aait.getak.ui.dialogs.ExitDialog
import com.aait.getak.utils.GlobalPreferences
import com.aait.getak.utils.Util
import com.aait.getak.utils.toasty
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_my_settings.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class MySettingsActivity : ParentActivity() {
    private lateinit var myCalendar: Calendar
    override val layoutResource: Int
        get() = R.layout.activity_my_settings

    override fun onResume() {
        super.onResume()
        setData()
    }
    override fun init() {
        setToolbar(getString(R.string.edit))
        setActions()
        mPrefs!!.user!!.registered_social?.let {
            if (it){
                linPass.visibility=View.GONE
            }
        }


    }

    private fun setData() {
        mPrefs?.user!!.apply {
            name_tv.text=firstName
            phone_tv.text=phonekey+""+phone
            if (email!!.isNotBlank()) {
                mail_tv.text = email
            }
            if (!birth_date.isNullOrBlank()) date_tv.text=birth_date
            if (gender == getString(R.string.male)){
                gender_tv.text=getString(R.string.male)
            }
            else{
                gender_tv.text=getString(R.string.female)
            }
        }
    }

    private fun setActions() {
        linName.onClick {
            goHome("name")
        }
        linPhone.onClick {
            startActivity(Intent(applicationContext,ChangePhoneActivity::class.java))
        }
        linGender.onClick {
            showGender(arrayListOf(getString(R.string.male),getString(R.string.female)))
        }
        linMail.onClick {
            goHome("mail")
        }
        linPass.onClick {
            goHome("pass")
        }
        lang_lay.onClick {
            showLang(arrayListOf("English","العربية"))
        }
        rate_lay.onClick {
            goRate()
        }
        linBirth.onClick {
            showDate()
        }
        tv_terms.onClick {
            val intent=Intent(applicationContext,TermsActivity::class.java)
            startActivity(intent)
        }
        log_lay.onClick {
            logOut()
        }
    }

    private fun logOut() {
        log_lay.onClick {
        val dialog = ExitDialog{
            FirebaseMessaging.getInstance().token.addOnCompleteListener {
                if(it.isSuccessful)
                    sendRequestLogout(it.result)

            }
        }
        dialog.show(supportFragmentManager,"logout")
        }

    }

    private fun sendRequestLogout(deviceId:String) {
        RetroWeb.serviceApi.logOut(mPrefs!!.token!!,deviceId).enqueue(object :
            Callback<ResetModel> {
            override fun onFailure(call: Call<ResetModel>, t: Throwable) {
                Log.e("error",t.message!!)
                toast(getString(R.string.error_connection))

            }

            override fun onResponse(call: Call<ResetModel>, response: Response<ResetModel>) {
                if (response.isSuccessful){
                    if (response.body()!!.value!="0"){
                        GlobalPreferences(applicationContext).logout()
                        val intent = Intent(applicationContext, SplashActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        })
    }

    private fun showLang(langs:ArrayList<String>) {

            val dialog = MaterialDialog(this)
            dialog.title(R.string.app_language)

            dialog.positiveButton(res = R.string.confirm)

            dialog.show {
                cornerRadius(16f)
                theme.applyStyle(R.style.AppTheme_Custom,true)
                listItemsSingleChoice(res = R.string.language_settings, items = langs)
                {dialog, indices, item ->
                    if (item.toString()=="English"){
                      updateLang("en")
                  }
                   else{
                      updateLang("ar")
                  }

               }


            }


    }
    private fun showGender(genders:ArrayList<String>) {

            val dialog = MaterialDialog(this)
            dialog.title(R.string.gender)

            dialog.positiveButton(res = R.string.confirm)

            dialog.show {
                cornerRadius(16f)
                theme.applyStyle(R.style.AppTheme_Custom,true)
                listItemsSingleChoice(res = R.string.gender, items = genders)
                {dialog, indices, item ->
                    sendRequestProfile(item.toString())
               }
            }


    }

    private fun sendRequestProfile(gender: String) {
        repo.editProfile(gender=gender).subscribeWithLoading({
            showProgressDialog()
        },{handleData(it.data)},{handleError(it.message)},{})
    }

    private fun handleData(data: UserModel?) {
        hideProgressDialog()
        if (data != null) {
            mPrefs?.storeUser(data)
            mPrefs?.token="Bearer "+data.token
            if (data.gender == getString(R.string.male)){
                gender_tv.text=getString(R.string.male)
            }
            else{
                gender_tv.text=getString(R.string.female)
            }
            toasty(getString(R.string.updated_successfully))
        }

    }

    private fun handleError(message: String?) {
        hideProgressDialog()
        message?.let { toasty(it,2) }
    }

    private fun updateLang(lang: String) {
        mPrefs!!.storeLang(lang)
        Util.changeLang(lang, applicationContext)
        val intent = Intent(this, SplashActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    private fun goRate() {
        Util.rateApp(this)
    }

    private fun showDate() {
         myCalendar = Calendar.getInstance();

        val date =
            OnDateSetListener { view, year, monthOfYear, dayOfMonth -> // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDate()
            }
        DatePickerDialog(this, date, myCalendar
                 .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun updateDate() {
        val myFormat = "dd-MM-yyyy" //In which you need put here

        val sdf = SimpleDateFormat(myFormat, Locale.US)
        val format = sdf.format(myCalendar.time)
        Log.e("date",format)
        sendRequestDate(format)

    }
    private fun sendRequestDate(date: String) {
        repo.editProfile(birh_date= date).subscribeWithLoading({
            showProgressDialog()
        },{handleData(it.data)},{handleError(it.message)},{})
    }



    fun goHome(key:String){
        val intent=Intent(applicationContext,EditSettingsActivity::class.java)
        intent.putExtra("key",key)
        startActivity(intent)
    }
}

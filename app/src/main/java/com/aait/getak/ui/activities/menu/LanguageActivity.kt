package com.aait.getak.ui.activities.menu

import android.content.Intent
import androidx.core.content.ContextCompat
import com.aait.getak.R
import com.aait.getak.base.ParentActivity
import com.aait.getak.ui.activities.splash.SplashActivity
import com.aait.getak.utils.Util
import kotlinx.android.synthetic.main.activity_language.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.sdk27.coroutines.onClick

class LanguageActivity : ParentActivity() {
    override val layoutResource: Int
        get() = R.layout.activity_language

    override fun init() {
        setToolbar(getString(R.string.language))
        if (Util.language=="ar"){
            ar_lay.backgroundColor=ContextCompat.getColor(this,R.color.colorAccent)
        }
        else{
            en_lay.backgroundColor=ContextCompat.getColor(this,R.color.colorAccent)
        }

        ar_lay.onClick {
            ar_lay.backgroundColor=ContextCompat.getColor(applicationContext,R.color.colorAccent)
            ar_lay.backgroundColor=ContextCompat.getColor(applicationContext,android.R.color.white)
            updateLang("ar")
        }
        en_lay.onClick {
            en_lay.backgroundColor=ContextCompat.getColor(applicationContext,R.color.colorAccent)
            en_lay.backgroundColor=ContextCompat.getColor(applicationContext,android.R.color.white)
            updateLang("en")
        }



    }

    private fun updateLang(lang: String) {
        mPrefs!!.storeLang(lang)
        Util.changeLang(lang, applicationContext)
        val intent = Intent(this, SplashActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }


}

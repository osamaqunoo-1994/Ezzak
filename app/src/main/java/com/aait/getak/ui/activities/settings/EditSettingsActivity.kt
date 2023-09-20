package com.aait.getak.ui.activities.settings

import androidx.fragment.app.Fragment
import com.aait.getak.R
import com.aait.getak.base.ParentActivity
import com.aait.getak.ui.fragments.auth.*

class EditSettingsActivity : ParentActivity() {
    override val layoutResource: Int
        get() = R.layout.activity_edit_settings

    override fun init() {
        val key=intent.getStringExtra("key")
        when(key){
            "name"->addFragment(NameFragment(),key)
            //"phone"->addFragment(PhoneFragment(),key)
            "pass"->addFragment(EditPassFragment(),key)
            "mail"->addFragment(MailFragment(),key)
        }
    }

    private fun addFragment(fragment:Fragment,key:String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, fragment)
            .commit()

    }




}

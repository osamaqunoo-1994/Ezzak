package com.aait.getak.ui.view_pager

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.aait.getak.ui.fragments.auth.*

class RegisterViewPager (val context: Context, fm: FragmentManager, val phoneFragment: PhoneFragment, val verFragment: VerificationFragment, val nameFragment: NameFragment, val passFragment: PasswordFragment,  val mailFragment: MailFragment) : FragmentStatePagerAdapter(fm) {

    override fun getCount(): Int {
        return 5
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> phoneFragment
            1 -> verFragment
            2 -> mailFragment
            3 ->  passFragment
            else ->  nameFragment
        }
    }


}
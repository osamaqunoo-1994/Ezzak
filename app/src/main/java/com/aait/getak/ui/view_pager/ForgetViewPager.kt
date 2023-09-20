package com.aait.getak.ui.view_pager

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.aait.getak.ui.fragments.forget.ForgetVerifFragment
import com.aait.getak.ui.fragments.forget.NewPassFragment
import com.aait.getak.ui.fragments.forget.PhoneForgetFragment

class ForgetViewPager (val context: Context, val fm: FragmentManager, val phoneFragment: PhoneForgetFragment, val forgetVerifFragment: ForgetVerifFragment, val newPassFragment: NewPassFragment) : FragmentStatePagerAdapter(fm) {

    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> phoneFragment
            1 -> forgetVerifFragment
            else -> newPassFragment
        }
    }



}
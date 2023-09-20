package com.aait.getak.ui.activities.auth

import android.annotation.SuppressLint
import android.view.animation.ScaleAnimation
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.aait.getak.R
import com.aait.getak.base.ParentActivity
import com.aait.getak.fcm.MessageService
import com.aait.getak.ui.fragments.auth.*
import com.aait.getak.ui.view_model.RegisterViewModel
import com.aait.getak.ui.view_pager.RegisterViewPager
import kotlinx.android.synthetic.main.activity_register.*
import org.koin.android.viewmodel.ext.android.viewModel


class RegisterActivity : ParentActivity(), ViewPager.OnPageChangeListener {

    private val viewModel: RegisterViewModel by viewModel()

    @SuppressLint("CheckResult")
    override fun onPageScrollStateChanged(state: Int) {

    }


    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        addDots(position)
    }

    override val layoutResource: Int
        get() = R.layout.activity_register

    override fun init() {
        v_pager.setPagingEnabled(false)
        setViewPager()
        addDots(0)
        if(intent.getBooleanExtra("fromLogin",false)){
            v_pager.currentItem=5
        }
    }

    companion object{
        var phone=""
        var name=""
        var friend_code=""
        var email=""
        var country_iso=""
        var password=""
        var socialId:String?=null

    }

    private fun addDots(position: Int){
        var list = arrayOfNulls<TextView>(5)
        dots_layout.removeAllViews()
        repeat(list.size){
            list[it]=(TextView(this))
            with(list[it]){
                this?.text= android.text.Html.fromHtml("&#8226;")
                this?.textSize=40f
                this?.setTextColor(resources.getColor(R.color.colorAccent))
                dots_layout.addView(this)
            }
        }

        if (list.isNotEmpty()) {
            list[position]?.setTextColor(resources.getColor(android.R.color.darker_gray))
            val scaleAnimation = ScaleAnimation(0.7f, 1.1f, 0.7f, 1.1f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f)
            scaleAnimation.duration = 200
            list[position]?.startAnimation(scaleAnimation)
        }
    }

    private fun setViewPager() {
        val adapter = RegisterViewPager(this,supportFragmentManager,
            PhoneFragment(),
            VerificationFragment(),
            NameFragment(),
            PasswordFragment(),
            MailFragment()
        )
        v_pager.adapter=adapter
        v_pager.offscreenPageLimit=5
        v_pager.addOnPageChangeListener(this)


    }

    override fun onBackPressed() {

        if (v_pager.currentItem==0 || intent.getBooleanExtra("fromLogin",false)){
        super.onBackPressed()
        }
        else{
            v_pager.currentItem=v_pager.currentItem-1
        }

    }

  
}

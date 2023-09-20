package com.aait.getak.ui.activities.auth

import android.annotation.SuppressLint
import android.view.View
import android.view.animation.ScaleAnimation
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.aait.getak.R
import com.aait.getak.base.ParentActivity
import com.aait.getak.ui.fragments.forget.ForgetVerifFragment
import com.aait.getak.ui.fragments.forget.NewPassFragment
import com.aait.getak.ui.fragments.forget.PhoneForgetFragment
import com.aait.getak.ui.view_model.RegisterViewModel
import com.aait.getak.ui.view_pager.ForgetViewPager
import kotlinx.android.synthetic.main.activity_forget.dots_layout
import kotlinx.android.synthetic.main.activity_forget.v_pager
import kotlinx.android.synthetic.main.forget_verif.*
import kotlinx.android.synthetic.main.fragment_pass.*
import kotlinx.android.synthetic.main.fragment_phone_forget.*
import org.koin.android.viewmodel.ext.android.viewModel

class ForgetActivity : ParentActivity(), ViewPager.OnPageChangeListener {
    private val viewModel: RegisterViewModel by viewModel()
    override fun onPageScrollStateChanged(state: Int) {
        when (v_pager.currentItem) {
            //phone
            0 -> {
                checkPhone()
            }
          //  1-> checkPattern()


        }
    }

    @SuppressLint("CheckResult")
    private fun checkPattern() {
        viewModel.checkPattern(firstPinView)!!.subscribe {
            if (!it) {
                error_txt_pin.visibility= View.VISIBLE
                error_txt_pin.text=getString(R.string.check_phone)
            }
            else {
                error_txt_pin.visibility= View.GONE
            }
            v_pager.setPagingEnabled(false)
        }
    }


    @SuppressLint("CheckResult")
    private fun checkPhone() {
        viewModel.checkPhone(etPhone)!!.subscribe {
            if (!it) {
                error_txt.visibility= View.VISIBLE
                error_txt.text=getString(R.string.check_phone)
            }
            else {
                error_txt.visibility= View.GONE
            }
            v_pager.setPagingEnabled(false)
        }
    }

    @SuppressLint("CheckResult")
    private fun checkPass() {
        viewModel.checkPass(etPass)!!.subscribe {
            if (!it) {
                error_txt_pass.visibility=View.VISIBLE
                error_txt_pass.text=getString(R.string.check_pass)
            }

            else {
                error_txt_pass.visibility=View.GONE
            }

        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        addDots(position)
    }

    override val layoutResource: Int
        get() = R.layout.activity_forget

    override fun init() {
        addDots(0)
        setViewPager()


    }

    fun addDots(position: Int){
        var list = arrayOfNulls<TextView>(3)
        //create 3 dots
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
        val adapter = ForgetViewPager(this,supportFragmentManager,
            PhoneForgetFragment(),
            ForgetVerifFragment(),
            NewPassFragment()
        )
        v_pager.adapter=adapter
        v_pager.offscreenPageLimit=3
        v_pager.addOnPageChangeListener(this)
    }

    override fun onBackPressed() {
        if (v_pager.currentItem>0){
            v_pager.currentItem=v_pager.currentItem -1
        }
        else {
            super.onBackPressed()
        }
    }
}

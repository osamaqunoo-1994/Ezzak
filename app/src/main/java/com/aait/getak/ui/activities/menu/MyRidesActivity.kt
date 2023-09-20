package com.aait.getak.ui.activities.menu

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.aait.getak.R
import com.aait.getak.base.ParentActivity
import com.aait.getak.ui.fragments.bottom_nav.MyRecords
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.my_records.*

class MyRidesActivity : ParentActivity() {

    private lateinit var previousTab: TabLayout.Tab
    private lateinit var schedTab: TabLayout.Tab

    override val layoutResource: Int
        get() = R.layout.activity_recent_trips

    override fun init() {
        setToolbar(getString(R.string.my_trips))
        previousTab = tabLayout.newTab().setText(resources.getString(R.string.previous))
        schedTab = tabLayout.newTab().setText(resources.getString(R.string.scheduled))

        setupTabLayout()

        if (intent.getIntExtra("order_id",0)==0){
            //  tabLayout.getTabAt(1)?.select()
            tabLayout.selectTab(previousTab,true)
            val fragment = MyRecords()
            val bundle = Bundle()
            fragment.arguments = bundle
            replaceFragment(fragment,"")
        }
        else{
            val fragment = MyRecords()
            val bundle = Bundle()

            bundle.putBoolean("isFuture",true)
            //tabLayout.getTabAt(0)?.select()
            fragment.arguments = bundle
            tabLayout.selectTab(previousTab,true)
            replaceFragment(MyRecords(),"")
        }


    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun showPopUpPrice(anchorView: View) {
        val popup = PopupWindow(this)
        val layout = layoutInflater.inflate(R.layout.alert_pop_up, null)
        popup.contentView = layout
        // Set content width and height
        val okBtn = layout.findViewById(R.id.ok_btn) as TextView

        okBtn.setOnClickListener {
            popup.dismiss()
        }
        popup.height = WindowManager.LayoutParams.WRAP_CONTENT
        popup.width = WindowManager.LayoutParams.WRAP_CONTENT
        // Closes the popup window when touch outside of it - when looses focus
        popup.isOutsideTouchable = true
        popup.isFocusable = true
        // Show anchored to button
        popup.setBackgroundDrawable(BitmapDrawable())
        if (mPrefs!!.lang=="ar") {
            popup.showAsDropDown(anchorView, Gravity.LEFT, 0, anchorView.height)
        }
        else{
            popup.showAsDropDown(anchorView, Gravity.RIGHT, 0, anchorView.height)
        }
        // add flag
        var container: View? = null
        if (android.os.Build.VERSION.SDK_INT > 22) {
            container = popup.contentView.parent.parent as View
        } else {
            container = popup.contentView.parent.parent as View
        }
        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        val p = container.layoutParams as WindowManager.LayoutParams

        p.flags = p.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
        p.dimAmount = 0.5f
        wm.updateViewLayout(container, p)



    }

    private fun setupTabLayout() {
        tabLayout.addTab(previousTab)
        tabLayout.addTab(schedTab)


        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            @RequiresApi(Build.VERSION_CODES.KITKAT)
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab!!.position==0) {

                    val fragment = MyRecords()
                    val bundle = Bundle()

                    fragment.arguments = bundle
                    replaceFragment(fragment, "")
                }
                else{
                    val fragment = MyRecords()
                    val bundle = Bundle()
                    bundle.putBoolean("isFuture",true)
                    fragment.arguments = bundle
                    replaceFragment(fragment,"")
                }
            }

        })
    }

    fun replaceFragment(fragment: Fragment) {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.frame, fragment)
        ft.addToBackStack(null)
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ft.commit()
    }
    fun replaceFragment(fragment: Fragment, home:String) {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.frame, fragment)
        //ft.addToBackStack(null)
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ft.commit()
    }



}

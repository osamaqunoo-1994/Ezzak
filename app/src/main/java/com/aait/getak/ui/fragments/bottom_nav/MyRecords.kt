package com.aait.getak.ui.fragments.bottom_nav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.aait.getak.R
import com.aait.getak.base.BaseFragment
import com.aait.getak.ui.fragments.trips.NestedRecordFragment

import kotlinx.android.synthetic.main.my_records.*

class MyRecords : BaseFragment(), AdapterView.OnItemSelectedListener {
    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    private var isFuture: Boolean?=false
    var year=""
    var month=0
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {

        if (tabLayout!=null) {
            this.month = tabLayout.selectedTabPosition
        }
        val fragment =
            NestedRecordFragment()
        val bundle = Bundle()
        bundle.putString("year", year)
        bundle.putString("month",this.month.toString())
        fragment.arguments=bundle
        replaceFragment(fragment)
    }


    fun replaceFragment(fragment: Fragment) {
        val fm = activity!!.supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.frame, fragment)
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ft.commit()
    }

    override val viewId: Int
        get() = R.layout.my_records


    override fun init(view: View) {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val fragment =
            NestedRecordFragment()
        val bundle = Bundle()
        bundle.putBoolean("isFuture", isFuture!!)
        fragment.arguments=bundle
        replaceFragment(fragment)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isFuture = arguments?.getBoolean("isFuture")
        return super.onCreateView(inflater, container, savedInstanceState)
    }



    override fun onDestroyView() {
        super.onDestroyView()
      //  activity!!.sp_years.visibility=View.GONE
       // activity!!.title_toolbar.visibility=View.VISIBLE
    }
}
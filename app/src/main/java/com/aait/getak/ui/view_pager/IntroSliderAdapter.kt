package com.aait.getak.ui.view_pager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.aait.getak.R

class IntroSliderAdapter(val context: Context) : PagerAdapter() {

    val drawables = arrayOf(R.mipmap.intro_one,R.mipmap.intro_two, R.mipmap.intro_tree)
    val titles = arrayOf(context.getString(R.string.start_journey),context.getString(R.string.transport_operation),context.getString(R.string.operation_type))
    val descs = arrayOf(context.getString(R.string.src_dist_point),context.getString(R.string.transport_package),context.getString(R.string.choose_offer))


    override fun getCount(): Int {
        return drawables.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view==obj as RelativeLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.slider_layout, container, false)
        val imageView = view.findViewById<ImageView>(R.id.src_img)
        val desc = view.findViewById<TextView>(R.id.desc)
        val title = view.findViewById<TextView>(R.id.title)
        imageView.setImageResource(drawables[position])
        desc?.text=descs[position]
        title?.text=titles[position]
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeAllViews()
    }
}
/*
package com.aait.getak.ui.view_pager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.aait.getak.R
import com.aait.getak.utils.RoundedCornersTransformation
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import org.jetbrains.anko.find
import java.lang.Exception

class ImageViewPager (val context: Context) : PagerAdapter() {
    private var imgs: MutableList<String> = ArrayList<String>()

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view==obj as RelativeLayout
    }
     fun setSwap( imgs:MutableList<String>){
        this.imgs=imgs
    }
    override fun getCount(): Int {
        return imgs.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.slider_layout, container, false)
         //val img = view.findViewById<ImageView>(R.id.img_prog)
        var progress = view.find<ProgressBar>(R.id.load_prog)
        progress.visibility = View.VISIBLE

        Picasso.get().load(imgs[position]).transform(RoundedCornersTransformation(64,0)).into(img, object : Callback {
            override fun onSuccess() {
                progress.visibility = View.INVISIBLE
            }

            override fun onError(e: Exception?) {
                progress.visibility = View.INVISIBLE
            }
        })
         container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeAllViews()
    }



}*/

package com.aait.getak.ui.fragments.bottom_nav

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.TranslateAnimation
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.aait.getak.R
import com.aait.getak.base.BaseFragment
import com.aait.getak.ui.activities.map.AddressDetailsActivity
import com.aait.getak.ui.adapters.MySliderAdapter
import com.aait.getak.utils.isGpsEnabled
import com.aait.getak.utils.showGpsAlert
import com.aait.getak.utils.toasty
import kotlinx.android.synthetic.main.main_fragment.*
import org.jetbrains.anko.sdk27.coroutines.onClick


class HomeFragment : BaseFragment() {

companion object{
    var serviceIn:String="mycity"
    var serviceType:String="goods"
}


    private lateinit var adapter: MySliderAdapter
    override val viewId: Int
        get() = R.layout.main_fragment


    override fun init(view: View) {

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = MySliderAdapter()
        sliderView_.sliderAdapter = adapter
        sendRequest()
        setChooseAction(indoors,outdoors)
        checkChoosen()
        indoors.onClick {
            if (serviceIn!="mycity") {
                val anim = TranslateAnimation(outdoors.width.toFloat(), 0F, 0F, 0F)
                anim.duration = 200
                indoors.startAnimation(anim)
            }
            serviceIn="mycity"
            setChooseAction(indoors,outdoors)
        }
        outdoors.onClick {
            if (serviceIn!="between_cities") {
                val anim = TranslateAnimation(-indoors.width.toFloat(), 0F, 0F, 0F)
                anim.duration = 200
                outdoors.startAnimation(anim)
            }
            serviceIn="between_cities"
            setChooseAction(outdoors,indoors)
        }
        package_.onClick {
            setPermissionsLocation {
                if (it) {
                    if (activity!!.isGpsEnabled()) {
                        serviceType = "goods"
                        val intent = Intent(activity, AddressDetailsActivity::class.java)
                        startActivity(intent)
                    }
                    else{
                        activity!!.supportFragmentManager.showGpsAlert(activity!!)
                    }
                }
            }

        }
        members.onClick {
            setPermissionsLocation {
                if (it) {
                    if (activity!!.isGpsEnabled()) {
                        serviceType = "people"
                        val intent = Intent(activity, AddressDetailsActivity::class.java)
                        intent.putExtra("order_id", 0)
                        startActivity(intent)
                    }
                    else{
                        activity!!.supportFragmentManager.showGpsAlert(activity!!)
                    }
                }

            }

        }
    }

    private fun checkChoosen() {
        serviceIn = if (indoors.background != null){
            "mycity"
        }
        else{
            "between_cities"
        }
    }


    private fun prepareSlider(images:MutableList<String>) {
        adapter.swapData(images)

        /*if (images.size > 0) {
            for (image in images) {
                val sliderView = TextSliderView(activity!!)
                sliderView.image(image).scaleType = BaseSliderView.ScaleType.FitCenterCrop
                    *//*.setOnSliderClickListener(this)*//*
                adapter.addSlider(sliderView)
            }

            sliderViewLay.setPresetTransformer(SliderLayout.Transformer.Default)

            sliderViewLay.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom)
            sliderViewLay.setCustomAnimation(DescriptionAnimation())
            sliderViewLay.setDuration(6000)
                //sliderViewLay.addOnPageChangeListener(this)
        }*/
    }

    private fun sendRequest() {
        addDisposable(repo.getAds()
            .subscribeWithLoading({
                if (it.value=="1"){
                    val data = it.data.ads
                    val imgs = data.map {
                        it.image!!
                    }
                   // setImageViewPager(imgs.toMutableList())

                    prepareSlider(imgs.toMutableList())

                }
                else{
                    activity!!.toasty(it.msg!!,2)
                    }
                },{
                activity!!.toasty(getString(R.string.error_connection),2)
                Log.e("error",it.message!!)
                })
        )
    }


    private fun setChooseAction(text_selected: TextView, text_normal: TextView){
        text_selected.background=ContextCompat.getDrawable(activity!!,R.drawable.selected_bg)
        text_selected.setTextColor(ContextCompat.getColorStateList(activity!!,android.R.color.white))

        text_normal.setTextColor(ContextCompat.getColorStateList(activity!!,android.R.color.darker_gray))
        text_normal.background=null
    }
}
package com.aait.getak.ui.activities.food

import androidx.recyclerview.widget.LinearLayoutManager
import com.aait.getak.R
import com.aait.getak.base.ParentActivity
import com.aait.getak.listeners.CartListener
import com.aait.getak.models.Place
import com.aait.getak.models.store_details_model.Data
import com.aait.getak.models.store_details_model.MenucategoryX
import com.aait.getak.models.store_details_model.ProductX
import com.aait.getak.ui.adapters.ItemFoodAdapter
import com.aait.getak.utils.toasty
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_restaurant_details.*
import org.jetbrains.anko.textColor

class RestaurantDetailsActivity : ParentActivity(), CartListener {
    private var menuCats: List<MenucategoryX>? = null

    private val mAdapter = ItemFoodAdapter(this)
    override val layoutResource: Int
        get() = R.layout.activity_restaurant_details

    override fun init() {
        setRec()
        sendRequest()
        setupTabListeners()
    }

    private fun setupTabListeners() {
        tabLayout_cat.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (menuCats?.isNotEmpty()==true) {
                    menuCats!![tab?.position!!].products?.let { mAdapter.swapData(it) }

                }
            }

        })
    }

    private fun setRec() {
        rec_food.layoutManager=LinearLayoutManager(applicationContext)
        rec_food.adapter=mAdapter
    }

    private fun sendRequest() {
        val place = intent.getSerializableExtra("restaurant") as Place
        repo.storeDetails(place.id.toString(),RestaurantsActivity.userLat.toString(),RestaurantsActivity.userLong.toString())
            .subscribeWithLoading({showProgressDialog()},{handleData(it.data)},{handleError(it.message)},{})
    }

    private fun handleError(message: String?) {
        hideProgressDialog()
        message?.let { toasty(it,2) }
    }

    private fun handleData(data: Data?) {
        hideProgressDialog()
        menuCats = data?.store?.menucategories
        tv_name.text=data?.store?.name
        rest_rate.rating=data?.store?.rate!!.toFloat()
        if (data.store.place_open!!){
            tv_status.textColor=android.R.color.holo_green_light
            tv_status.text=getString(R.string.open)
        }
        else{
            tv_status.textColor=R.color.red
            tv_status.text=getString(R.string.close)
        }
        setTabLayout(data?.store?.menucategories?.map {
            it.products
            it.name } as List<String>)


    }
    private fun setTabLayout(catsName: List<String>) {
        catsName.mapIndexed { index, cat->
            var selected=false
            if (index==0) {
                selected=true
                menuCats!![0].products?.let { mAdapter.swapData(it) }

            }
            tabLayout_cat.addTab(tabLayout_cat.newTab().setText(cat),selected)
        }



    }

    override fun onPlus(count: Int, current_count: Float, product: ProductX) {

    }

    override fun onMinus(count: Int, current_count: Float, product: ProductX) {

    }

}
package com.aait.getak.ui.adapters

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.aait.getak.R
import com.aait.getak.models.Place
import com.aait.getak.ui.activities.food.OrderActivity
import com.aait.getak.ui.activities.food.RestaurantDetailsActivity
import com.aait.getak.utils.StringsHelper
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_subcat_search_adapter.view.*
import java.util.*

class StoresAdapter(val fromSearch:Boolean?=false) : RecyclerView.Adapter<StoresAdapter.StoresVH>() {

    private var data: List<Place> = ArrayList()
    private var key:String?=""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoresVH {

        return StoresVH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_subcat_search_adapter, parent, false)
        )

    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: StoresVH, position: Int) = holder.bind(data[position],key)

    fun swapData(
        data: MutableList<Place>,
        key:String?="") {
        this.data = data
        this.key=key
        notifyDataSetChanged()
    }

    class StoresVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            item: Place,
            key: String?
        ) = with(itemView) {
            Glide.with(context).load(item.icon).into(iv_item)
            tv_name.text =
                StringsHelper.highlightLCS(item.name!!, key!!,ContextCompat.getColor(context, R.color.colorYello))
            /*tv_name.text=item.title*/
            tv_address.text=item.vicinity ?: item.address
            tv_dist.text=item.distance.toString()
            setOnClickListener {
                if (item.id==0) {
                    context.startActivity(Intent(context, OrderActivity::class.java).apply {
                        putExtra("restaurant", item)
                    })
                }
                else{
                    context.startActivity(Intent(context, RestaurantDetailsActivity::class.java).apply {
                        putExtra("restaurant", item)
                    })
                }

            }
        }
    }
}
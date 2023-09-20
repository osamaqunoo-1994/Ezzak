package com.aait.getak.ui.adapters

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aait.getak.R
import com.aait.getak.models.nearest_trip_model.Data
import com.aait.getak.ui.activities.map.AddressDetailsActivity
import com.aait.getak.ui.fragments.bottom_nav.HomeFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_ride_rec.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*

class RidesAdapter(val activity: Activity,val onClick:(case:Int,item:Data)->Unit) : RecyclerView.Adapter<RidesAdapter.MyRidesViewHolder>() {

    private var data: List<Data> = ArrayList()
    private var isRecent:Boolean?=true
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRidesViewHolder {
        return MyRidesViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_ride_rec, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: MyRidesViewHolder, position: Int) =
        holder.bind(data[position],activity,onClick,isRecent)

    fun swapData(data: List<Data>,isRecent:Boolean?=true) {
        this.data = data
        this.isRecent=isRecent
        notifyDataSetChanged()
    }

    class MyRidesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            item: Data,
            activity: Activity,
            onClick: (case: Int,item:Data) -> Unit,
            recent: Boolean?
        ) = with(itemView) {
            tv_captin_name.text=item.name
            Picasso.get().load(item.avatar).into(iv_captin)
            from_loc.text=item.startAddress
            to_loc.text=item.endAddress
1
            join_btn.onClick {
                if (recent!!){
                HomeFragment.serviceType = item.serviceType!!
                HomeFragment.serviceIn = item.serviceIn!!
                val intent=Intent(context, AddressDetailsActivity::class.java)
                intent.putExtra("order_id",item.id)
                intent.putExtra("car_type_id",item.carTypeId)
                intent.putExtra("isJoin",true)
                context.startActivity(intent)
            }
                else{
                    onClick(1,item)
                }

            }
            details_btn.onClick {
                val intent = Intent(context, AddressDetailsActivity::class.java)
                intent.putExtra("trip",item)
                val options = ActivityOptions.makeSceneTransitionAnimation(
                    activity,
                    Pair.create(iv_captin, "img")
                )

                context.startActivity(intent,options.toBundle())
            }
            setOnClickListener {

            }
        }
    }
}
package com.aait.getak.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aait.getak.R
import com.aait.getak.models.notifications_api_model.Notification
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_system_msg.view.*
import java.util.ArrayList


class NotificationsAdapter(val onRemove:(pos:Int, item: Notification)->Unit, val onClick:(tripId:Int)->Unit) : RecyclerView.Adapter<NotificationsAdapter.SystemVH>() {

    private var data: MutableList<Notification> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SystemVH {
        return SystemVH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_system_msg, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: SystemVH, position: Int) {
        holder.bind(data[position])
        holder.itemView.iv_remove.setOnClickListener {
            onRemove(position,data[position])
        }
        holder.itemView.setOnClickListener {
            if (!data[position].data?.order_id.isNullOrEmpty()) {
                onClick(data[position].data?.order_id!!.toInt())
            }
        }
    }

    fun swapData(data: List<Notification>) {
        this.data = data as MutableList<Notification>
        notifyDataSetChanged()
    }
    fun removeItem(index:Int,item: Notification){
        data.remove(item)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index,itemCount)
    }

    class SystemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Notification) = with(itemView) {
            Picasso.get().load(item.image).into(iv_logo)
            tv_title.text=item.text
            /*setOnClickListener {
                    context.startActivity(Intent(context,MainActivity::class.java).apply {
                        action="trip"
                        putExtra("trip_id", item.data?.order_id?.toInt())
                    })

            }*/
        }
    }
}
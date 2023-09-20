package com.aait.getak.ui.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aait.getak.R
import com.aait.getak.models.notification_model.Data
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.frament_notifcation.view.*

class NotificationAdapter(val activity: Activity, val onDelete:(id:Int, pos:Int)->Unit,val onClickItem:(item:Data)->Unit) : RecyclerView.Adapter<NotificationAdapter.NotificationVH>() {

    private var data = mutableListOf<Data>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationVH {
        return NotificationVH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.frament_notifcation, parent, false)
        )
    }

    fun onDeleteUi(position: Int) {
        data.removeAt(position)
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: NotificationVH, position: Int) =
        holder.bind(data[position],onClickItem, onDelete, position, activity)

    fun swapData(data: List<Data>) {
        this.data = data as MutableList<Data>
        notifyDataSetChanged()
    }

    class NotificationVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            item: Data,
            onClickItem: (item: Data) -> Unit,
            onDelete: (id: Int, pos: Int) -> Unit,
            pos: Int,
            activity: Activity
        ) = with(itemView) {
            title.text = item.title
            details.text = item.desc
            date.text = item.date

            Picasso.get().load(item.img).into(img_disp)
            setOnClickListener {
                onClickItem(item)
            }


        }

    }
}
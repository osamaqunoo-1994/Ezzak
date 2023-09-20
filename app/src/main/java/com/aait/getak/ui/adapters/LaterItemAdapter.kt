package com.aait.getak.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.aait.getak.R
import com.aait.getak.models.client_later_model.Order
import kotlinx.android.synthetic.main.item_later_ride.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*

class LaterItemAdapter(val onCancelTrip:(order:Order,btn:Button)->Unit,val onConfirmTrip:(order:Order)->Unit) : RecyclerView.Adapter<LaterItemAdapter.MyViewHolder>() {

    private var data: List<Order> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_later_ride, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) = holder.bind(data[position],onCancelTrip,onConfirmTrip)

    fun swapData(data: List<Order>) {
        this.data = data
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            item: Order,
            onCancelTrip: (order: Order,btn:Button) -> Unit,
            onConfirmTrip: (order: Order) -> Unit
        ) = with(itemView) {
            time.text=item.date
            address.text=item.endAddress
            if (!item.canUpdate!!){
                edit_btn.visibility=View.GONE
            }
            else{
                edit_btn.visibility=View.VISIBLE
            }

            edit_btn.onClick {
                onConfirmTrip(item)
            }

            cancel_btn.onClick {
                onCancelTrip(item,cancel_btn)
            }

            setOnClickListener {

            }
        }
    }
}
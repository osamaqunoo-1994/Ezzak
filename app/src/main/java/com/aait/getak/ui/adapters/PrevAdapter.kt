package com.aait.getak.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aait.getak.R
import com.aait.getak.models.prev_trip_model.Data
import com.aait.getak.ui.activities.core.DetailsActivity
import com.aait.getak.ui.fragments.trips.TripDetailsActivity

import kotlinx.android.synthetic.main.prev_trip_item.view.*
import java.util.*

class PrevAdapter : RecyclerView.Adapter<PrevAdapter.PrevTripVH>() {

    private var data: List<Data> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrevTripVH {
        return PrevTripVH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.prev_trip_item, parent,false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: PrevTripVH, position: Int) = holder.bind(data[position])

    fun swapData(data: List<Data>) {
        this.data = data
        notifyDataSetChanged()
    }

    class PrevTripVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Data) = with(itemView) {
            from_loc.text=item.startAddress
            to_loc.text=item.endAddress
            if (item.price!="0") {
                price.visibility=View.VISIBLE
                price.text = item.price.plus(item.currency)
            }
            else{
                price.visibility=View.GONE
            }
            if (item.date?.trim()=="now"||item.date?.trim()=="الأن"){
                date.text=item.date
            }
            else{
                date.text=item.date.plus(" ")+item.time
            }

            setOnClickListener {
                if (item.later) {
                    val intent = Intent(context, TripDetailsActivity::class.java)
                    intent.putExtra("order_id", item.id)
                    context.startActivity(intent)
                }
                else{
                    val intent = Intent(context, DetailsActivity::class.java)
                    intent.putExtra("order_id", item.id)
                    context.startActivity(intent)
                }
            }
        }
    }
}
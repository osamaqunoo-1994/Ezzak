package com.aait.getak.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aait.getak.R
import com.aait.getak.models.payment.PaymentWaysData
import com.aait.getak.models.prev_trip_model.Data
import com.aait.getak.ui.activities.core.DetailsActivity
import com.aait.getak.ui.fragments.trips.TripDetailsActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_payment_ways.view.*
import java.util.ArrayList

class PaymentWaysAdapter(val context:Context,var data: List<PaymentWaysData>,val onItemClick:(type:String)->Unit) : RecyclerView.Adapter<PaymentWaysAdapter.PaymentWaysVH>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentWaysVH {
        return PaymentWaysVH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_payment_ways, parent,false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: PaymentWaysVH, position: Int) = holder.bind(data[position])


    inner class PaymentWaysVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: PaymentWaysData) = with(itemView) {
            Glide.with(context).load(item.image).into(iv_card)
            tv_card.text = item.title
            setOnClickListener {
                item.type?.let { it1 -> onItemClick.invoke(it1) }
            }
        }
    }
}
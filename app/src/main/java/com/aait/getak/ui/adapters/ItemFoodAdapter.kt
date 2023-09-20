package com.aait.getak.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aait.getak.R
import com.aait.getak.listeners.CartListener
import com.aait.getak.models.store_details_model.ProductX
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.food_item.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*

class ItemFoodAdapter(val cartListener: CartListener)  : RecyclerView.Adapter<ItemFoodAdapter.ItemFoodVH>() {

    private var data: MutableList<ProductX> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemFoodVH {
        return ItemFoodVH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.food_item, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ItemFoodVH, position: Int) {

        val item = data[position]
        holder.itemView.iv_plus.onClick {
            var quant=holder.itemView.tv_quant.text.toString().toInt()
            quant++
            holder.itemView.tv_quant.text=quant.toString()
            cartListener.onPlus(holder.itemView.tv_quant.text.toString().toInt(),item.price!!.toFloat(),item)

        }
        holder.itemView.iv_min.onClick {
            var quant=holder.itemView.tv_quant.text.toString().toInt()
            if (quant>1) {
                quant--
                holder.itemView.tv_quant.text = quant.toString()
                cartListener.onMinus(holder.itemView.tv_quant.text.toString().toInt(),item.price!!.toFloat(),item)
            }
        }

        holder.bind(data[position])
    }

    fun swapData(data: List<ProductX>) {
        this.data = data as MutableList<ProductX>
        notifyDataSetChanged()
    }

    fun removeWithIndex(index: Int) {
        data.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, itemCount)
    }

    class ItemFoodVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item:ProductX) = with(itemView) {
                Picasso.get().load(item.image).into(iv_cart)
            tv_name.text=item.name
            tv_price.text=item.price+" "+item.currency
            setOnClickListener {
            }
        }
    }
}
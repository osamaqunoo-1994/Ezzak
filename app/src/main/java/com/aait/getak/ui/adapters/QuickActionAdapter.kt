package com.aait.getak.ui.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aait.getak.R
import com.aait.getak.models.categories_model.Car
import com.squareup.picasso.Picasso
import org.jetbrains.anko.find

class QuickActionAdapter(
    val context: Context,
    var selected: Int,
    val cats: List<Car>,
    val onItemSelect: (position: Int, car: Car) -> Unit
) :
    RecyclerView.Adapter<QuickActionAdapter.QuickViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuickViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.quick_item, parent, false)
        return QuickViewHolder(view)
    }


    override fun onBindViewHolder(holder: QuickViewHolder, position: Int) {
        holder.mCat!!.text = cats[position].name

        if (cats[position].type == "goods") {
            holder.mExtra!!.text = cats[position].maxWeight
        } else {
            holder.mExtra!!.text =
                cats[position].numPersons.toString() + " " + context.getString(R.string.persons)
        }
        if (position == cats.lastIndex) {
            holder.view!!.visibility = View.INVISIBLE
        }
        if (position == selected) {
            holder.itemView.setBackgroundColor(Color.parseColor("#eeeeee"))
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
        }
        if (cats[position].expectedPrice.isNullOrEmpty()) {
            holder.tv_price?.visibility = View.GONE

        } else {
            holder.tv_price?.text = cats[position].expectedPrice + " " +  context.getString(R.string.sar)
        }

        Picasso.get().load(cats[position].image).into(holder.mark)
        holder.itemView.setOnClickListener {
            notifyDataSetChanged()
            onItemSelect(position, cats[position])

        }

    }

    override fun getItemCount(): Int {
        return cats.size
    }

    class QuickViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var mCat: TextView? = null
        var mExtra: TextView? = null
        var tv_price: TextView? = null
        var mark: ImageView? = null
        var view: View? = null

        init {
            mCat = itemView.find(R.id.cat)
            tv_price = itemView.find(R.id.tv_price)
            mExtra = itemView.find(R.id.extra)
            mark = itemView.find(R.id.img)
            view = itemView.find(R.id.view)
        }
    }

}

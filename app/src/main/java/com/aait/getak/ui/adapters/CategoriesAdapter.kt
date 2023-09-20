package com.aait.getak.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aait.getak.R
import com.aait.getak.models.categories_model.Car
import java.util.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.categories_item.view.*


class CategoriesAdapter(private var selected:Int, private val onItemSelect:(pos:Int,id:String)->Unit) : RecyclerView.Adapter<CategoriesAdapter.CategoriesVH>() {

    private var data: List<Car> = ArrayList()
    private var isSingleCar:Boolean?=false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesVH {
        return CategoriesVH(
            if (isSingleCar!!) {
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.category_item, parent, false)
            }
        else{
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.categories_item, parent, false)

            }
        )
    }


    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: CategoriesVH, position: Int) {
        val item=data[position]
        with(holder.itemView){
            Picasso.get().load(item.image).into(iv_garage)
            //set data
        name.text=item.name
            //set background
        if(selected==position){
            frame.setBackgroundResource(R.drawable.circle_lay_selected)
            name.setTextColor(ContextCompat.getColor(context,R.color.colorAccent))
        }
        else{
            frame.setBackgroundResource(R.drawable.circle_lay)
            name.setTextColor(ContextCompat.getColor(context,R.color.colorSecond))
            }
            //set action and update
            setOnClickListener {
                selected=position
                onItemSelect(position, item.id.toString())
                notifyDataSetChanged()

            }
        }
    }

    fun swapData(data: List<Car>,isSingleCar:Boolean?=false) {
        this.data = data
        this.isSingleCar=isSingleCar
        notifyDataSetChanged()
    }


    class CategoriesVH(itemView: View) : RecyclerView.ViewHolder(itemView)


}
package com.aait.getak.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.aait.getak.R
import com.aait.getak.models.categories_model.Car
import org.jetbrains.anko.layoutInflater
import android.widget.TextView
import com.squareup.picasso.Picasso


class CustomSpinnerAdapter(context: Context,layout:Int,title:Int,data:List<Car>): ArrayAdapter<Car>(context,layout,title,data) {
    private var inflater: LayoutInflater = context.layoutInflater
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = getItem(position)
        val rowView = inflater.inflate(R.layout.adapter_item, null, true)
        val name = rowView.findViewById(R.id.name) as TextView
        val img = rowView.findViewById(R.id.src_img) as ImageView
        val size = rowView.findViewById(R.id.size) as TextView
        name.text = item!!.name
        Picasso.get().load(item.image).into(img)
        size.text = item.maxWeight
        return rowView

    }
}
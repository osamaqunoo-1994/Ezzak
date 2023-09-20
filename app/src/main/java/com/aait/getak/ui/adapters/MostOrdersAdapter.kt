package com.aait.getak.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.aait.getak.R
import com.aait.getak.models.wholde_places_model.SavedPlace
import com.aait.getak.utils.Util
import kotlinx.android.synthetic.main.most_ordered_item.view.*
import java.util.*

class MostOrdersAdapter(val onUnsave:(pos:Int, item:SavedPlace, img:ImageView)->Unit, val onSave:(lat:Double, lng:Double, name:String, address:String)->Unit)
    : RecyclerView.Adapter<MostOrdersAdapter.SavedVH>() {

    private var data: MutableList<SavedPlace> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedVH {
        return SavedVH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.most_ordered_item, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: SavedVH, position: Int) = holder.bind(data[position],position,onUnsave,onSave)

    fun swapData(data: List<SavedPlace>) {
        this.data = data as MutableList<SavedPlace>
        notifyDataSetChanged()
    }

    class SavedVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            item: SavedPlace,
            position: Int,
            onUnsave: (pos: Int, item: SavedPlace, img: ImageView) -> Unit,
            onSave: (lat: Double, lng: Double,name:String,address: String) -> Unit
        ) = with(itemView) {
            title.text=item.address

            item.address?.let { Util.onPrintLog(it) }
            setOnClickListener {
                onSave(item.lat!!,item.long!!,item.address!!,item.address!!)
            }
        }
    }
}
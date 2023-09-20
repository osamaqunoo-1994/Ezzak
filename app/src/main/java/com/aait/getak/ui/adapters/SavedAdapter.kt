package com.aait.getak.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.aait.getak.R
import com.aait.getak.models.wholde_places_model.SavedPlace
import com.aait.getak.ui.activities.map.AddressDetailsActivity
import kotlinx.android.synthetic.main.place_item.view.*
import kotlinx.android.synthetic.main.place_item.view.period_txt
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*

class SavedAdapter(val onUnsave:(pos:Int,item:SavedPlace,img:ImageView)->Unit,val onSave:(lat:Double,lng:Double,name:String,address:String)->Unit) : RecyclerView.Adapter<SavedAdapter.SavedVH>() {

    private var data: MutableList<SavedPlace> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedVH {
        return SavedVH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.place_item, parent, false)
        )
    }
    fun removeItem(pos:Int){
        data.removeAt(pos)
        notifyItemRemoved(pos)
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
            header.text=item.name
            title.text=item.address
            period_txt.text= "${item.distance} ${context.getString(R.string.meter)}"
            if (item.infav!!){
                star.setImageResource(R.mipmap.heartactive)
                star.onClick {
                        onUnsave(position,item,star)
                }
            }
            else{
                star.setImageResource(R.mipmap.heart)
                star.onClick {
                    val intent = Intent(context, AddressDetailsActivity::class.java)
                    intent.putExtra("item",item)
                    intent.putExtra("isNearest",false)
                    context.startActivity(intent)
                }
            }

            setOnClickListener {
                onSave(item.lat!!,item.long!!,item.name!!,item.address!!)
            }
        }
    }
}
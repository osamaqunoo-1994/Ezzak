package com.aait.getak.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.aait.getak.R
import com.aait.getak.models.wholde_places_model.Place
import com.aait.getak.ui.activities.map.AddressDetailsActivity
import kotlinx.android.synthetic.main.place_item.view.*
import kotlinx.android.synthetic.main.place_item.view.period_txt
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*

class NearestPlacesAdapter(val onUnsave:(pos:Int,item: Place,img:ImageView)->Unit,val onSave:(lat:Double,lng:Double,header:String,address:String)->Unit) : RecyclerView.Adapter<NearestPlacesAdapter.NearestVH>() {

    private var isSearch: Boolean?=false
    private var data: MutableList<Place> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NearestVH {
        return NearestVH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.place_item, parent, false)
        )
    }

    fun removeItem(pos:Int){
        data.removeAt(pos)
        notifyItemRemoved(pos)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: NearestVH, position: Int) = holder.bind(data[position],onUnsave,position,onSave,isSearch)

    fun swapData(data: List<Place>,isSearch:Boolean?=false) {
        this.data = data as MutableList<Place>
        this.isSearch=isSearch
        notifyDataSetChanged()
    }

    class NearestVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            item: Place,
            onUnsave: (pos: Int, item: Place, img: ImageView) -> Unit,
            position: Int,
            onSave: (lat: Double, lng: Double, address: String,details:String) -> Unit,
            search: Boolean?
        ) = with(itemView) {
            header.text=item.name
            title.text=item.vicinity
            if (search!!) {
                period_txt.text = "${item.distance} ${context.getString(R.string.km)}"
            }
            else{
                period_txt.text = "${item.distance} ${context.getString(R.string.meter)}"
            }
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
                    intent.putExtra("isNearest",true)
                    context.startActivity(intent)
                }
            }


            setOnClickListener {
                onSave(item.lat!!,item.lng!!,item.name!!,item.vicinity!!)
            }
        }
    }
}
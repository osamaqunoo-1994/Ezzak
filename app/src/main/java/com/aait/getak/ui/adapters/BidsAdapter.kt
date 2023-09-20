package com.aait.getak.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton
import com.aait.getak.R
import com.aait.getak.models.bids_model.Bid
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.bids_item.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*

class BidsAdapter(val onSelectionListener:(item:Bid,btn: CircularProgressButton)->Unit) : RecyclerView.Adapter<BidsAdapter.BidsVH>() {

    private var data: List<Bid> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BidsVH {
        return BidsVH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.bids_item, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: BidsVH, position: Int) = holder.bind(data[position],onSelectionListener)

    fun swapData(data: List<Bid>) {
        this.data = data
        notifyDataSetChanged()
    }

    class BidsVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            item: Bid,
            onSelectionListener: (item: Bid, btn: CircularProgressButton) -> Unit
        ) = with(itemView) {
            captin_name.text=item.name
            captin_phone.text=item.phone
            rate.rating=item.rate!!
            captin_price.text=item.price+" "+item.currency
            Picasso.get().load(item.avatar).into(captin_iv)
            select_btn.onClick {

                    onSelectionListener(item,select_btn)
            }

            setOnClickListener {

            }
        }
    }
}

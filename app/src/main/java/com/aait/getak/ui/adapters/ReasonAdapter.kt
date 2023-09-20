package com.aait.getak.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aait.getak.R
import com.aait.getak.models.client_later_model.Reason
import kotlinx.android.synthetic.main.item_reasons.view.*
import org.jetbrains.anko.sdk27.coroutines.onCheckedChange
import java.util.*

class ReasonAdapter(val checked:(item:Reason,position:Int)->Unit) : RecyclerView.Adapter<ReasonAdapter.ReasonViewHolder>() {

    private var data: List<Reason> = ArrayList()
    private var lastPosition: Int = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReasonViewHolder {
        return ReasonViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_reasons, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ReasonViewHolder, position: Int) {
       // holder.bind(data[position],checked,position)

        holder.itemView.tv_reason.text=data[position].reason!!
        holder.itemView.check_reason.isChecked = position == lastPosition
        holder.itemView.check_reason.onCheckedChange { buttonView, isChecked ->
            if (isChecked){
                lastPosition=position
                checked(data[position], position)


            }
            notifyDataSetChanged()
        }



        }

    fun swapData(data: List<Reason>) {
        this.data = data
        notifyDataSetChanged()
    }

    class ReasonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(
            item: Reason,
            checked: (item: Reason, position: Int) -> Unit,
            position: Int) = with(itemView) {


            setOnClickListener {

            }
        }
    }
}
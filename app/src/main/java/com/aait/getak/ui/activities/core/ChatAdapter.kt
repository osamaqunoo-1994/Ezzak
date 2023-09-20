package com.aait.getak.ui.activities.core

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aait.getak.R
import com.aait.getak.models.chat_model.Message
import kotlinx.android.synthetic.main.chat_item_me.view.*
import kotlinx.android.synthetic.main.chat_item_other.view.*
import kotlinx.android.synthetic.main.text_item_other.view.*


class ChatAdapter : RecyclerView.Adapter<ChatAdapter.ChatVH>() {

    private var VIEW_ITEM_TYPE: Int = 0
    private var data: MutableList<Message> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatVH {
        if (VIEW_ITEM_TYPE==0) {
            return ChatVH(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.chat_item_me, parent, false)
            )
        }
        else{
            return ChatVH(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.chat_item_other, parent, false)
            )

        }
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ChatVH, position: Int) = holder.bind(data[position],VIEW_ITEM_TYPE)

    override fun getItemViewType(position: Int): Int {
        if (data[position].sender=="you"){
            VIEW_ITEM_TYPE=0
        }
        else{
            VIEW_ITEM_TYPE=1
        }
        return VIEW_ITEM_TYPE
    }

    fun swapData(data: MutableList<Message>) {
        this.data = data
        notifyDataSetChanged()
    }

    class ChatVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("CheckResult")
        fun bind(item: Message, viewItemType: Int) = with(itemView) {
            //me

            if (viewItemType==0) {
                    card_txt_item.visibility=View.VISIBLE
                    if (item.type=="text"){
                        tv_me_chat.visibility=View.VISIBLE
                        tv_me_chat.text = item.content

                       // card_txt_item.setCardBackgroundColor( ColorStateList.valueOf(ContextCompat.getColor(context,R.color.colorAccent)))
                    }


                else{
                    card_txt_item.visibility=View.GONE


                }
                tv_me_date.text=item.date


            }

            //other
            else{
                if (item.type == "text" || item.type=="link") {
                    card_txt_other.visibility=View.VISIBLE
                    if (item.type=="text" ){
                      //  richLinkView_other.visibility=View.GONE
                        tv_other_chat.visibility=View.VISIBLE
                        tv_other_chat.text = item.content
                    }
                else{
                    card_txt_other.visibility=View.GONE


                    
                }
                tv_other_date.text=item.date

                }
            }
            setOnClickListener {

            }
        }
    }
}
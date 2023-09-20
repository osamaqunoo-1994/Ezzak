package com.aait.getak.ui.adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aait.getak.R
import com.aait.getak.listeners.Navigation_Listeners
import com.aait.getak.models.NavigationModel
import com.aait.getak.utils.GlobalPreferences
import kotlinx.android.synthetic.main.nav_row_item.view.*

class NavAdapter(val context: Context?, val models:MutableList<NavigationModel>, val listeners: Navigation_Listeners,val onSelect:(indicator:Int)-> Unit,var indicator:Int=-1) : RecyclerView.Adapter<NavAdapter.NavViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.nav_row_item, parent, false)
        return NavViewHolder(view)
    }

    override fun getItemCount(): Int {
        return models.size
    }

    override fun onBindViewHolder(holder: NavViewHolder, position: Int) {
        val model = models[position]
        if (model.name==indicator){
            holder.mlay!!.setBackgroundColor(ContextCompat.getColor(context!!,android.R.color.darker_gray))
        }
        else{
            holder.mlay!!.setBackgroundColor(ContextCompat.getColor(context!!,android.R.color.transparent))
        }

        holder.title!!.setText(model.name)
        holder.icon!!.setImageResource(model.src)

        if (model.name==R.string.my_wallet){
            holder.wallet?.visibility=View.VISIBLE
            holder.wallet?.text=GlobalPreferences(context).user?.balance
            holder.wallet!!.text=model.wallet

        }
        else{
            holder.wallet?.visibility=View.GONE
        }
        if (model.name==R.string.notifications){
            holder.notif_lay?.visibility=View.VISIBLE
            holder.icon?.visibility=View.GONE
            model.notifNum?.let {
                if (it>0) {
                    holder.badge?.setText(model.notifNum.toString(), true)
                }
                else{
                    holder.badge?.visibility=View.GONE
                }

        }
        }
        else{
            holder.notif_lay?.visibility=View.GONE
            holder.icon?.visibility=View.VISIBLE
        }

        holder.mlay!!.setOnClickListener {
            when (model.name) {
                R.string.your_journies -> {
                    indicator=R.string.your_journies
                    onSelect(R.string.your_journies)
                    listeners.onYourJournies()
                    holder.mlay!!.setBackgroundColor(ContextCompat.getColor(context!!,R.color.selected_item))
                }
                R.string.notifications -> {
                    indicator=R.string.notifications
                    onSelect(R.string.notifications)
                    listeners.onNotifications()
                    holder.mlay!!.setBackgroundColor(ContextCompat.getColor(context!!,R.color.selected_item))
                }
                R.string.my_wallet -> {
                    indicator=R.string.my_wallet
                    onSelect(R.string.my_wallet)
                    listeners.onWallet()
                    holder.mlay!!.setBackgroundColor(ContextCompat.getColor(context!!,R.color.selected_item))
                }

                R.string.free_trips->{
                    indicator=R.string.free_trips
                    onSelect(R.string.free_trips)
                    listeners.onFreeTrips()
                    holder.mlay!!.setBackgroundColor(ContextCompat.getColor(context!!,R.color.selected_item))
                }

                R.string.help->{
                    indicator=R.string.help
                    onSelect(indicator)
                    listeners.onHelp()
                    holder.mlay!!.setBackgroundColor(ContextCompat.getColor(context!!,R.color.selected_item))
                }
                R.string.settings->{
                    indicator=R.string.settings
                    onSelect(indicator)
                    listeners.onSettings()
                    holder.mlay!!.setBackgroundColor(ContextCompat.getColor(context!!,R.color.selected_item))
                }
                R.string.contact_us->{
                    indicator=R.string.contact_us
                    onSelect(indicator)
                    listeners.onContacus()
                    holder.mlay!!.setBackgroundColor(ContextCompat.getColor(context!!,R.color.selected_item))
                }


                R.string.join_as_captin -> {
                    indicator=R.string.join_as_captin
                    onSelect(R.string.join_as_captin)
                    listeners.onJoin()
                    holder.mlay!!.setBackgroundColor(ContextCompat.getColor(context!!,R.color.selected_item))
                }
                R.string.offers ->{
                    indicator=R.string.offers
                    onSelect(R.string.offers)
                    listeners.onOffers()
                    holder.mlay!!.setBackgroundColor(ContextCompat.getColor(context!!,R.color.selected_item))
                }
            }

            notifyDataSetChanged()
        }
    }

    class NavViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var mlay=itemView?.mainLay
        var icon=itemView?.nav_src
        var title=itemView?.nav_title
        var wallet=itemView?.nav_addition
        var notif_lay=itemView?.notif_lay
        var badge=itemView?.badge

    }
}
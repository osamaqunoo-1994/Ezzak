package com.aait.getak.ui.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aait.getak.R
import com.aait.getak.models.categories_model.Car
import com.aait.getak.ui.adapters.QuickActionAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.jetbrains.anko.find

class BottomSheetCars(val cats: List<Car>, val onClickItem:(item:Car)->Unit): BottomSheetDialogFragment()  {
    companion object{
         var selectedPos=0

    }
    override fun setupDialog(dialog: Dialog, style: Int) {
        val view = View.inflate(context, R.layout.item_ride, null)
        if (dialog.window != null) {
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        }
        dialog.setContentView(view)
        val rec: RecyclerView = view.find(R.id.recycler)
        rec.layoutManager=LinearLayoutManager(activity)
            val adapter = QuickActionAdapter(
                context!!, selectedPos,cats)
        {pos,data->
            selectedPos=pos
            onClickItem(data)
            dismiss()
        }
        rec.adapter=adapter
    }
}
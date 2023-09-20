package com.aait.getak.ui.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.format.DateFormat
import android.view.View
import android.view.Window
import android.widget.*
import com.aait.getak.R
import com.aait.getak.utils.Util
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*
import kotlin.collections.HashMap
/*face - google - twitter*//**/
class DateBottomSheet(val date: String?="",val time: String?="",val onclick:(date:String,time:String,notes:String?)->Unit) : BottomSheetDialogFragment(){
    private var day: String=""
    private var year: Int=0

    override fun setupDialog(dialog: Dialog, style: Int) {
        val view = View.inflate(context, R.layout.calender_date_time, null)
        if (dialog.window != null) {
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)

        }
        dialog.setContentView(view)
        var cal_values=HashMap<Pair<String,String>,Int>()

        val etDate=view.find<NumberPicker>(R.id.date_picker)
        val etTime=view.find<TimePicker>(R.id.time_picker)
        val etBtn=view.find<MaterialButton>(R.id.reserve_btn)
        var calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        calendar.set(year,month,day)


        etDate.minValue = calendar.get(Calendar.DAY_OF_MONTH)
        etDate.maxValue = calendar.get(Calendar.DAY_OF_MONTH) + 30

        //getDefault day today
        val dates=arrayListOf<String>()
        dates.add(getFormattedDate(calendar).first)
        //get days
        calendar.add(Calendar.DATE, 0)

        cal_values[getFormattedDate(calendar)]=calendar.get(Calendar.DAY_OF_MONTH)
        var counter=calendar.get(Calendar.DAY_OF_MONTH)
        for ( num in 0..31){

            calendar.add(Calendar.DATE, 1)
            counter++
            val formattedDate = getFormattedDate(calendar)
            dates.add(formattedDate.first)
            cal_values[formattedDate]=counter


        }
        etDate.displayedValues= dates.toTypedArray()

        etBtn.onClick {
            val filterKeys = cal_values.filterKeys {
                cal_values[it] == etDate.value
            }
            val selected = filterKeys.keys.first().second
            /*var vg=etTime.getChildAt(0) as ViewGroup
            val button = vg.getChildAt(1) as NumberPicker*/
            var am="am"
            //am 1 else 0
            /*if (button.value==1){
                am="am"
            }*/
            if (etTime.hour>=12){
                am="pm"
            }
            //Log.e("hours",Util.getTime(etTime.hour).toString()+":"+etTime.minute+":"+am)
            onclick(selected,Util.getTime(etTime.hour).toString()+":"+etTime.minute+":"+am,null)
            dismiss()
        }
    }




    private fun getFormattedDate(calendar: Calendar):Pair<String,String>{
        var dayOfTheWeek = DateFormat.format("EEEE", calendar) as String // Thursday
        var day_          =  DateFormat.format("dd",   calendar) as String // 20
        var monthString  = DateFormat.format("MMM",  calendar) as String // Jun

        var year=DateFormat.format("yyyy",  calendar).toString()
        var month=DateFormat.format("MM",  calendar).toString()
        return Pair("$dayOfTheWeek $day_ $monthString","$day_-$month-$year")/*"$day_-$monthString-$year"*/
    }

}
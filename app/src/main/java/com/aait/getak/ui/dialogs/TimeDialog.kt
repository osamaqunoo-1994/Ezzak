package com.aait.getak.ui.dialogs

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class TimeDialog (val onReceive:(hr:Int,min:Int)->Unit) : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current time as the default values for the picker
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)


        return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))

    }


    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        onReceive(hourOfDay,minute)
    }

}
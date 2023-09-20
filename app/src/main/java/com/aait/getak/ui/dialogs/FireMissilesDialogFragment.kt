package com.aait.getak.ui.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.aait.getak.R


class FireMissilesDialogFragment(val msg:String?,val onClick:(i:Int)->Unit) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage(msg)
                .setPositiveButton(R.string.fire
                ) { dialog, id ->
                    // FIRE ZE MISSILES!
                    onClick(1)
                }

                .setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                        onClick(0)
                    })
            // Create the AlertDialog object and return it
            val create = builder.create()


            create
        }.also {dialog->
            dialog!!.setOnShowListener {
                val negativeButton =
                    dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
                val positiveButton =
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE)

                val negativeButtonDrawable =
                    resources.getDrawable(android.R.color.transparent)
                val positiveButtonDrawable =
                    resources.getDrawable(android.R.color.transparent)
                negativeButton.background = negativeButtonDrawable
                positiveButton.background = positiveButtonDrawable

                positiveButton.setTextColor(resources.getColor(R.color.colorAccent))
                negativeButton.setTextColor(R.color.colorPrimary)

                negativeButton.invalidate()
                positiveButton.invalidate()

            }

        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
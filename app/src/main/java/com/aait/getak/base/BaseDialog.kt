package com.aait.getak.base

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ProgressBar
import com.aait.getak.R
import com.github.ybq.android.spinkit.style.FadingCircle


class BaseDialog(context: Activity):Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.base_custom_dialog)
        val progressBar = findViewById<View>(R.id.spin_kit) as ProgressBar
        val doubleBounce = FadingCircle()
        progressBar.indeterminateDrawable = doubleBounce


    }
}
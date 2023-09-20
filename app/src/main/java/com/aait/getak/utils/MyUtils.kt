package com.aait.getak.utils

import android.view.Window
import android.view.WindowManager

object MyUtils{

    fun setStatusBarTrans(window: Window){
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

}
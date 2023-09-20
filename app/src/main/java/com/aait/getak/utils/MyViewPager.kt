package com.aait.getak.utils

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.duolingo.open.rtlviewpager.RtlViewPager


class MyViewPager(context: Context, attrs: AttributeSet) : RtlViewPager(context, attrs) {
     //private var enabled: Boolean = false
    private var isMove=false

    init {
        this.isMove = true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (this.isMove) {
            super.onTouchEvent(event)
        } else false
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (this.isMove) {
            super.onInterceptTouchEvent(event)
        } else false
    }

    fun setPagingEnabled(isMove: Boolean) {
        this.isMove = isMove
    }

}

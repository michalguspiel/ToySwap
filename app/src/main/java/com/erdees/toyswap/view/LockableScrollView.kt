package com.erdees.toyswap.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ScrollView

open class LockableScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ScrollView(context, attrs, defStyleAttr) {


    fun disableScrollView(){
        isDisabled = true
    }

    fun enableScrollView(){
        isDisabled = false
    }

    private var isDisabled = false

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return if(!isDisabled) super.onTouchEvent(ev)
        else {
            false
        }
    }

}
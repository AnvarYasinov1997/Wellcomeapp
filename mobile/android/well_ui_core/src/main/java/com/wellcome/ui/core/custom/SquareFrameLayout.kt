package com.wellcome.ui.core.custom

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.widget.FrameLayout

/**
 * Created by @mistreckless on 03.09.2017. !
 */
class SquareFrameLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0, defRes : Int =0) : FrameLayout(context, attrs, defStyle,defRes) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        @SuppressLint("DrawAllocation")
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        val size = if (width > height) height else width
        setMeasuredDimension(size, size)
    }
}
package com.wellcomeapp.ui_core.custom

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.support.design.widget.AppBarLayout
import android.util.AttributeSet
import android.util.DisplayMetrics

/**
 * Created by @mistreckless on 08.10.2017. !
 */
class SquareAppBarLayout @JvmOverloads constructor(context : Context, attrs : AttributeSet?=null) : AppBarLayout(context,attrs) {
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
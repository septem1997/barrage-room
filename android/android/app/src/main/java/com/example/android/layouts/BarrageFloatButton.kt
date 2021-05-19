package com.example.android.layouts

import android.content.Context
import android.widget.ImageView
import com.example.android.R


class BarrageFloatButton(context: Context) : DragViewLayout(context) {
    init {
        isClickable = true
        val floatView = ImageView(context)
        floatView.setImageResource(R.drawable.rocket)

        val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        addView(floatView, params)
    }
}
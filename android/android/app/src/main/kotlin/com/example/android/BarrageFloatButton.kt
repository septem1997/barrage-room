package com.example.android

import android.content.Context
import android.view.View.OnClickListener
import android.widget.ImageView
import android.widget.Toast


class BarrageFloatButton(context: Context) : DragViewLayout(context) {
    init {
        isClickable = true
        val floatView = ImageView(context)
        floatView.setImageResource(R.drawable.rocket)
        floatView.setOnClickListener(OnClickListener {
            Toast.makeText(context,
                "点击了悬浮球",
                Toast.LENGTH_SHORT).show()
        })

        val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        addView(floatView, params)
    }
}
package com.example.android.layouts

import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.util.AttributeSet
import android.view.Gravity
import android.view.WindowManager

class BarrageLayout(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    FloatLayout(context, attrs, defStyleAttr) {


    constructor(context: Context) : this(context, null) {}
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {}


    override fun show() {
        super.show()
        floatLayoutParams = WindowManager.LayoutParams(
            LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.RGBA_8888
        )
        floatLayoutParams!!.gravity = Gravity.START or Gravity.TOP
        floatLayoutParams!!.x = 0
        floatLayoutParams!!.y = 0

        mWindowManager.addView(this, floatLayoutParams)
    }

    init {
        mWindowManager = getContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        mScreenWidth = context.resources.displayMetrics.widthPixels
        mScreenHeight = context.resources.displayMetrics.heightPixels
        mStatusBarHeight = getStatusHeight()
        setBackgroundColor(Color.argb(128,255,255,255))
    }
}
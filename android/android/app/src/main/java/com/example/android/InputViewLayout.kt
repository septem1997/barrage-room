package com.example.android

import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText

class InputViewLayout(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    FloatLayout(context, attrs, defStyleAttr) {


    constructor(context: Context) : this(context, null) {}
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {}


    override fun show() {
        floatLayoutParams = WindowManager.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                 WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.RGBA_8888)
        floatLayoutParams!!.gravity = Gravity.START or Gravity.TOP
        floatLayoutParams!!.x = 0
        floatLayoutParams!!.y = mScreenHeight - 200
        floatLayoutParams!!.width = mScreenWidth
        floatLayoutParams!!.height = 200

        mWindowManager.addView(this, floatLayoutParams)
    }

    init {
        mWindowManager = getContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        mScreenWidth = context.resources.displayMetrics.widthPixels
        mScreenHeight = context.resources.displayMetrics.heightPixels
        mStatusBarHeight = getStatusHeight()
        setBackgroundColor(Color.WHITE)
        val edit = EditText(context)
        edit.hint = "请输入弹幕内容"
        edit.setHintTextColor(Color.GRAY)
        val editParams = LayoutParams(0, LayoutParams.MATCH_PARENT)
        editParams.weight = 1F
        addView(edit,editParams)
    }
}
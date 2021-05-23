package com.example.android.layouts

import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.content.ContextCompat.getSystemService

class InputViewLayout(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    FloatLayout(context, attrs, defStyleAttr) {

    val edit:EditText
    val btn:Button

    constructor(context: Context) : this(context, null) {}
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {}


    override fun show() {
        super.show()
        floatLayoutParams = WindowManager.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                 WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.RGBA_8888)
        floatLayoutParams!!.gravity = Gravity.BOTTOM
        floatLayoutParams!!.x = 0
        floatLayoutParams!!.y = 0

        mWindowManager.addView(this, floatLayoutParams)
        Handler(Looper.getMainLooper()).postDelayed( {
            edit.requestFocus()
            val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        },100)
    }

    init {
        mWindowManager = getContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        setBackgroundColor(Color.argb(128,0,0,0))

        val linearLayout = LinearLayout(context)
        val linearLayoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        linearLayoutParams.addRule(ALIGN_PARENT_BOTTOM)
        addView(linearLayout,linearLayoutParams)

        edit = EditText(context)
        edit.hint = "请输入弹幕内容"
        edit.setHintTextColor(Color.WHITE)
        edit.setTextColor(Color.WHITE)
        edit.isFocusable = true
        edit.isFocusableInTouchMode = true
        val editParams = LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT,1F)
        linearLayout.addView(edit,editParams)

        btn = Button(context)
        btn.text="发送"
        btn.isClickable = true
        linearLayout.addView(btn,LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
    }
}
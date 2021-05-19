package com.example.android.layouts

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.PixelFormat
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.RelativeLayout


abstract class FloatLayout(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    RelativeLayout(context, attrs, defStyleAttr) {
    //view所在位置
    var mLastX = 0
    var mLastY = 0

    var isFloatLayoutVisible = false

    //屏幕高宽
    var mScreenWidth: Int
    var mScreenHeight: Int

    var mStatusBarHeight:Int

    //view高宽
    var mWidth = 0
    var mHeight = 0

    var floatLayoutParams: WindowManager.LayoutParams? = null
    var mWindowManager: WindowManager = getContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager

    constructor(context: Context) : this(context, null) {}
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {}


    fun setLayoutVisible(visible:Boolean){
        if (visible){
            show()
        }else{
            remove()
        }
    }

    open fun show() {
        isFloatLayoutVisible = true
    }

    fun remove(){
        mWindowManager.removeView(this)
        isFloatLayoutVisible = false
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (mWidth == 0) {
            //获取悬浮球高宽
            mWidth = width
            mHeight = height
        }
    }

    fun getStatusHeight(): Int {
        var statusHeight = -1
        try {
            val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                statusHeight = resources.getDimensionPixelSize(resourceId)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return statusHeight
    }

    fun resetScreenSize(){
        mScreenWidth = context.resources.displayMetrics.widthPixels
        mScreenHeight = context.resources.displayMetrics.heightPixels
        mStatusBarHeight = getStatusHeight()
        Log.d("屏幕尺寸",mScreenWidth.toString()+"X"+mScreenHeight.toString())
        Log.d("状态栏高度",mStatusBarHeight.toString())
    }

    init {
        mScreenWidth = context.resources.displayMetrics.widthPixels
        mScreenHeight = context.resources.displayMetrics.heightPixels
        mStatusBarHeight = getStatusHeight()
        Log.d("屏幕尺寸",mScreenWidth.toString()+"X"+mScreenHeight.toString())
        Log.d("状态栏高度",mStatusBarHeight.toString())
    }
}
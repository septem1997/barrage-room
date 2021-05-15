package com.example.android

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.PixelFormat
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.RelativeLayout


open class FloatLayout(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    LinearLayout(context, attrs, defStyleAttr) {
    //view所在位置
    var mLastX = 0
    var mLastY = 0

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

    //更新悬浮球位置
    fun updateFloatPosition() {
        val x = 0
        val y = mScreenHeight - mHeight
        floatLayoutParams!!.x = x
        floatLayoutParams!!.y = y
        mWindowManager.updateViewLayout(this, floatLayoutParams)
    }

    //执行贴边动画
    fun startAnim() {
        val valueAnimator: ValueAnimator
        if (floatLayoutParams!!.x < mScreenWidth / 2) {
            valueAnimator = ValueAnimator.ofInt(floatLayoutParams!!.x, 0)
        } else {
            valueAnimator = ValueAnimator.ofInt(floatLayoutParams!!.x, mScreenWidth - mWidth)
        }
        valueAnimator.duration = 200
        valueAnimator.addUpdateListener { animation ->
            floatLayoutParams!!.x = animation.animatedValue as Int
            mWindowManager.updateViewLayout(this@FloatLayout, floatLayoutParams)
        }
        valueAnimator.start()
    }

    //悬浮页面显示
    open fun show() {
        floatLayoutParams = WindowManager.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.RGBA_8888)
        floatLayoutParams!!.gravity = Gravity.START or Gravity.TOP
        floatLayoutParams!!.x = mScreenWidth - 144
        floatLayoutParams!!.y = (mScreenHeight * 0.4).toInt()
        floatLayoutParams!!.width = 144
        floatLayoutParams!!.height = 144

        mWindowManager.addView(this, floatLayoutParams)
    }

    fun remove(){
        mWindowManager.removeView(this)
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

    open fun resetSize(){
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
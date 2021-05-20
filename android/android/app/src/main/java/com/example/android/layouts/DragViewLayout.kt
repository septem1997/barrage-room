package com.example.android.layouts

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.PixelFormat
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.view.WindowManager
import kotlin.math.abs


open class DragViewLayout(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    FloatLayout(context, attrs, defStyleAttr) {

    /**
     * 是否在拖拽过程中
     */
    var isDrag = false

    /**
     * 系统最小滑动距离
     * @param context
     */
    var mTouchSlop = 0

    //手指触摸位置
    private var xInScreen = 0f
    private var yInScreen = 0f
    private var xInView = 0f
    private var yInView = 0f

    constructor(context: Context) : this(context, null) {}
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {}


    fun alignToEdge() {
        val valueAnimator: ValueAnimator = if (floatLayoutParams!!.x < mScreenWidth / 2) {
            ValueAnimator.ofInt(floatLayoutParams!!.x, 0)
        } else {
            ValueAnimator.ofInt(floatLayoutParams!!.x, mScreenWidth - mWidth)
        }
        valueAnimator.duration = 200
        valueAnimator.addUpdateListener { animation ->
            floatLayoutParams!!.x = animation.animatedValue as Int
            mWindowManager.updateViewLayout(this, floatLayoutParams)
        }
        valueAnimator.start()
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mLastX = event.rawX.toInt()
                mLastY = event.rawY.toInt()
                yInView = event.y
                xInView = event.x
                xInScreen = event.rawX
                yInScreen = event.rawY
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = event.rawX.toInt() - mLastX
                val dy = event.rawY.toInt() - mLastY
                if (abs(dx) > mTouchSlop || abs(dy) > mTouchSlop) {
                    isDrag = true
                }
                xInScreen = event.rawX
                yInScreen = event.rawY
                mLastX = event.rawX.toInt()
                mLastY = event.rawY.toInt()
                updateFloatPosition(false)
            }
            MotionEvent.ACTION_UP -> if (isDrag) {
                alignToEdge()
                isDrag = false
            }
            else -> {
            }
        }
        return super.dispatchTouchEvent(event)
    }

    fun updateFloatPosition(reset:Boolean) {
        var x = (xInScreen - xInView).toInt()
        var y = (yInScreen - yInView).toInt() - mStatusBarHeight
        if (y < 0) {
            y = 0
        }
        if (x < 0){
            x = 0
        }
        if (y > mScreenHeight - mHeight) {
            y = mScreenHeight - mHeight
        }
        if (x > mScreenWidth - mWidth) {
            x = mScreenWidth - mWidth
        }
        if (reset){
            x = mScreenWidth - mWidth
            y = (mScreenHeight * 0.4).toInt()
        }
        floatLayoutParams!!.x = x
        floatLayoutParams!!.y = y
        mWindowManager.updateViewLayout(this, floatLayoutParams)
    }

    override fun show() {
        super.show()
        floatLayoutParams = WindowManager.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.RGBA_8888)
        floatLayoutParams!!.gravity = Gravity.LEFT or Gravity.TOP
        floatLayoutParams!!.x = mScreenWidth - 144
        floatLayoutParams!!.y = (mScreenHeight * 0.4).toInt()
        floatLayoutParams!!.width = 144
        floatLayoutParams!!.height = 144

        mWindowManager.addView(this, floatLayoutParams)
    }


    init {
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        mWindowManager = getContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        mScreenWidth = context.resources.displayMetrics.widthPixels
        mScreenHeight = context.resources.displayMetrics.heightPixels
        mStatusBarHeight = getStatusHeight()
    }
}
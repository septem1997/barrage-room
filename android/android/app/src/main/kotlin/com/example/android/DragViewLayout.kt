package com.example.android

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.view.WindowManager
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import kotlin.math.abs


open class DragViewLayout(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    RelativeLayout(context, attrs, defStyleAttr) {
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

    /**
     * 是否在拖拽过程中
     */
    var isDrag = false

    /**
     * 系统最小滑动距离
     * @param context
     */
    var mTouchSlop = 0
    var floatLayoutParams: WindowManager.LayoutParams? = null
    var mWindowManager: WindowManager

    //手指触摸位置
    private var xInScreen = 0f
    private var yInScreen = 0f
    private var xInView = 0f
    var yInView = 0f

    constructor(context: Context) : this(context, null) {}
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {}

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
                updateFloatPosition()
            }
            MotionEvent.ACTION_UP -> if (isDrag) {
                //执行贴边
                startAnim()
                isDrag = false
            }
            else -> {
            }
        }
        return super.dispatchTouchEvent(event)
    }

    //更新悬浮球位置
    fun updateFloatPosition() {
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
        Log.d("position","mScreenWidth$mScreenWidth mScreenHeight$mScreenHeight x:$x y:$y  xInScreen:$xInScreen yInScreen:$yInScreen xInView$xInView yInView$yInView")
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
            mWindowManager.updateViewLayout(this@DragViewLayout, floatLayoutParams)
        }
        valueAnimator.start()
    }

    //悬浮球显示
    fun show() {
        floatLayoutParams = WindowManager.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.RGBA_8888)
        floatLayoutParams!!.gravity = Gravity.LEFT or Gravity.TOP
        floatLayoutParams!!.x = 0
        floatLayoutParams!!.y = (mScreenHeight * 0.4).toInt()
        floatLayoutParams!!.width = 120
        floatLayoutParams!!.height = 120

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

    fun resetSize(){
        mScreenWidth = context.resources.displayMetrics.widthPixels
        mScreenHeight = context.resources.displayMetrics.heightPixels
        mStatusBarHeight = getStatusHeight()
        Log.d("屏幕尺寸",mScreenWidth.toString()+"X"+mScreenHeight.toString())
        Log.d("状态栏高度",mStatusBarHeight.toString())
    }

    init {
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        mWindowManager = getContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        mScreenWidth = context.resources.displayMetrics.widthPixels
        mScreenHeight = context.resources.displayMetrics.heightPixels
        mStatusBarHeight = getStatusHeight()
        Log.d("屏幕尺寸",mScreenWidth.toString()+"X"+mScreenHeight.toString())
        Log.d("状态栏高度",mStatusBarHeight.toString())
    }
}
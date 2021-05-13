package com.example.android

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.ImageView


class CustomMoveButton(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : ImageView(context, attrs, defStyleAttr) {
    private val statusHeight: Int
    var sW: Int
    var sH: Int
    private var mTouchStartX = 0f
    private var mTouchStartY = 0f
    private var coorX = 0f
    private var coorY = 0f
    private var isMove = false
    private var mContext: Context? = null
    private val wm = getContext().getApplicationContext()
        .getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private val wmParams: WindowManager.LayoutParams =
        WindowManager.LayoutParams()
    private var mLastX = 0f
    private var mLastY = 0f
    private var mStartX = 0f
    private var mStartY = 0f
    private var mDownTime: Long = 0
    private var mUpTime: Long = 0
    private var listener: OnSpeakListener? = null

    constructor(context: Context) : this(context, null) {
        this.mContext = context
        this.wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, -1) {}

    override fun onTouchEvent(event: MotionEvent): Boolean {
//获取相对屏幕的坐标，即以屏幕左上角为原点
        coorX = event.rawX
        coorY = event.rawY - statusHeight //statusHeight是系统状态栏的高度
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                setImageResource(R.mipmap.ic_launcher)
                mTouchStartX = event.x
                mTouchStartY = event.y
                mStartX = event.rawX
                mStartY = event.rawY
                mDownTime = System.currentTimeMillis()
                isMove = false
            }
            MotionEvent.ACTION_MOVE -> {
                updateViewPosition()
                isMove = true
            }
            MotionEvent.ACTION_UP -> {
                setImageResource(R.mipmap.ic_launcher)
                mLastX = event.rawX
                mLastY = event.rawY
                mUpTime = System.currentTimeMillis()
                //按下到抬起的时间大于500毫秒,并且抬手到抬手绝对值大于20像素处理点击事件
                if (mUpTime - mDownTime < 500) {
                    if (Math.abs(mStartX - mLastX) < 20.0 && Math.abs(mStartY - mLastY) < 20.0) {
                        if (listener != null) {
                            listener!!.onSpeakListener()
                        }
                    }
                }
            }
        }
        return true
    }

    private fun updateViewPosition() {
        wmParams.x = (coorX - mTouchStartX).toInt()
        wmParams.y = (coorY - mTouchStartY).toInt()
        wm.updateViewLayout(this, wmParams) //刷新显示
    }

    /**
     * 设置点击回调接口
     */
    interface OnSpeakListener {
        fun onSpeakListener()
    }

    fun setOnSpeakListener(listener: OnSpeakListener?) {
        this.listener = listener
    }

    companion object {
        /**
         * 状态栏的高度
         *
         */
        fun getStatusHeight(context: Context): Int {
            var statusHeight = -1
            try {
                val clazz = Class.forName("com.android.internal.R\$dimen") //使用反射获取实例
                val `object` = clazz.newInstance()
                val height = clazz.getField("status_bar_height")[`object`].toString().toInt()
                statusHeight = context.getResources().getDimensionPixelSize(height)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return statusHeight
        }
    }

    init {
        sW = wm.defaultDisplay.width
        sH = wm.defaultDisplay.height
        statusHeight = getStatusHeight(context)
    }
}
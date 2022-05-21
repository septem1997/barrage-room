package com.example.android.layouts

import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.example.android.utils.BiliDanmakuParser
import master.flame.danmaku.controller.DrawHandler
import master.flame.danmaku.danmaku.loader.IllegalDataException
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory
import master.flame.danmaku.danmaku.model.BaseDanmaku
import master.flame.danmaku.danmaku.model.DanmakuTimer
import master.flame.danmaku.danmaku.model.IDisplayer
import master.flame.danmaku.danmaku.model.android.DanmakuContext
import master.flame.danmaku.danmaku.model.android.Danmakus
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser
import master.flame.danmaku.ui.widget.DanmakuView
import java.io.InputStream


class BarrageLayout(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    FloatLayout(context, attrs, defStyleAttr) {
    private lateinit var mDanmakuView: DanmakuView
    private lateinit var mContext: DanmakuContext
    private lateinit var mParser: BaseDanmakuParser

    constructor(context: Context) : this(context, null) {}
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {}

    @RequiresApi(Build.VERSION_CODES.O)
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
        // todo 写篇博客
        floatLayoutParams!!.alpha = 0.8F
        mWindowManager.addView(this, floatLayoutParams)
    }

    private fun createParser(stream: InputStream?): BaseDanmakuParser {
        if (stream == null) {
            return object : BaseDanmakuParser() {
                override fun parse(): Danmakus {
                    return Danmakus()
                }
            }
        }
        val loader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI)
        try {
            loader.load(stream)
        } catch (e: IllegalDataException) {
            e.printStackTrace()
        }
        val parser: BaseDanmakuParser = BiliDanmakuParser()
        val dataSource = loader.dataSource
        parser.load(dataSource)
        return parser
    }


    private fun createDanmakuView() {
        // 设置最大显示行数
        val maxLinesPair = HashMap<Int, Int>()
        maxLinesPair[BaseDanmaku.TYPE_SCROLL_RL] = 5 // 滚动弹幕最大显示5行

        // 设置是否禁止重叠
        // 设置是否禁止重叠
        val overlappingEnablePair = HashMap<Int, Boolean>()
        overlappingEnablePair[BaseDanmaku.TYPE_SCROLL_RL] = true
        overlappingEnablePair[BaseDanmaku.TYPE_FIX_TOP] = true

        mDanmakuView = DanmakuView(context)
        mContext = DanmakuContext.create()
        mContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3F)
            .setDuplicateMergingEnabled(false).setScrollSpeedFactor(1.2f).setScaleTextSize(1.2f)
            .setMaximumLines(maxLinesPair)
            .preventOverlapping(overlappingEnablePair).setDanmakuMargin(40)
        mParser = createParser(null)
        mDanmakuView.setCallback(object : DrawHandler.Callback {
            override fun updateTimer(timer: DanmakuTimer) {}
            override fun drawingFinished() {}
            override fun danmakuShown(danmaku: BaseDanmaku) {
//                    Log.d("DFM", "danmakuShown(): text=" + danmaku.text);
            }

            override fun prepared() {
                mDanmakuView.start()
            }
        })
        mDanmakuView.prepare(mParser, mContext)
//        mDanmakuView.showFPS(true)
        mDanmakuView.enableDanmakuDrawingCache(true)

        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        addView(mDanmakuView, layoutParams)
    }

    fun addDanmaku(text: String) {
        val danmaku = mContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL) ?: return
        danmaku.text = text
        danmaku.padding = 5
        danmaku.priority = 0 // 可能会被各种过滤器过滤并隐藏显示
        danmaku.isLive = true
        danmaku.time = mDanmakuView.currentTime
        danmaku.textSize = 25f * (mParser.displayer.density - 0.6f)
        danmaku.textColor = Color.WHITE
        danmaku.textShadowColor = Color.BLACK
        // danmaku.underlineColor = Color.GREEN;
//        danmaku.borderColor = Color.GREEN
        mDanmakuView.addDanmaku(danmaku)
    }

    init {
        mWindowManager = getContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        mScreenWidth = context.resources.displayMetrics.widthPixels
        mScreenHeight = context.resources.displayMetrics.heightPixels
        mStatusBarHeight = getStatusHeight()

        createDanmakuView()
    }
}
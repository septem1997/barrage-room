package com.example.android

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Point
import android.os.IBinder
import android.util.Log
import android.view.Display
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.WindowManager
import kotlin.concurrent.thread


class FloatService : Service() {
    private var lastOrientation:Int = 0
    lateinit var floatButton:BarrageFloatButton
    private var needDelay = false

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        super.onCreate()
        floatButton = BarrageFloatButton(applicationContext)
        floatButton.show()
    }

    private inner class ResetThread : Thread() {
        override fun run() {
            sleep(200)
            if (needDelay) {
                needDelay = false
                ResetThread().start()
                return
            }
            floatButton.resetSize()
            floatButton.updateFloatPosition()
            floatButton.startAnim()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (lastOrientation != newConfig.orientation){
            needDelay = true
            ResetThread().start()
        }
        lastOrientation = newConfig.orientation

    }
}
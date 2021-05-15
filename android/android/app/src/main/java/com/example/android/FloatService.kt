package com.example.android

import android.app.Service
import android.content.Intent
import android.content.res.Configuration
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Toast


class FloatService : Service() {
    private var lastOrientation:Int = 0
    lateinit var floatButton:BarrageFloatButton
    lateinit var inputLayout:InputViewLayout
    private var needDelay: Boolean = false
    private var showInputLayout = false
    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        super.onCreate()
        floatButton = BarrageFloatButton(applicationContext)
        inputLayout = InputViewLayout(applicationContext)
        floatButton.show()
        floatButton.setOnClickListener {
            if (showInputLayout){
                inputLayout.remove()
                showInputLayout = false
            }else{
                inputLayout.show()
                showInputLayout = true
            }
        }
    }

    private inner class ResetThread : Thread() {
        override fun run() {
            sleep(200)
            Handler(Looper.getMainLooper()).post {
                floatButton.resetSize()
                floatButton.updateFloatPosition(true)
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (lastOrientation != newConfig.orientation){
            // todo 防抖
            ResetThread().start()
        }
        lastOrientation = newConfig.orientation

    }
}
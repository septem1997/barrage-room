package com.example.android.service

import android.app.Service
import android.content.Intent
import android.content.res.Configuration
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import com.example.android.layouts.BarrageFloatButton
import com.example.android.layouts.BarrageLayout
import com.example.android.layouts.InputViewLayout

class FloatService : Service() {
    private var lastOrientation:Int = 0
    lateinit var floatButton: BarrageFloatButton
    lateinit var inputLayout: InputViewLayout
    lateinit var barrageLayout: BarrageLayout
    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        super.onCreate()
        floatButton = BarrageFloatButton(applicationContext)
        inputLayout = InputViewLayout(applicationContext)
        barrageLayout = BarrageLayout(applicationContext)
        floatButton.show()
        floatButton.setOnClickListener {
            inputLayout.setLayoutVisible(!inputLayout.isFloatLayoutVisible)
        }
        inputLayout.setOnClickListener{
            inputLayout.remove()
        }
        barrageLayout.show()
        inputLayout.btn.setOnClickListener {
            barrageLayout.addDanmaku()
        }
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (lastOrientation != newConfig.orientation){
            // todo 防抖
            Thread {
                Thread.sleep(200)
                Handler(Looper.getMainLooper()).post {
                    floatButton.resetScreenSize()
                    floatButton.updateFloatPosition(true)
                }
            }.start()
        }
        lastOrientation = newConfig.orientation

    }
}
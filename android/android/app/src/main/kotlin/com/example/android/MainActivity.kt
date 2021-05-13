package com.example.android

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class FloatView(context: Context?) : Button(context) {
    init {
        setBackgroundResource(R.mipmap.sym_def_app_icon)
    }
}

class MainActivity: FlutterActivity() {
    private val CHANNEL = "com.septem1997.flutter/barrageRoom"
    private lateinit var button: CustomMoveButton;

    private lateinit var wmParams: WindowManager.LayoutParams
    private lateinit var wm: WindowManager
    private var showButton = false

    private fun checkPermission():Boolean{
        Log.d("info","检查权限")
        return !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this))
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun turnOnPermission(){
        Log.d("info","设置权限")
        val intent = Intent();
        intent.action = Settings.ACTION_MANAGE_OVERLAY_PERMISSION;
        intent.data = Uri.parse("package:$packageName");
        startActivityForResult(intent, 0);
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("RtlHardcoded")
    fun showFloatingWindow():Boolean {
        if (showButton){
            return true
        }
        try {
            val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
            val floatView = FloatView(applicationContext)
            val params = WindowManager.LayoutParams()
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            params.format = PixelFormat.RGBA_8888
            params.gravity = Gravity.LEFT or Gravity.TOP
            params.flags =
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            params.width = 150
            params.height = 150
            params.x = 0
            params.y = 100
            windowManager.addView(floatView, params)
            showButton = true
        }catch (e:Exception){
            Log.e(null,"启动按钮失败")
        }
        return showButton
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler {
            // Note: this method is invoked on the main thread.
                call, result ->
            when (call.method) {
                "checkPermission" -> {
                    result.success(checkPermission())
                }
                "turnOnPermission" -> {
                    result.success(turnOnPermission())
                }
                "showFloatingWindow" -> {
                    result.success(showFloatingWindow())
                }
                else -> {
                    result.notImplemented()
                }
            }
        }
    }
}

package com.example.android

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.SensorManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.OrientationEventListener
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel


class MainActivity: FlutterActivity() {
    private val CHANNEL = "com.septem1997.flutter/barrageRoom"

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


    fun showFloatingWindow(){
        startService(Intent(this,FloatService::class.java))
    }

    @RequiresApi(Build.VERSION_CODES.M)
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

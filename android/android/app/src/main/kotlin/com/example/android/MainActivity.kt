package com.example.android

import android.R.id.message
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.util.Log
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import com.example.android.binder.FloatBinder
import com.example.android.service.FloatService
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.BasicMessageChannel
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.StringCodec


class MainActivity: FlutterActivity() {
    private val CHANNEL = "com.septem1997.flutter/barrageRoom"
    private fun checkPermission():Boolean{
        Log.d("info","检查权限")
        return !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this))
    }

    private lateinit var floatBinder:FloatBinder
    private lateinit var floatService: FloatService
    private lateinit var messageChannel: BasicMessageChannel<String>

    @RequiresApi(Build.VERSION_CODES.M)
    private fun turnOnPermission(){
        Log.d("info","设置权限")
        val intent = Intent();
        intent.action = Settings.ACTION_MANAGE_OVERLAY_PERMISSION;
        intent.data = Uri.parse("package:$packageName");
        startActivityForResult(intent, 0);
    }

    private val connection: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName) {}
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            floatBinder = service as FloatBinder
            floatService = floatBinder.getService()
            floatService.setOnSendMsgListener(object : FloatService.OnSendMsgListener {
                override fun onSendMsg(text: String) {
                    Log.d("MainActivity", "sendMsg:$text")
                    sendMsgToFlutter(text)
                }
            })
        }
    }

    fun sendMsgToFlutter(text:String){
        messageChannel = BasicMessageChannel(
            flutterEngine!!.dartExecutor,
            CHANNEL,
            StringCodec.INSTANCE
        )
        messageChannel.send(text)
        Log.d("MainActivity", "sendMsgToFlutter:$text")
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
    }


    fun showFloatingWindow(){
        bindService(Intent(this, FloatService::class.java),connection,BIND_AUTO_CREATE)
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
                "receiveMsg" -> {
                    result.success(floatService.onReceiveMsg(call.arguments as String))
                }
                else -> {
                    result.notImplemented()
                }
            }
        }
    }
}

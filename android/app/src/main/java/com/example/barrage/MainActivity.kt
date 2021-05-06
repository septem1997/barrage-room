package com.example.barrage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.Polling
import io.socket.engineio.client.transports.PollingXHR
import io.socket.engineio.client.transports.WebSocket
import okhttp3.*
import java.io.IOException
import java.net.URISyntaxException


class MainActivity : AppCompatActivity() {
    var socket: Socket? = null
        private set
    private var hasError = false

    private val okHttpClient = OkHttpClient()


    // Better use in a background thread.
    fun initSocket(): Socket? {
        if (socket == null && !hasError) {
            val options = getOptions()
            socket = try {
                IO.socket("ws://localhost:3001", options)
            } catch (e: RuntimeException) {
                // java.lang.RuntimeException: java.net.MalformedURLException: For input string: "...".
                hasError = true
                null
            } catch (e: URISyntaxException) {
                hasError = true
                null
            }
        }
        return socket
    }

    fun connect() {
        socket?.apply {
            if (!connected()) {
                connect()
            }
        }
    }

    fun disconnect() {
        socket?.apply {
            if (connected()) {
                disconnect()
            }
        }
    }

    private fun getOptions(): IO.Options {
        // Fixing the error "io.socket.engineio.client.EngineIOException: xhr poll error".
        IO.setDefaultOkHttpWebSocketFactory(okHttpClient)
        IO.setDefaultOkHttpCallFactory(okHttpClient)
        return IO.Options().apply {
            forceNew = true
            reconnectionAttempts = Integer.MAX_VALUE;
            reconnection = true
            secure = true
            timeout = 1000
            transports = arrayOf(Polling.NAME, PollingXHR.NAME, WebSocket.NAME)
            query = "..." // For authorization.
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        Thread(Runnable {
            run {
                val request: Request = Request.Builder()
                        .url("https://192.168.0.105:3000/")
                        .build()
                okHttpClient.newCall(request).execute().use { response -> println(response.body().toString())  }
            }
        }).start()
    }

}

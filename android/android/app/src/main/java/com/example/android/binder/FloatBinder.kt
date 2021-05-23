package com.example.android.binder

import android.os.Binder
import com.example.android.service.FloatService

class FloatBinder(private val service: FloatService) :Binder() {

    fun getService(): FloatService {
        return service
    }
}
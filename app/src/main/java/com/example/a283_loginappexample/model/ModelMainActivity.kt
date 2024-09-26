package com.example.a283_loginappexample.model

import android.content.Context
import com.example.a283_loginappexample.androidWrapper.DeviceInfo

class ModelMainActivity(private val context: Context) {

    fun getErrorEmail() = "ایمیل نباید خالی باشد"

    fun getAndroidId(): String {
        return DeviceInfo.getAndroidId(context)
    }

}
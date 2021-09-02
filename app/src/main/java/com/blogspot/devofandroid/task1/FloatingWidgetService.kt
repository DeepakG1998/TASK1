package com.blogspot.devofandroid.task1

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.view.WindowManager
import androidx.annotation.RequiresApi

class FloatingWidgetService: Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var floatingWidgetView: FloatingWidgetView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        floatingWidgetView = FloatingWidgetView(this)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()

        if(::windowManager.isInitialized) windowManager.removeView(floatingWidgetView)
    }


}
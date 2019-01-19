package com.morpheus.aditya.drive.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

class AlertService: Service(){


    companion object {
        const val TAG = "alert_service"
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        // we wont provide binding right now
        return null
    }
}
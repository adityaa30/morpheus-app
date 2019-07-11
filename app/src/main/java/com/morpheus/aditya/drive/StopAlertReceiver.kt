package com.morpheus.aditya.drive

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.morpheus.aditya.drive.service.AlertService

class StopAlertReceiver : BroadcastReceiver() {

    companion object {
        const val TAG = "stop_alert_receiver"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Intent(context, AlertService::class.java).apply {
            this.putExtra(DriveUtils.STOP_INTENT_EXTRA, true)
            context?.startService(this)
        }
    }
}

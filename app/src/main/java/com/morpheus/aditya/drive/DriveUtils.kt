package com.morpheus.aditya.drive

import com.github.mikephil.charting.data.Entry

object DriveUtils {
    const val CHANNEL_ID = "alert_notification_channel"
    const val NOTIFICATION_GROUP_ALERT_SERVICE = "notification_group_alert_service"
    const val NOTIFICATION_ID_ALERT = 1001

    const val IMAGE_WIDTH = 720
    const val IMAGE_HEIGHT = 1080

    const val SERVER_PORT = 8080
    const val SERVER_IP = "127.ngrok.io"
    const val API_URL = "http://192.168.43.183:8000/api/"

    const val PHOTO_UPLOAD_INTERVAL_TIME = 200L

    const val RECEIVER_INTENT_URI = "com.morpheus.aditya.drive.STOP_ALERT"
    const val STOP_INTENT_EXTRA = "stop_intent_extra"

    const val DATABASE_NAME = "database_ear.db"

    fun getEntries(): List<Entry> {
        return listOf(
            Entry()
        )
    }
}
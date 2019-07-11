package com.morpheus.aditya.drive.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.util.SparseIntArray
import android.view.Surface
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.morpheus.aditya.drive.DriveUtils
import com.morpheus.aditya.drive.home.HomeActivity
import com.morpheus.aditya.drive.R
import com.morpheus.aditya.drive.StopAlertReceiver
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class AlertService : Service() {

    companion object {
        const val TAG = "alert_service"
        val ORIENTATIONS = SparseIntArray().apply {
            this.append(Surface.ROTATION_0, 90)
            this.append(Surface.ROTATION_90, 0)
            this.append(Surface.ROTATION_180, 270)
            this.append(Surface.ROTATION_270, 180)
        }
    }

    private var cameraController: AutoPhoto? = null
    private val mCompositeDisposable = CompositeDisposable()

    override fun onCreate() {
        super.onCreate()
        cameraController = AutoPhoto(application)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null)
            startNotification(intent.getBooleanExtra(DriveUtils.STOP_INTENT_EXTRA, true))
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        // we wont provide binding right now
        return null
    }

    private fun startNotification(stop: Boolean) {
        val pendingIntent: PendingIntent =
            Intent(this, HomeActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }

        val stopAlertIntent = Intent(this, StopAlertReceiver::class.java)
        val stopAlertPendingIntent: PendingIntent =
            PendingIntent.getBroadcast(this, 0, stopAlertIntent, 0)

        val mBuilder = NotificationCompat.Builder(applicationContext, DriveUtils.CHANNEL_ID)
            .setSmallIcon(R.drawable.steering_wheel)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentTitle("Analyzing your face!")
            .setContentText("Please drive safe. If you're feeling sleepy, look for nearby hotels.. Take rest.")
            .setPriority(Notification.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setColorized(true)
            .setColor(ContextCompat.getColor(this, R.color.gray))
            .setDefaults(Notification.DEFAULT_ALL)
            .setAutoCancel(true)
            .setGroup(DriveUtils.NOTIFICATION_GROUP_ALERT_SERVICE)
            .addAction(R.drawable.alert_exit, getString(R.string.exit), stopAlertPendingIntent)
        val notification = mBuilder.build()

        if (stop) {
            if (!mCompositeDisposable.isDisposed) {
                mCompositeDisposable.dispose()
            }
            stopForeground(true)
        } else {
            startForeground(DriveUtils.NOTIFICATION_ID_ALERT, notification)
            startCapturingImages()
        }

    }

    private fun startCapturingImages() {
        cameraController?.openCamera()
        try {
            Thread.sleep(100)
        } catch (e: InterruptedException) {
            Log.v(TAG, e.toString())
        }

        mCompositeDisposable.add(Observable
            .interval(0, DriveUtils.PHOTO_UPLOAD_INTERVAL_TIME, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                cameraController?.takePicture()
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!mCompositeDisposable.isDisposed) {
            mCompositeDisposable.dispose()
        }
    }
}
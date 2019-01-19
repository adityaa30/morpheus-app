package com.morpheus.aditya.drive.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.media.Image
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.util.Log
import android.util.SparseIntArray
import android.view.Surface
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.morpheus.aditya.drive.DriveUtils
import com.morpheus.aditya.drive.HomeActivity
import com.morpheus.aditya.drive.R


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

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startNotification()
        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent?): IBinder? {
        // we wont provide binding right now
        return null
    }

    private fun startNotification() {
        val pendingIntent: PendingIntent =
            Intent(this, HomeActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }

        val mBuilder = NotificationCompat.Builder(applicationContext, DriveUtils.CHANNEL_ID)
            .setSmallIcon(R.drawable.steering_wheel)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentTitle("Analyzing your face!")
            .setPriority(Notification.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setColorized(true)
            .setColor(ContextCompat.getColor(this, R.color.gray))
            .setDefaults(Notification.DEFAULT_ALL)
            .setAutoCancel(true)
            .setGroup(DriveUtils.NOTIFICATION_GROUP_ALERT_SERVICE)
        val notification = mBuilder.build()
        startForeground(DriveUtils.NOTIFICATION_ID_ALERT, notification)

    }

    private var mCameraId: String? = null
    private var mCameraDevice: CameraDevice? = null
    private var mCameraCaptureSession: CameraCaptureSession? = null
    private var mCameraRequestBuilder: CaptureRequest.Builder? = null
    private var mImageReader: ImageReader? = null

    private var mBackgroundHandler: Handler? = null
    private var mBackgroundThread: HandlerThread? = null

    private fun takePicture() {
        mCameraDevice.takeIf { it != null }
            ?.apply {
                val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
                try {
                    val cameraCharacteristics = cameraManager.getCameraCharacteristics(this.id)
                    val reader: ImageReader = ImageReader.newInstance(
                        DriveUtils.IMAGE_WIDTH,
                        DriveUtils.IMAGE_HEIGHT,
                        ImageFormat.JPEG,
                        1
                    )
                    val captureBuilder = this.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
                    captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)

                    val imageReaderListener = ImageReader.OnImageAvailableListener {
                        var image: Image? = null
                        try {
                            image = reader.acquireLatestImage()
                            val buffer = image.planes[0].buffer
                            val byteArray = ByteArray(buffer.capacity())
                            buffer.get(byteArray)
                            // TODO : Send the image via a socket connection
                        } finally {
                            image?.close()
                        }
                    }

                    reader.setOnImageAvailableListener(imageReaderListener, mBackgroundHandler)


                } catch (e: CameraAccessException) {
                    Log.v(TAG, e.toString())
                }
            } ?: return

    }
}
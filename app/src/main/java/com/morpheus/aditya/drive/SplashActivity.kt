package com.morpheus.aditya.drive

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.morpheus.aditya.drive.home.HomeActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {

    private val mCompositeDisposable = CompositeDisposable()
    private lateinit var mLogo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // create the notification channel
        createNotificationChannel()

        mCompositeDisposable.add(
            Observable
                .timer(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe {
                    Intent(this@SplashActivity, HomeActivity::class.java).apply {
                        startActivity(this)
                        finish()
                    }
                }
        )
        mLogo = findViewById(R.id.splash_logo)
        mLogo.startAnimation(getAnimatorSet())
    }

    fun getAnimatorSet(): AnimationSet {
        val animationSet = AnimationSet(false)

        val translateAnimationToRight = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, -1f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f
        )
        translateAnimationToRight.interpolator = AccelerateDecelerateInterpolator()
        translateAnimationToRight.duration = 600
        translateAnimationToRight.fillAfter = true


        animationSet.addAnimation(translateAnimationToRight)
        animationSet.fillAfter = true
        return animationSet
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Alert Notification"
            val description = "Alerts about drowsiness level"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(DriveUtils.CHANNEL_ID, name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

}
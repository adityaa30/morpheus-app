package com.morpheus.aditya.drive

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import com.morpheus.aditya.drive.service.AlertService

class HomeActivity : AppCompatActivity() {

    private lateinit var mAlertMaterialCard: MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        mAlertMaterialCard = findViewById(R.id.home_alert_materialCard)
        mAlertMaterialCard.setOnClickListener {
            startAlertForegroundService()
        }
    }

    private fun startAlertForegroundService() {
        Intent(this@HomeActivity, AlertService::class.java).apply {
            startService(this)
        }
    }
}

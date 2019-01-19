package com.morpheus.aditya.drive

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView

class HomeActivity : AppCompatActivity() {

    private lateinit var mAlertMaterialCard: MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        mAlertMaterialCard = findViewById(R.id.home_alert_materialCard)
    }

    private fun startAlertForegroundService() {
        
    }
}

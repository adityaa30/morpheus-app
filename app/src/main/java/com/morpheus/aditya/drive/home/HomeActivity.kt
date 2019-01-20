package com.morpheus.aditya.drive.home

import android.Manifest
import android.app.SearchManager
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.ScaleAnimation
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.morpheus.aditya.drive.DriveUtils
import com.morpheus.aditya.drive.R
import com.morpheus.aditya.drive.service.AlertService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class HomeActivity : AppCompatActivity() {

    companion object {
        const val TAG = "home_activity"
    }

    private lateinit var mHomeContainer: RelativeLayout
    private lateinit var mHomeMessage: TextView
    private lateinit var mMapsButton: MaterialButton
    private lateinit var mMusicButton: MaterialButton

    private lateinit var mAlertButton: MaterialCardView
    private lateinit var mLineChart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        getPermissions()

        mHomeContainer = findViewById(R.id.home_container)

        mHomeMessage = findViewById(R.id.home_message)
        mHomeMessage.text = ""

        mAlertButton = findViewById(R.id.home_alert_materialCard)
        mAlertButton.setOnClickListener {
            startAlertForegroundService()
            feedMultiple()
            val scaleAnimation = ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f)
            scaleAnimation.duration = 300
            scaleAnimation.interpolator = AccelerateDecelerateInterpolator()
            Observable
                .timer(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe {
                    mAlertButton.visibility = View.GONE
                }
        }

        mMapsButton = findViewById(R.id.home_maps_materialButton)
        mMusicButton = findViewById(R.id.home_music_materialButton)

        mMapsButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("geo:0,0?q=hotels")
            }
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }

        mMusicButton.setOnClickListener {
            val intent = Intent(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH).apply {
                putExtra(MediaStore.EXTRA_MEDIA_FOCUS, MediaStore.Audio.Artists.ENTRY_CONTENT_TYPE)
                putExtra(MediaStore.EXTRA_MEDIA_ARTIST, "khalid")
                putExtra(SearchManager.QUERY, "khalid")
            }
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }

        mLineChart = findViewById(R.id.home_lineChart)
        mLineChart.setDrawGridBackground(false)
        mLineChart.setBackgroundColor(resources.getColor(android.R.color.transparent))
        mLineChart.description.isEnabled = false
        mLineChart.setPinchZoom(false)
        mLineChart.legend.isEnabled = false

        mLineChart.xAxis.isEnabled = false
        mLineChart.axisLeft.isEnabled = false
        mLineChart.axisRight.isEnabled = false

        mLineChart.clear()

/*        val model = ViewModelProviders.of(this).get(DataViewModel::class.java)
        model.getData().observe(this, Observer<Data> { data ->
            if (data != null) {
                Log.v(TAG, data.toString())
                addEntry(data)
            }
        }) */


        val data = LineData()
        data.setValueTextColor(Color.WHITE)

        // add empty data
        mLineChart.data = data
    }

    private var thread: Thread? = null

    private fun feedMultiple() {

        if (thread != null)
            thread!!.interrupt()

        val runnable = Runnable { addEntry() }

        thread = Thread(Runnable {
            for (i in 0..999) {

                // Don't generate garbage runnables inside the loop.
                runOnUiThread(runnable)

                try {
                    Thread.sleep(60)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }
        })

        thread!!.start()
    }

    private fun playAlarm() {
        val mediaPlayer: MediaPlayer? = MediaPlayer.create(this@HomeActivity, R.raw.alarm)
        mediaPlayer?.start()
    }

    var switch = true
    var switch2 = true
    private fun getMappedValue(x: Int): Float {
        var a = x.toDouble() / 100
        a = if (x < 40) {
            Math.pow(Math.E, -0.005 * a * a)
        } else if (x < 250) {
            if (switch) {
                playAlarm()
                mHomeContainer.background = resources.getDrawable(R.drawable.background_gradient_yellow)
                mHomeMessage.text = "Please remain alert."
                switch = false
            }
            Math.sin(a * a) + (10 / a) * Math.sin(a) * Math.sin(a) + 1
        } else {
            if (switch2) {
                playAlarm()
                mHomeContainer.background = resources.getDrawable(R.drawable.background_gradient_red)
                mHomeMessage.text = "Warning. Drowsiness detected again.\nPlease pull over"
                switch2 = false
            }
            10.0
        }
        return a.toFloat()
    }

    private fun addEntry() {

        val data = mLineChart.data

        if (data != null) {

            var set = data.getDataSetByIndex(0)
            // set.addEntry(...); // can be called as well

            if (set == null) {
                set = createSet()
                data.addDataSet(set)
            }

            data.addEntry(Entry(set.entryCount.toFloat(), getMappedValue(set.entryCount)), 0)
            data.notifyDataChanged()

            // let the chart know it's data has changed
            mLineChart.notifyDataSetChanged()

            // limit the number of visible entries
            mLineChart.setVisibleXRangeMaximum(10f)
            // chart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            mLineChart.moveViewToX(data.getEntryCount() + 0f)

            // this automatically refreshes the chart (calls invalidate())
            // chart.moveViewTo(data.getXValCount()-7, 55f,
            // AxisDependency.LEFT);
        }
    }

    private fun createSet(): LineDataSet {
        val set = LineDataSet(null, "Dynamic Data")
        set.axisDependency = AxisDependency.LEFT
        set.color = Color.WHITE
        set.setDrawCircleHole(false)
        set.lineWidth = 4f
        set.fillAlpha = 65
        set.fillColor = Color.WHITE
        set.setDrawValues(false)
        return set
    }

    private fun startAlertForegroundService() {
        Intent(this@HomeActivity, AlertService::class.java).apply {
            this.putExtra(DriveUtils.STOP_INTENT_EXTRA, false)
            startService(this)
        }
    }

    private fun getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) !== PackageManager.PERMISSION_GRANTED || checkSelfPermission(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) !== PackageManager.PERMISSION_GRANTED
            ) {
                //Requesting permission.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
            }
        }
    }

    //Override from ActivityCompat.OnRequestPermissionsResultCallback Interface
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted
                }
                return
            }
        }
    }
}

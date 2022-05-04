package com.example.weatherapp

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.weatherapp.Constants.ELAPSED_TIME
import com.example.weatherapp.Constants.TIMER_UPDATED
import java.util.*

class NotificationService : Service() {

    override fun onBind(p0: Intent?): IBinder? = null

    private val timer = Timer()

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val time = intent.getIntExtra(ELAPSED_TIME, 0)
        timer.scheduleAtFixedRate(TimeTask(time), 1800000, 1800000)
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        timer.cancel()
        super.onDestroy()
    }

    private inner class TimeTask(private var time: Int) : TimerTask() {
        override fun run() {
            val intent = Intent(TIMER_UPDATED)
            time += 30
            intent.putExtra(ELAPSED_TIME, time)
            sendBroadcast(intent)
        }
    }

}
package com.example.weatherapp

import android.Manifest

object Constants {
    const val manifestLocationPermission: String = Manifest.permission.ACCESS_COARSE_LOCATION

    const val ACTION_SHOW_CURRENT_CONDITIONS_FRAGMENT = "ACTION_SHOW_CURRENT_CONDITIONS_FRAGMENT"

    const val NOTIFICATION_CHANNEL_ID = "Weather_Channel"
    const val NOTIFICATION_CHANNEL_NAME = "Weather"


    const val TIMER_UPDATED = "timerUpdated"
    const val ELAPSED_TIME = "elapsedTime"
}
package com.example.managingtask.util

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class DeleteBtnReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val manager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(AlarmManagerReceiver.NOTIFICATION_ID)
    }
}
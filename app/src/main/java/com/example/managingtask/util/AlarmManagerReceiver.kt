package com.example.managingtask.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.managingtask.R
import com.example.managingtask.activities.mainActivity.MainActivity

class AlarmManagerReceiver: BroadcastReceiver() {

    companion object {
        const val NOTIFICATION_ID = 101
        const val CHANNEL_ID = "channel_ID"
        const val CHANNEL_NAME = "channel_name"
        const val TASK_TITLE = "taskTitle"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        var titleTask = ""

        if (intent != null && intent.hasExtra(TASK_TITLE)) titleTask = intent.getStringExtra(TASK_TITLE)!!

        val mainIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val deleteIntent = Intent(context, DeleteBtnReceiver::class.java)
        val deletePendingIntent = PendingIntent.getBroadcast(context, 1, deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationManager =  NotificationManagerCompat.from(context!!)
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Reminder")
            .setContentText(titleTask)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .addAction(R.mipmap.ic_launcher, "Delete", deletePendingIntent)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder)
    }
}
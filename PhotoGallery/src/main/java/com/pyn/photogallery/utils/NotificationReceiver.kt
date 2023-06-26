package com.pyn.photogallery.utils

import android.app.Activity
import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat

/**
 * Description：
 * @author Created by pengyanni
 * @e-mail 393507488@qq.com
 * @time   2023/6/15 09:54
 */
private const val TAG = "NotificationReceiver"

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.i(TAG, "received broadcast = ${intent.action}")
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        val requestCode = intent.getIntExtra(PollWorker.REQUEST_CODE, 0)
        val notification: Notification? = intent.getParcelableExtra(PollWorker.NOTIFICATION)
        val notificationManager = NotificationManagerCompat.from(context)
        notification?.let { notificationManager.notify(requestCode, it) }
    }
}
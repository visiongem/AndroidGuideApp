package com.pyn.photogallery.utils

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * Description：
 * @author Created by pengyanni
 * @e-mail 393507488@qq.com
 * @time   2023/6/8 16:11
 */
private const val TAG = "PollWorker"

class PollWorker(val context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        Log.i(TAG,"Work request triggered")
        return  Result.success()
    }
}
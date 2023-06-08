package com.pyn.photogallery.utils

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.pyn.photogallery.bean.GalleryItem
import com.pyn.photogallery.net.FlickrFetchr

/**
 * Description：
 * @author Created by pengyanni
 * @e-mail 393507488@qq.com
 * @time   2023/6/8 16:11
 */
private const val TAG = "PollWorker"

class PollWorker(val context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        Log.i(TAG, "Work request triggered")
        val query = QueryPreferences.getStoredQuery(context)
        val lastResultId = QueryPreferences.getLastResultId(context)
        val items: List<GalleryItem> = if (query.isEmpty()) {
            FlickrFetchr().fetchPhototsRequest()
                .execute()
                .body()
                ?.photos
                ?.galleryItems
        } else {
            FlickrFetchr().searchPhotosRequest(query)
                .execute()
                .body()
                ?.photos
                ?.galleryItems
        } ?: emptyList()

        if (items.isEmpty()){
            return Result.success()
        }

        val resultId = items.first().id
        if (resultId == lastResultId){
            Log.i(TAG, "Got an old result :$resultId")
        }else{
            Log.i(TAG, "Got an new result :$resultId")
            QueryPreferences.setLastResultId(context, resultId)
        }
        return Result.success()
    }
}
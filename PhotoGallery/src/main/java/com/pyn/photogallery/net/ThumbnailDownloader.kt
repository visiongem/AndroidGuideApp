package com.pyn.photogallery.net

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import java.util.concurrent.ConcurrentHashMap

/**
 * Description：
 * @author Created by pengyanni
 * @e-mail 393507488@qq.com
 * @time   2023/5/11 18:03
 */
private const val TAG = "ThumbnailDownloader"

// 标识下载请求消息
private const val MESSAGE_DOWNLOAD = 0

class ThumbnailDownloader<in T>(
    private val responseHandler: Handler
//    private val onThumbnailDownloader: (Bitmap) -> Unit
) : HandlerThread(TAG),
    LifecycleObserver {

    private var hasQuit = false
    private lateinit var requestHandler: Handler
    private val requestMap = ConcurrentHashMap<T, String>()
    private val flickrFetchr = FlickrFetchr()
    private lateinit var onThumbnailDownloader: (T, Bitmap) -> Unit

    override fun quit(): Boolean {
        hasQuit = true
        return super.quit()
    }

    fun setThumbnailDownloader(loader: (@UnsafeVariance T, Bitmap) -> Unit) {
        onThumbnailDownloader = loader
    }

    fun queueThumbnail(target: T, url: String) {
        Log.i(TAG, "Got a URL:$url")
        requestMap[target] = url
        requestHandler.obtainMessage(MESSAGE_DOWNLOAD, target).sendToTarget()
    }

    @SuppressLint("HandlerLeak")
    @Suppress("UNCHECKED_CAST")
    override fun onLooperPrepared() {
        requestHandler = object : Handler() {
            override fun handleMessage(msg: Message) {
                if (msg.what == MESSAGE_DOWNLOAD) {
                    val target = msg.obj as T
                    Log.i(TAG, "Got a request for URL:${requestMap[target]}")
                    handleRequest(target)
                }
            }
        }
    }

    private fun handleRequest(target: T) {
        val url = requestMap[target] ?: return
        val bitmap = flickrFetchr.fetchPhoto(url) ?: return

        responseHandler.post(Runnable {
            if (requestMap[target] != url || hasQuit) {
                return@Runnable
            }
            requestMap.remove(target)
            onThumbnailDownloader(target, bitmap)
        })
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun setup() {
        Log.i(TAG, "Starting background thread")
        start()
        looper
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun tearDown() {
        Log.i(TAG, "Destroying background thread")
        quit()
    }
}
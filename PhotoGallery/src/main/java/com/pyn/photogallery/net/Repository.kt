package com.pyn.photogallery.net

import com.pyn.photogallery.bean.FlickrResponse
import retrofit2.Call

/**
 * Description：
 * @author Created by pengyanni
 * @e-mail 393507488@qq.com
 * @time   2022/12/7 15:48
 */
class Repository {

    private lateinit var flickrCall: Call<FlickrResponse>

    fun addFlickrCall(call: Call<FlickrResponse>) {
        flickrCall = call
    }

    fun cancelRequestInFlight() {
        if (::flickrCall.isInitialized) {
            flickrCall.cancel()
        }
    }
}
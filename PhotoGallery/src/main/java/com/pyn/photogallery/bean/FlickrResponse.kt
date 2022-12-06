package com.pyn.photogallery.bean

/**
 * Description：
 * @author Created by pengyanni
 * @e-mail 393507488@qq.com
 * @time   2022/12/5 18:03
 */
class FlickrResponse {

    lateinit var photos: PhotoResponse

    override fun toString(): String {
        return "FlickrResponse(photos=$photos)"
    }
}
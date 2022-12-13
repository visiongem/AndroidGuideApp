package com.pyn.photogallery.api

import com.pyn.photogallery.FlickrConstants
import com.pyn.photogallery.bean.FlickrResponse
import com.pyn.photogallery.bean.PhotoDeserializer
import retrofit2.Call
import retrofit2.http.GET

interface FlickrApi {

    @GET("/")
    fun fetchContents(): Call<String>

    @GET(
        "services/rest/?method=flickr.interestingness.getList"
                + "&api_key=${FlickrConstants.FLICKR_KEY}"
                + "&format=json&nojsoncallback=1"
                + "&extras=url_s"
    )
    fun fetchPhotos(): Call<PhotoDeserializer>
}
package com.pyn.photogallery.net

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.pyn.photogallery.api.FlickrApi
import com.pyn.photogallery.api.PhotoInterceptor
import com.pyn.photogallery.bean.FlickrResponse
import com.pyn.photogallery.bean.GalleryItem
import com.pyn.photogallery.bean.PhotoDeserializer
import com.pyn.photogallery.bean.PhotoResponse
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "FlickrFetchr"

class FlickrFetchr {

    private val flickrApi: FlickrApi

    init {

        val gsonPhotoDeserializer = GsonBuilder()
            .registerTypeAdapter(PhotoResponse::class.java, PhotoDeserializer())
            .create()

        val httpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        httpClientBuilder.addInterceptor(PhotoInterceptor())
        httpClientBuilder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.flickr.com/")
            .client(httpClientBuilder.build())
//            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .addConverterFactory(GsonConverterFactory.create(gsonPhotoDeserializer))
//            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()))
            .build()
        flickrApi = retrofit.create(FlickrApi::class.java)
    }

    fun fetchPhototsRequest(): Call<FlickrResponse> {
        return flickrApi.fetchPhotosResponse()
    }

    fun fetchPhotosResponse(): LiveData<List<GalleryItem>> {
//        return fetchPhotoMetadata(flickrApi.fetchPhotosResponse())
        return fetchPhotoMetadata(fetchPhototsRequest())
    }

    fun searchPhotos(query: String): LiveData<List<GalleryItem>> {
//        return fetchPhotoMetadata(flickrApi.searchPhotos(query))
        return fetchPhotoMetadata(searchPhotosRequest(query))
    }

    fun searchPhotosRequest(query: String):Call<FlickrResponse>{
        return flickrApi.searchPhotos(query)
    }

    private fun fetchPhotoMetadata(flickrRequest: Call<FlickrResponse>): LiveData<List<GalleryItem>> {
        val responseLiveData: MutableLiveData<List<GalleryItem>> = MutableLiveData()

        val repository = Repository()
        repository.addFlickrCallResponse(flickrRequest)

        flickrRequest.enqueue(object : Callback<FlickrResponse> {
            override fun onResponse(call: Call<FlickrResponse>, response: Response<FlickrResponse>) {
                Log.d(TAG, "Response received : ${response.body()}")

                val flickrResponse: FlickrResponse? = response.body()
                val photoResponse: PhotoResponse? = flickrResponse?.photos
                var galleryItems: List<GalleryItem> = photoResponse?.galleryItems ?: mutableListOf()
                galleryItems = galleryItems.filterNot { it.url.isBlank() }
                responseLiveData.value = galleryItems
            }

            override fun onFailure(call: Call<FlickrResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch photos", t)
                if (call.isCanceled) {
                    Log.e(TAG, "request Canceled")
                }
            }
        })
        return responseLiveData
    }

    fun fetchPhotos(): LiveData<List<GalleryItem>> {
        val responseLiveData: MutableLiveData<List<GalleryItem>> = MutableLiveData()
        val flickrRequest: Call<PhotoDeserializer> = flickrApi.fetchPhotos()

        val repository = Repository()
        repository.addFlickrCall(flickrRequest)

        flickrRequest.enqueue(object : Callback<PhotoDeserializer> {
            override fun onResponse(call: Call<PhotoDeserializer>, response: Response<PhotoDeserializer>) {
                Log.d(TAG, "Response received : ${response.body()}")

                val photoDeserializer: PhotoDeserializer? = response.body()
                val photoResponse: PhotoResponse? = photoDeserializer?.photos
                var galleryItems: List<GalleryItem> = photoResponse?.galleryItems ?: mutableListOf()
                galleryItems = galleryItems.filterNot { it.url.isBlank() }
                responseLiveData.value = galleryItems
            }

            override fun onFailure(call: Call<PhotoDeserializer>, t: Throwable) {
                Log.e(TAG, "Failed to fetch photos", t)
                if (call.isCanceled) {
                    Log.e(TAG, "request Canceled")
                }
            }
        })
        return responseLiveData
    }

    fun fetchContents(): LiveData<String> {

        val responseLiveData: MutableLiveData<String> = MutableLiveData()

        val flickrHomePageRequest: Call<String> = flickrApi.fetchContents()

        flickrHomePageRequest.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.d(TAG, "Response received : ${response.body()}")
                responseLiveData.value = response.body()
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e(TAG, "Failed to fetch photos", t)
            }
        })
        return responseLiveData
    }

    @WorkerThread
    fun fetchPhoto(url: String): Bitmap? {
        val response: Response<ResponseBody> = flickrApi.fetchUrlBytes(url).execute()
        val bitmap = response.body()?.byteStream()?.use(BitmapFactory::decodeStream)
        Log.i(TAG, "Decoded bitmap = $bitmap from Response = $response")
        return bitmap
    }

}
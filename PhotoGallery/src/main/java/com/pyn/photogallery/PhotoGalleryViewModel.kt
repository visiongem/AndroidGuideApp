package com.pyn.photogallery

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.pyn.photogallery.bean.GalleryItem
import com.pyn.photogallery.net.FlickrFetchr
import com.pyn.photogallery.net.Repository
import com.pyn.photogallery.utils.QueryPreferences

/**
 * Description：
 * @author Created by pengyanni
 * @e-mail 393507488@qq.com
 * @time   2022/12/6 15:43
 */
class PhotoGalleryViewModel(private val app: Application) : AndroidViewModel(app) {

    private val repository = Repository()

    val galleryItemLiveData: LiveData<List<GalleryItem>>
    private val flickrFetchr = FlickrFetchr()
    private val mutableSearchTerm = MutableLiveData<String>()

    val searchTerm: String get() = mutableSearchTerm.value ?: ""

    init {
        mutableSearchTerm.value = QueryPreferences.getStoredQuery(app)/*"cat"*/
        galleryItemLiveData = mutableSearchTerm.switchMap { searchTerm ->
            if (searchTerm.isBlank()) {
                flickrFetchr.fetchPhotos()
            } else {
                flickrFetchr.searchPhotos(searchTerm)
            }
        }
    }

    fun fetchPhotos(query: String = "") {
        QueryPreferences.setStoredQuery(app, query)
        mutableSearchTerm.value = query
    }

    override fun onCleared() {
        super.onCleared()
        repository.cancelRequestInFlight()
    }

}
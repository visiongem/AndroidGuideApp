package com.pyn.photogallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.pyn.photogallery.bean.GalleryItem
import com.pyn.photogallery.net.FlickrFetchr
import com.pyn.photogallery.net.Repository

/**
 * Description：
 * @author Created by pengyanni
 * @e-mail 393507488@qq.com
 * @time   2022/12/6 15:43
 */
class PhotoGalleryViewModel : ViewModel() {

    private val repository = Repository()

    val galleryItemLiveData: LiveData<List<GalleryItem>>
    private val flickrFetchr = FlickrFetchr()
    private val mutableSearchTerm = MutableLiveData<String>()

    init {
        mutableSearchTerm.value = "cat"
        galleryItemLiveData = mutableSearchTerm.switchMap { searchTerm ->
            flickrFetchr.searchPhotos(searchTerm)
        }
    }

    fun fetchPhotos(query: String = "") {
        mutableSearchTerm.value = query
    }

    override fun onCleared() {
        super.onCleared()
        repository.cancelRequestInFlight()
    }

}
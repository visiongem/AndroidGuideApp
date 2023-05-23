package com.pyn.photogallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
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

    val galleryItemLiveData: LiveData<List<GalleryItem>> = FlickrFetchr().searchPhotos("bicycle")

    override fun onCleared() {
        super.onCleared()
        repository.cancelRequestInFlight()
    }

}
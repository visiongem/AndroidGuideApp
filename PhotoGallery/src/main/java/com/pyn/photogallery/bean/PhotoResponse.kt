package com.pyn.photogallery.bean

import com.google.gson.annotations.SerializedName

/**
 * Description：
 * @author Created by pengyanni
 * @e-mail 393507488@qq.com
 * @time   2022/12/5 18:01
 */
class PhotoResponse {
    @SerializedName("photo")
    lateinit var galleryItems:List<GalleryItem>
}
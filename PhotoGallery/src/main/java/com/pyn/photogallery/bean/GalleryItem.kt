package com.pyn.photogallery.bean

import android.net.Uri
import com.google.gson.annotations.SerializedName

/**
 * Description：
 * @author Created by pengyanni
 * @e-mail 393507488@qq.com
 * @time   2022/12/5 17:00
 */
data class GalleryItem(
    var title: String = "",
    var id: String = "",
    @SerializedName("url_s")
    var url: String = "",
    @SerializedName("owner")
    var owner: String = ""
) {
    val photoPageUri: Uri
        get() {
            return Uri.parse("https://www.flickr.com/photos/")
                .buildUpon()
                .appendPath(owner)
                .appendPath(id)
                .build()
        }
}

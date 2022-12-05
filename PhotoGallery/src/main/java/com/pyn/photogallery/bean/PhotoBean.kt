package com.pyn.photogallery.bean

data class PhotoBean(
    val extra: Extra,
    val photos: Photos,
    val stat: String
) {
    data class Extra(
        val explore_date: String,
        val next_prelude_interval: String
    )

    data class Photos(
        val page: Int,
        val pages: Int,
        val perpage: Int,
        val photo: List<Photo>,
        val total: String
    ) {
        data class Photo(
            val farm: Int,
            val id: String,
            val isfamily: Int,
            val isfriend: Int,
            val ispublic: Int,
            val owner: String,
            val secret: String,
            val server: String,
            val title: String
        )
    }
}
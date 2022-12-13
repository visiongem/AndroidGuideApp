package com.pyn.photogallery.bean

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

/**
 * Description：
 * @author Created by pengyanni
 * @e-mail 393507488@qq.com
 * @time   2022/12/8 17:56
 */
class PhotoDeserializer : JsonDeserializer<PhotoResponse>{

    lateinit var photos : PhotoResponse

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): PhotoResponse {
        val photoJsonObject = json?.asJsonObject
        val gson = Gson()

        return gson.fromJson(photoJsonObject, PhotoResponse::class.java)
    }
}
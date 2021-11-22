package com.pyn.beatbox

import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData

class SoundViewModel(private val beatBox: BeatBox) /*: BaseObservable() */{

    fun onButtonClicked() {
        sound?.let { beatBox.play(it) }
    }

    val title :MutableLiveData<String?> = MutableLiveData()

    var sound: Sound? = null
        set(sound) {
            field = sound
            /*notifyChange()*/
            title.postValue(sound?.name)
        }

   /* @get:Bindable
    val title: String?
        get() = sound?.name*/
}

@BindingAdapter("app:isGone")
fun bindIsGone(view: View, isGone: Boolean) {
    view.visibility = if (isGone) View.GONE else View.VISIBLE
}
package com.pyn.beatbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pyn.beatbox.databinding.ActivityMainBinding
import com.pyn.beatbox.databinding.ListItemSoundBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = SoundAdapter()
        }
    }

    private inner class SoundHolder(private val binding:ListItemSoundBinding):RecyclerView.ViewHolder(binding.root){

    }

    private inner class SoundAdapter():RecyclerView.Adapter<SoundHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            SoundHolder{
                val binding = DataBindingUtil.inflate<ListItemSoundBinding>(
                    layoutInflater,
                    R.layout.list_item_sound,
                    parent,
                    false
                )
                return SoundHolder(binding)
        }

        override fun onBindViewHolder(holder: SoundHolder, position: Int) {

        }
        override fun getItemCount() = 0
    }
}
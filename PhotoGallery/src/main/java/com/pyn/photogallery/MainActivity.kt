package com.pyn.photogallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pyn.photogallery.api.FlickrApi
import com.pyn.photogallery.databinding.ActivityMainBinding
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory.create

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val isFragmentContainerEmpty = savedInstanceState == null
        if (isFragmentContainerEmpty){
            supportFragmentManager
                .beginTransaction()
                .add(R.id.flayout_container, PhotoGalleryFragment.newInstance())
                .commit()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
package com.pyn.photogallery

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.pyn.photogallery.databinding.ActivityPhotoPageBinding

/**
 * Description：
 * @author Created by pengyanni
 * @e-mail 393507488@qq.com
 * @time   2023/6/26 16:55
 */
class PhotoPageActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityPhotoPageBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityPhotoPageBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val fm = supportFragmentManager
        val currentFragment = fm.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
            val fragment = intent.data?.let { PhotoPageFragment.newInstance(it) }
            if (fragment != null) {
                fm.beginTransaction().add(R.id.fragment_container, fragment).commit()
            }
        }
    }

    companion object {
        fun newIntent(context: Context, photoPageUri: Uri): Intent {
            return Intent(context, PhotoPageActivity::class.java).apply {
                data = photoPageUri
            }
        }
    }
}
package com.yn.sunset

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import androidx.core.content.ContextCompat
import com.yn.sunset.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var initSunTop: Float = 0.0f
    private var initSunBottom: Float = 0.0f
    private var isUp = false

    private val blueSkyColor: Int by lazy {
        ContextCompat.getColor(this@MainActivity, R.color.blue_sky)
    }
    private val sunsetSkyColor: Int by lazy {
        ContextCompat.getColor(this@MainActivity, R.color.sunset_sky)
    }
    private val nightSkyColor: Int by lazy {
        ContextCompat.getColor(this@MainActivity, R.color.night_sky)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        binding.sky.post {
            initSunTop = binding.sky.measuredHeight / 2 - 50f.dp
            initSunBottom = binding.sky.measuredHeight / 2 + 50f.dp
        }

        binding.scene.setOnClickListener {
            isUp = if (isUp) {
                startUpAnimator()
                false
            } else {
                startAnimation()
                true
            }
        }
    }

    private fun startAnimation() {
        val sunStartY = initSunTop
        val sunEndY = binding.sky.height.toFloat()

        val heightAnimator = ObjectAnimator.ofFloat(binding.sun, "y", sunStartY, sunEndY).setDuration(3000)
        heightAnimator.interpolator = AccelerateInterpolator()

        val sunsetSkyAnimator = ObjectAnimator
            .ofInt(binding.sky, "backgroundColor", blueSkyColor, sunsetSkyColor).setDuration(3000)
        sunsetSkyAnimator.setEvaluator(ArgbEvaluator())

        val nightSkyAnimator = ObjectAnimator
            .ofInt(binding.sky, "backgroundColor", sunsetSkyColor, nightSkyColor).setDuration(1500)
        nightSkyAnimator.setEvaluator(ArgbEvaluator())

        val animatorSet = AnimatorSet()
        animatorSet.play(heightAnimator).with(sunsetSkyAnimator).before(nightSkyAnimator)
        animatorSet.start()
        /*     heightAnimator.start()
             sunsetSkyAnimator.start()*/
    }

    private fun startUpAnimator() {
        val sunStartY = binding.sky.height.toFloat()
        val sunEndY = initSunTop

        val heightAnimator = ObjectAnimator.ofFloat(binding.sun, "y", sunStartY, sunEndY).setDuration(3000)
        heightAnimator.interpolator = AccelerateInterpolator()

        val sunsetSkyAnimator = ObjectAnimator
            .ofInt(binding.sky, "backgroundColor", sunsetSkyColor, blueSkyColor).setDuration(3000)
        sunsetSkyAnimator.setEvaluator(ArgbEvaluator())

        val nightSkyAnimator = ObjectAnimator
            .ofInt(binding.sky, "backgroundColor", nightSkyColor, sunsetSkyColor).setDuration(1500)
        nightSkyAnimator.setEvaluator(ArgbEvaluator())

        val animatorSet = AnimatorSet()
        animatorSet.play(nightSkyAnimator).before(heightAnimator).with(sunsetSkyAnimator)
        animatorSet.start()
    }
}